package com.miracle.service;

import com.miracle.dto.PatientDto;
import com.miracle.model.Patient;
import com.miracle.model.User;
import com.miracle.repository.PatientRepository;
import com.miracle.repository.UserRepository;
import com.miracle.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.miracle.model.Appointment;
import com.miracle.model.Doctor;
import com.miracle.model.DoctorAvailableSlotTime;
import com.miracle.model.DoctorSpecialisation;
import com.miracle.repository.DoctorRepository;
import com.miracle.repository.DoctorSpecialisationRepository;
import com.miracle.repository.PatientDoctorChatRepository;
import com.miracle.repository.LocationRepository;
import com.miracle.repository.PatientDocumentRepository;
import com.miracle.model.PatientDocument;

import org.springframework.beans.factory.annotation.Value;

import com.miracle.repository.AppointmentRepository;
import com.miracle.repository.DoctorAvailableSlotTimeRepository;

import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import com.miracle.util.IdGenerator;
import com.miracle.model.PatientDoctorChat;

@Service
@Slf4j
@RequiredArgsConstructor
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final WhatsappService whatsappService;
    private final JwtUtil jwtUtil;
    private final DoctorRepository doctorRepository;
    private final DoctorSpecialisationRepository doctorSpecialisationRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorAvailableSlotTimeRepository doctorAvailableSlotTimeRepository;
    private final IdGenerator idGenerator;
    private final PatientDoctorChatRepository patientDoctorChatRepository;
    private final NotificationService notificationService;
    private final LocationRepository locationRepository;
    private final PatientDocumentRepository patientDocumentRepository;
   

    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    // ── PATIENT LOGIN ────────────────────────────────────────────────────────
    public Map<String, Object> patientLogin(String mobileNo) {
        Map<String, Object> resp   = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        Optional<User> userOpt = userRepository.findFirstByPhoneNumberOrderByUserIdAsc(mobileNo);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            String otp = generateOTP(true);
            user.setOtp(otp);
            userRepository.save(user);
            String otpMsg = "Dear Guest, Your OTP is " + otp + " Regards, Miracle Hospital.";
            whatsappService.sendMessage(mobileNo, otpMsg);

            result.put("userId", user.getUserStringId());
            Optional<Patient> patientOpt = patientRepository.findFirstByUserAndRelation(user, "Self");
            result.put("patientId", patientOpt.map(Patient::getPatientStringId).orElse(""));
            result.put("otpStatus", "USER-EXIST");
            resp.put("status",  "success");
            resp.put("message", "OTP sent successfully");
            resp.put("result",  result);
        } else {
            String uniqueUserName  = "USER_" + mobileNo + "_" + System.currentTimeMillis();
            String newUserStringId = idGenerator.generateUniqueId();
            User newUser = User.builder()
                    .userStringId(newUserStringId)
                    .userName(uniqueUserName)
                    .phoneNumber(mobileNo)
                    .userPhone(mobileNo)
                    .email("")
                    .otp(generateOTP(true) + "##SEND##" + mobileNo)
                    .password("")
                    .userRole(User.UserRole.PATIENT)
                    .isActive(true)
                    .serverMode("DEVELOPMENT")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            userRepository.save(newUser);
            String newOtpMsg = "Dear Guest, Your OTP is " + newUser.getOtp() + " Regards, Miracle Hospital.";
            whatsappService.sendMessage(mobileNo, newOtpMsg);

            result.put("userId",    newUserStringId);
            result.put("patientId", "");
            result.put("otpStatus", "USER-NOTEXIST");
            resp.put("status",  "success");
            resp.put("message", "OTP sent successfully");
            resp.put("result",  result);
        }
        return resp;
    }

    // ── VALIDATE OTP ─────────────────────────────────────────────────────────
    @Transactional
    public Map<String, Object> validatePatientOtp(String mobileNo, String otp, String fcmId) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findFirstByPhoneNumberOrderByUserIdAsc(mobileNo);

        if (!userOpt.isPresent()) {
            response.put("status",  "failed");
            response.put("message", "User not found");
            return response;
        }

        User user = userOpt.get();
        if (!otp.equals(user.getOtp())) {
            response.put("status",  "failed");
            response.put("message", "Invalid OTP");
            return response;
        }

        user.setTokenId(fcmId);
        userRepository.save(user);

        Optional<Patient> patientOpt = patientRepository.findFirstByUserAndRelation(user, "Self");
        Patient selfPatient = patientOpt.orElse(null);

        String profileStatus = (selfPatient != null
                && selfPatient.getPatientName() != null
                && !selfPatient.getPatientName().isEmpty()) ? "COMPLETED" : "PENDING";

        String token = jwtUtil.generateToken(user.getUserName(), user.getUserRole().toString());

        Map<String, Object> result = new HashMap<>();
        result.put("userId",        user.getUserStringId());
        result.put("patientId",     selfPatient != null ? selfPatient.getPatientStringId() : "");
        result.put("mobileNo",      mobileNo);
        result.put("profileStatus", profileStatus);

        response.put("status",  "success");
        response.put("message", "OTP verified successfully");
        response.put("token",   token);
        response.put("result",  result);
        return response;
    }

    // ── RESEND OTP ───────────────────────────────────────────────────────────
    public Map<String, Object> resendOtp(String mobileNo) {
        Map<String, Object> response = new HashMap<>();
        Optional<User> userOpt = userRepository.findFirstByPhoneNumberOrderByUserIdAsc(mobileNo);

        if (!userOpt.isPresent()) {
            response.put("status",  "failed");
            response.put("message", "User not found");
            return response;
        }

        User user = userOpt.get();
        user.setOtp(generateOTP(true));
        userRepository.save(user);

        response.put("status",  "success");
        response.put("userId",  user.getUserStringId());
        response.put("message", "OTP sent successfully");
        return response;
    }

    // ── SET PROFILE ──────────────────────────────────────────────────────────
    @Transactional
    public Map<String, Object> setProfile(String patientId, String userId, String patientName,
                                          String relation, String phoneNo, String dob,
                                          String gender, String age, String isPregnant,
                                          String lmpDate, String expectedDeliveryDate) {
        Map<String, Object> response = new HashMap<>();

        // FIX: skip DB lookup when patientId is empty
        Optional<Patient> patientOpt = (patientId != null && !patientId.isEmpty())
                ? patientRepository.findByPatientStringId(patientId)
                : Optional.empty();

        Patient patient;
        if (!patientOpt.isPresent()) {
            Optional<User> userOpt = userRepository.findByUserStringId(userId);
            if (!userOpt.isPresent()) {
                response.put("status",  "failed");
                response.put("message", "User not found");
                return response;
            }
            String newPatientStringId = idGenerator.generateUniqueId();
            patient = Patient.builder()
                    .patientStringId(newPatientStringId)
                    .user(userOpt.get())
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .build();
        } else {
            patient = patientOpt.get();
        }

        patient.setPatientName(patientName);
        patient.setRelation(relation != null ? relation : "Self");
        patient.setPhoneNumber(phoneNo);
        patient.setPatientGender(gender);
        patient.setPatientDateOfBirth(dob);
        patient.setPatientAge(age != null ? age : "");
        patient.setPatientStatus("1");
        patient.setIsPregnant(isPregnant != null ? isPregnant : "0");
        patient.setLmpDate(lmpDate);
        patient.setExpectedDeliveryDate(expectedDeliveryDate);
        patientRepository.save(patient);

        Map<String, Object> result = new HashMap<>();
        result.put("patientId",          patient.getPatientStringId());
        result.put("profileStatus",      "COMPLETED");
        result.put("patientName",        patientName);
        result.put("patientRelation",    relation);
        result.put("patientPhone",       phoneNo);
        result.put("patientDateOfBirth", dob);
        result.put("patientGender",      gender);

        response.put("status",  "success");
        response.put("message", "Profile updated successfully");
        response.put("result",  result);
        return response;
    }

    // ── GET PROFILE ──────────────────────────────────────────────────────────
    // FIX: fallback to userId when patientId is empty
    public Map<String, Object> getProfile(String patientId, String userId) {
        Map<String, Object> response = new HashMap<>();

        Optional<Patient> patientOpt = (patientId != null && !patientId.isEmpty())
                ? patientRepository.findByPatientStringId(patientId)
                : Optional.empty();

        if (!patientOpt.isPresent() && userId != null && !userId.isEmpty()) {
            Optional<User> userOpt = userRepository.findByUserStringId(userId);
            if (userOpt.isPresent()) {
                patientOpt = patientRepository.findFirstByUserAndRelation(userOpt.get(), "Self");
            }
        }

        if (!patientOpt.isPresent()) {
            response.put("status",  "failed");
            response.put("message", "Patient not found");
            return response;
        }

        Patient patient = patientOpt.get();
        Map<String, Object> details = new HashMap<>();
        details.put("patientId",            patient.getPatientStringId());
        details.put("userId",               patient.getUser() != null ? patient.getUser().getUserStringId() : "");
        details.put("patientName",          patient.getPatientName());
        details.put("patientRelation",      patient.getRelation());
        details.put("patientPhone",         patient.getPhoneNumber());
        details.put("patientDateOfBirth",   patient.getPatientDateOfBirth());
        details.put("patientGender",        patient.getPatientGender());
        details.put("patientAge",           patient.getPatientAge());
        details.put("isPregnant",           patient.getIsPregnant());
        details.put("lmpDate",              patient.getLmpDate());
        details.put("expectedDeliveryDate", patient.getExpectedDeliveryDate());
        details.put("patientStatus",        patient.getPatientStatus());

        Map<String, Object> result = new HashMap<>();
        result.put("patientDetails", details);

        response.put("status",  "success");
        response.put("message", "Patient details retrieved");
        response.put("result",  result);
        return response;
    }

    // ── ADD PATIENT USER (family member) ─────────────────────────────────────
    @Transactional
    public Map<String, Object> addPatientUser(String userId, String patientName, String relation,
                                              String phoneNo, String dob, String gender,
                                              String age, String isPregnant,
                                              String lmpDate, String expectedDeliveryDate) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findByUserStringId(userId);
        if (!userOpt.isPresent()) {
            response.put("status",  "failed");
            response.put("message", "User not found");
            return response;
        }

        User user = userOpt.get();
        String newPatientStringId = idGenerator.generateUniqueId();

        Patient patient = Patient.builder()
                .user(user)
                .patientStringId(newPatientStringId)
                .patientName(patientName)
                .relation(relation)
                .phoneNumber(phoneNo)
                .patientGender(gender)
                .patientDateOfBirth(dob)
                .patientAge(age != null ? age : "")
                .patientStatus("1")
                .isPregnant(isPregnant != null ? isPregnant : "0")
                .lmpDate(lmpDate)
                .expectedDeliveryDate(expectedDeliveryDate)
                .isActive(true)
                .createdAt(LocalDateTime.now())
                .build();

        patientRepository.save(patient);

        Map<String, Object> result = new HashMap<>();
        result.put("patientId",            newPatientStringId);
        result.put("patientName",          patientName);
        result.put("patientRelation",      relation);
        result.put("patientPhone",         phoneNo);
        result.put("patientDateOfBirth",   dob);
        result.put("patientGender",        gender);
        result.put("patientAge",           age);
        result.put("isPregnant",           isPregnant);
        result.put("lmpDate",              lmpDate);
        result.put("expectedDeliveryDate", expectedDeliveryDate);

        response.put("status",  "success");
        response.put("message", "Patient added successfully");
        response.put("result",  result);
        return response;
    }

    // ── GET PATIENTS LIST ────────────────────────────────────────────────────
    public Map<String, Object> getPatientsList(String userId) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findByUserStringId(userId);
        if (!userOpt.isPresent()) {
            response.put("status",  "failed");
            response.put("message", "User not found");
            return response;
        }

        List<Patient> patients = patientRepository.findByUserAndIsActiveTrue(userOpt.get());
        List<Map<String, Object>> list = new ArrayList<>();
        for (Patient p : patients) {
            Map<String, Object> pMap = new HashMap<>();
            pMap.put("patientId",            p.getPatientStringId());
            pMap.put("patientName",          p.getPatientName());
            pMap.put("patientRelation",      p.getRelation());
            pMap.put("patientPhone",         p.getPhoneNumber());
            pMap.put("patientDateOfBirth",   p.getPatientDateOfBirth());
            pMap.put("patientGender",        p.getPatientGender());
            pMap.put("isPregnant",           p.getIsPregnant() != null ? p.getIsPregnant() : "0");
            pMap.put("lmpDate",              p.getLmpDate() != null ? p.getLmpDate() : "");
            pMap.put("expectedDeliveryDate", p.getExpectedDeliveryDate() != null ? p.getExpectedDeliveryDate() : "");
            pMap.put("documents",            new ArrayList<>());
            list.add(pMap);
        }

       Map<String, Object> result = new HashMap<>();
         result.put("patientsCount", list.size());
         result.put("list",          list);

         response.put("status",  "success");
         response.put("message", "Patients list retrieved");
         response.put("result",  result);
        return response;
    }

    // ── GET PATIENTS LIST WITH DOCUMENTS ────────────────────────────────────────
public Map<String, Object> getPatientsListWithDocuments(String userId) {
    Map<String, Object> response = new HashMap<>();

    Optional<User> userOpt = userRepository.findByUserStringId(userId);
    if (!userOpt.isPresent()) {
        response.put("status",  "failed");
        response.put("message", "User not found");
        return response;
    }

    List<Patient> patients = patientRepository.findByUserAndIsActiveTrue(userOpt.get());
    List<Map<String, Object>> list = new ArrayList<>();

    for (Patient p : patients) {
        // Fetch real documents for this patient
        List<PatientDocument> docs = patientDocumentRepository.findByPatientId(p.getPatientStringId());
        List<Map<String, Object>> docList = new ArrayList<>();
        for (PatientDocument doc : docs) {
            Map<String, Object> d = new LinkedHashMap<>();
            d.put("id",          String.valueOf(doc.getId()));
            d.put("docType",     doc.getDocType() != null ? doc.getDocType() : "Report");
            d.put("fileName",    doc.getFileName());
            d.put("uploaded_at", doc.getUploadedAt() != null
                    ? doc.getUploadedAt().toLocalDate().toString() : "");
            d.put("url",         baseUrl + "/assets/patientDocuments/" + doc.getFileName());
            docList.add(d);
        }

        Map<String, Object> pMap = new LinkedHashMap<>();
        pMap.put("patientId",            p.getPatientStringId());
        pMap.put("patientName",          p.getPatientName());
        pMap.put("patientRelation",      p.getRelation());
        pMap.put("patientPhone",         p.getPhoneNumber());
        pMap.put("patientDateOfBirth",   p.getPatientDateOfBirth());
        pMap.put("patientGender",        p.getPatientGender());
        pMap.put("isPregnant",           p.getIsPregnant() != null ? p.getIsPregnant() : "0");
        pMap.put("lmpDate",              p.getLmpDate() != null ? p.getLmpDate() : "");
        pMap.put("expectedDeliveryDate", p.getExpectedDeliveryDate() != null ? p.getExpectedDeliveryDate() : "");
        pMap.put("documents",            docList);  // ← REAL docs here
        list.add(pMap);
    }

    Map<String, Object> result = new HashMap<>();
    result.put("patientsCount", list.size());
    result.put("list",          list);

    response.put("status",  "success");
    response.put("message", "Patients list retrieved");
    response.put("result",  result);
    return response;
}

    // ── APPOINTMENT HISTORY ───────────────────────────────────────────────────
    public Map<String, Object> getAppointmentHistory(String userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> userOpt = userRepository.findByUserStringId(userId);
            if (!userOpt.isPresent()) {
                response.put("status",  "failed");
                response.put("message", "User not found");
                return response;
            }

            List<Patient> patients = patientRepository.findByUserAndIsActiveTrue(userOpt.get());
            List<Map<String, Object>> appointments = new ArrayList<>();

            for (Patient patient : patients) {
                List<Appointment> apptList = appointmentRepository.findByPatient(patient);
                for (Appointment a : apptList) {
                    String doctorName = "";
                    String doctorSpecialisation = "";
                    String doctorProfileImage = "";

                    if (a.getDoctor() != null) {
                        doctorName = a.getDoctor().getDoctorName() != null ? a.getDoctor().getDoctorName() : "";
                        if (a.getDoctor().getProfileImage() != null && !a.getDoctor().getProfileImage().isEmpty()) {
                            doctorProfileImage = baseUrl + "/assets/doctorProfileImage/" + a.getDoctor().getProfileImage();
                        }
                        if (a.getDoctor().getSpecialization() != null && !a.getDoctor().getSpecialization().isEmpty()) {
                            String[] specArr = a.getDoctor().getSpecialization().split(",");
                            StringBuilder sb = new StringBuilder();
                            for (String specId : specArr) {
                                Optional<DoctorSpecialisation> spec = doctorSpecialisationRepository.findBySpecialisationId(specId.trim());
                                spec.ifPresent(s -> sb.append(s.getSpecialisationName()).append(","));
                            }
                            if (sb.length() > 0) doctorSpecialisation = sb.substring(0, sb.length() - 1);
                        }
                    }

                    Map<String, Object> appt = new HashMap<>();
                    appt.put("appointmentId",        a.getAppointmentStringId());
                    appt.put("appointmentNo",        a.getAppointmentStringId());
                    appt.put("todayTokenNo",         a.getTodayTokenNo());
                    appt.put("appointmentDate",      a.getAppointmentDate());
                    appt.put("appointmentStartTime", a.getAppointmentStartTime());
                    appt.put("appointmentEndTime",   a.getAppointmentEndTime());
                   String apptStatus = switch (a.getAppointmentStatus() != null ? a.getAppointmentStatus() : "1") {
                                  case "2" -> "COMPLETED";
                                  case "3" -> "CANCELLED";
                                  case "4" -> "DELAYED";
                                  default  -> "PENDING";
                                       };
                    appt.put("appointmentStatus", apptStatus);
                    appt.put("patientId",            patient.getPatientStringId());
                    appt.put("patientName",          patient.getPatientName() != null ? patient.getPatientName() : "");
                    appt.put("patientRelation",      patient.getRelation() != null ? patient.getRelation() : "");
                    appt.put("doctorId",             a.getDoctor() != null ? a.getDoctor().getDoctorStringId() : "");
                    appt.put("doctorName",           doctorName);
                    appt.put("doctorSpecialisation", doctorSpecialisation);
                    appt.put("doctorProfileImage",   doctorProfileImage);
                    appointments.add(appt);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("totalAppointments", appointments.size());
            result.put("appointments",      appointments);

            response.put("status",  "success");
            response.put("message", "Appointment history");
            response.put("result",  result);
        } catch (Exception e) {
            log.error("appointmentHistory error: {}", e.getMessage(), e);
            response.put("status",  "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // ── NOTIFICATION LIST ─────────────────────────────────────────────────────
    public Map<String, Object> getNotificationList(String userId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Map<String, Object>> list = new ArrayList<>();

            Optional<User> userOpt = userRepository.findByUserStringId(userId);
            if (userOpt.isPresent()) {
                List<Patient> patients = patientRepository.findByUserAndIsActiveTrue(userOpt.get());
                for (Patient patient : patients) {
                    List<Appointment> appts = appointmentRepository.findByPatient(patient);
                    for (Appointment a : appts) {
                        Map<String, Object> item = new LinkedHashMap<>();
                        item.put("notification_id",      a.getAppointmentStringId());
                        item.put("notification_title",   getNotificationTitle(a.getAppointmentStatus()));
                        item.put("notification_message", buildNotificationMessage(a, patient));
                        item.put("read_status",          "0");
                        item.put("navigation_id",        a.getAppointmentStringId());
                        list.add(item);
                    }
                }
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("notificationsCount", list.size());
            result.put("list",               list);

            response.put("status",  "success");
            response.put("message", "Notifications");
            response.put("result",  result);
        } catch (Exception e) {
            log.error("notificationList error: {}", e.getMessage(), e);
            response.put("status",  "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    private String getNotificationTitle(String status) {
        if (status == null) return "Appointment Booked";
        return switch (status) {
            case "2" -> "Appointment Completed";
            case "3" -> "Appointment Cancelled";
            case "4" -> "Appointment Delayed";
            default  -> "Appointment Booked";
        };
    }

    private String buildNotificationMessage(Appointment a, Patient patient) {
        String doctorName = (a.getDoctor() != null && a.getDoctor().getDoctorName() != null)
                ? a.getDoctor().getDoctorName() : "Doctor";
        return "Appointment with " + doctorName + " on " + a.getAppointmentDate()
                + " at " + a.getAppointmentStartTime() + " for " + patient.getPatientName();
    }

    // ── UPDATE APPOINTMENT STATUS ─────────────────────────────────────────────
    @Transactional
    public Map<String, Object> updateAppointmentStatus(String appointmentId, String status) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Appointment> apptOpt = appointmentRepository.findByAppointmentStringId(appointmentId);
            if (!apptOpt.isPresent()) {
                response.put("status",  "failed");
                response.put("message", "Appointment not found");
                return response;
            }
           Appointment appt = apptOpt.get();
            appt.setAppointmentStatus(status);
            appointmentRepository.save(appt);
            response.put("status",  "success");
            response.put("message", "Appointment status updated");
            response.put("msgId",   "");
            response.put("result",  new ArrayList<>());
        } catch (Exception e) {
            log.error("updateAppointmentStatus error: {}", e.getMessage(), e);
            response.put("status",  "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // ── DELETE ACCOUNT PERMANENTLY ───────────────────────────────────────────
    @Transactional
    public Map<String, Object> deleteAccountPermanently(String patientId) {
        Map<String, Object> response = new HashMap<>();

        Optional<Patient> patientOpt = (patientId != null && !patientId.isEmpty())
                ? patientRepository.findByPatientStringId(patientId)
                : Optional.empty();

        if (!patientOpt.isPresent()) {
            response.put("status",  "failed");
            response.put("message", "Patient not found");
            return response;
        }

        patientRepository.deleteById(patientOpt.get().getPatientId());
        response.put("status",  "success");
        response.put("message", "Patient data deleted successfully");
        return response;
    }

    // ── GET DOCTOR LIST ──────────────────────────────────────────────────────
    public Map<String, Object> getDoctorList() {
        Map<String, Object> response = new HashMap<>();

        List<Doctor> activeDoctors = doctorRepository.findByDoctorStatus("1");
        if (activeDoctors.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "Doctor not exist, please contact admin!!!");
            return response;
        }

        List<Map<String, Object>> doctors = new ArrayList<>();
        for (Doctor doctor : activeDoctors) {
            String doctorProfileImgUrl    = "";
            String specialisationNameText = "";

            if (doctor.getSpecialization() != null && !doctor.getSpecialization().isEmpty()) {
                String[] specArr = doctor.getSpecialization().split(",");
                StringBuilder sb = new StringBuilder();
                for (String specId : specArr) {
                    Optional<DoctorSpecialisation> spec = doctorSpecialisationRepository.findBySpecialisationId(specId.trim());
                    spec.ifPresent(s -> sb.append(s.getSpecialisationName()).append(","));
                }
                if (sb.length() > 0) specialisationNameText = sb.substring(0, sb.length() - 1);
            }
            if (doctor.getProfileImage() != null && !doctor.getProfileImage().isEmpty()) {
                doctorProfileImgUrl = baseUrl + "/assets/doctorProfileImage/" + doctor.getProfileImage();
            }

            Map<String, Object> doc = new HashMap<>();
            doc.put("doctorId",             doctor.getDoctorStringId());
            doc.put("tokenId",              doctor.getTokenId());
            doc.put("doctorName",           doctor.getDoctorName());
            doc.put("doctorPhone",          doctor.getPhoneNumber());
            doc.put("doctorProfileImg",     doctorProfileImgUrl);
            doc.put("doctorSpecialisation", specialisationNameText);
            doc.put("doctorShortDesc",      doctor.getShortDescription());
            doctors.add(doc);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("doctorCount", doctors.size());
        res.put("list", doctors);

        response.put("status",  "success");
        response.put("message", "Doctors List");
        response.put("result",  res);
        return response;
    }

    // ── GET DOCTOR SPECIALISATION LIST ───────────────────────────────────────
    public Map<String, Object> getDoctorSpecialisationList() {
        Map<String, Object> response = new HashMap<>();

        List<DoctorSpecialisation> specList = doctorSpecialisationRepository.findBySpecialisationStatus("1");
        if (specList.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "Doctor not exist, please contact admin!!!");
            return response;
        }

        List<Map<String, Object>> specialisations = new ArrayList<>();
        for (DoctorSpecialisation row : specList) {
            Map<String, Object> docSpec = new HashMap<>();
            docSpec.put("specialisationId",   row.getSpecialisationId());
            docSpec.put("specialisationName", row.getSpecialisationName());
            specialisations.add(docSpec);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("specialisationCount", specialisations.size());
        res.put("list", specialisations);

        response.put("status",  "success");
        response.put("message", "Doctors List");
        response.put("result",  res);
        return response;
    }

    // ── GET DOCTOR DETAILS BY ID ─────────────────────────────────────────────
    public Map<String, Object> getDoctorDetailsById(String doctorId) {
        Map<String, Object> response = new HashMap<>();

        Optional<Doctor> doctorOpt = doctorRepository.findByDoctorStringId(doctorId);
        if (doctorOpt.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "Doctor not exist, please contact admin!!!");
            return response;
        }

        Doctor doctor = doctorOpt.get();
        String doctorProfileImgUrl    = "";
        String specialisationNameText = "";

        if (doctor.getSpecialization() != null && !doctor.getSpecialization().isEmpty()) {
            String[] specArr = doctor.getSpecialization().split(",");
            StringBuilder sb = new StringBuilder();
            for (String specId : specArr) {
                Optional<DoctorSpecialisation> spec = doctorSpecialisationRepository.findBySpecialisationId(specId.trim());
                spec.ifPresent(s -> sb.append(s.getSpecialisationName()).append(","));
            }
            if (sb.length() > 0) specialisationNameText = sb.substring(0, sb.length() - 1);
        }
        if (doctor.getProfileImage() != null && !doctor.getProfileImage().isEmpty()) {
            doctorProfileImgUrl = baseUrl + "/assets/doctorProfileImage/" + doctor.getProfileImage();
        }

        Map<String, Object> pers = new HashMap<>();
        pers.put("doctorId",             doctor.getDoctorStringId());
        pers.put("tokenId",              doctor.getTokenId());
        pers.put("doctorName",           doctor.getDoctorName());
        pers.put("doctorPhone",          doctor.getPhoneNumber());
        pers.put("doctorProfileImg",     doctorProfileImgUrl);
        pers.put("doctorSpecialisation", specialisationNameText);
        pers.put("doctorShortDesc",      doctor.getShortDescription());

        Map<String, Object> res = new HashMap<>();
        res.put("doctorDetails", pers);

        response.put("status",  "success");
        response.put("message", "Doctor Details");
        response.put("result",  res);
        return response;
    }

    // ── DOCTOR AVAILABLE SLOT TIME ────────────────────────────────────────────
    public Map<String, Object> doctorAvailableSlotTime(String doctorId, String date) {
        Map<String, Object> response = new HashMap<>();

        List<DoctorAvailableSlotTime> slotTimes = doctorAvailableSlotTimeRepository.findByDoctorId(doctorId);
        if (slotTimes.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "Doctor slots not exist, please contact admin!!!");
            return response;
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        LocalDate today   = LocalDate.now(ZoneId.of("Asia/Kolkata"));
        LocalTime timeNow = LocalTime.now(ZoneId.of("Asia/Kolkata")).plusMinutes(30);
        boolean isToday   = date.equals(today.toString());

        List<Map<String, Object>> docSlots = new ArrayList<>();

        for (DoctorAvailableSlotTime slotRow : slotTimes) {
            LocalTime start = slotRow.getStartTime();
            LocalTime end   = slotRow.getEndTime();
            if (start == null || end == null) continue;

            LocalTime current = start;
            while (current.isBefore(end)) {
                LocalTime next = current.plusMinutes(15);
                if (next.isAfter(end)) break;

                String slotStart = current.format(formatter);
                String slotEnd   = next.format(formatter);

                if (isToday && !current.isAfter(timeNow)) {
                    current = next;
                    continue;
                }

                List<?> existing = appointmentRepository
                    .findByDoctorDoctorStringIdAndAppointmentDateAndAppointmentStartTime(doctorId, date, slotStart);
                if (!existing.isEmpty()) {
                    current = next;
                    continue;
                }

                String doctorName             = "";
                String specialisationNameText = "";
                Optional<Doctor> doctorOpt    = doctorRepository.findByDoctorStringId(doctorId);
                if (doctorOpt.isPresent()) {
                    Doctor doctor = doctorOpt.get();
                    doctorName = doctor.getDoctorName();
                    if (doctor.getSpecialization() != null && !doctor.getSpecialization().isEmpty()) {
                        String[] specArr = doctor.getSpecialization().split(",");
                        StringBuilder sb = new StringBuilder();
                        for (String specId : specArr) {
                            Optional<DoctorSpecialisation> spec = doctorSpecialisationRepository.findBySpecialisationId(specId.trim());
                            spec.ifPresent(s -> sb.append(s.getSpecialisationName()).append(","));
                        }
                        if (sb.length() > 0) specialisationNameText = sb.substring(0, sb.length() - 1);
                    }
                }

                Map<String, Object> docSlot = new HashMap<>();
                docSlot.put("doctorId",             doctorId);
                docSlot.put("doctorName",           doctorName);
                docSlot.put("doctorSpecailization", specialisationNameText);
                docSlot.put("date",                 date);
                docSlot.put("startTime",            slotStart);
                docSlot.put("endTime",              slotEnd);
                docSlots.add(docSlot);
                current = next;
            }
        }

        Map<String, Object> res = new HashMap<>();
        res.put("slotsCount", docSlots.size());
        res.put("list", docSlots);

        response.put("status",  "success");
        response.put("message", "Appointment slots");
        response.put("result",  res);
        return response;
    }

    // ── ADD APPOINTMENT ───────────────────────────────────────────────────────
    @Transactional
    public Map<String, Object> addAppointment(String userId, String doctorId, String patientId,
                                               String date, String startTime, String endTime,
                                               String scanType) {
        Map<String, Object> response = new HashMap<>();

        if (userId == null || userId.isEmpty() || patientId == null || patientId.isEmpty() ||
            doctorId == null || doctorId.isEmpty() || date == null || date.isEmpty() ||
            startTime == null || startTime.isEmpty() || endTime == null || endTime.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "All fields are required!");
            return response;
        }

        LocalTime start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("HH:mm"));
        LocalTime end   = LocalTime.parse(endTime,   DateTimeFormatter.ofPattern("HH:mm"));
        if (!end.isAfter(start)) {
            response.put("status",  "failed");
            response.put("message", "End time must be greater than start time!");
            return response;
        }

        Optional<User> userOpt = userRepository.findByUserStringId(userId);
        if (userOpt.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "Invalid userId — please login again!");
            return response;
        }

        Optional<Patient> patientOpt = patientRepository.findByPatientStringId(patientId);
        if (patientOpt.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "Invalid patient — please select again!");
            return response;
        }

        List<Appointment> overlapping = appointmentRepository
            .findByDoctorDoctorStringIdAndAppointmentDateAndAppointmentStartTimeLessThanAndAppointmentEndTimeGreaterThan(
                doctorId, date, endTime, startTime);
        if (!overlapping.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "Doctor is already booked for this time slot!");
            return response;
        }

        long todayTokenNumber = appointmentRepository.countByAppointmentDate(date) + 1;

        Optional<Doctor> doctorOpt = doctorRepository.findByDoctorStringId(doctorId);
        if (doctorOpt.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "Doctor not found!");
            return response;
        }

        String appointmentId = idGenerator.generateUniqueId();
        Appointment appointment = Appointment.builder()
            .appointmentStringId(appointmentId)
            .todayTokenNo(String.valueOf(todayTokenNumber))
            .doctor(doctorOpt.get())
            .patient(patientOpt.get())
            .userId(userId)
            .appointmentDate(date)
            .appointmentStartTime(startTime)
            .appointmentEndTime(endTime)
            .appointmentStatus("1")
            .scanType(scanType != null ? scanType : "")
            .build();
        appointmentRepository.save(appointment);

        Map<String, Object> res = new HashMap<>();
        res.put("appointmentId",        appointmentId);
        res.put("appointmentDate",      date);
        res.put("appointmentStartTime", startTime);
        res.put("appointmentEndTime",   endTime);
        res.put("doctorId",             doctorId);
        res.put("userId",               userId);
        res.put("patientId",            patientId);
        res.put("scanType",             scanType);
        res.put("todayTokenNo",         todayTokenNumber);

        response.put("status",  "success");
        response.put("message", "Appointment booked successfully");
        response.put("result",  res);
        return response;
    }

    // ── CHAT PATIENT SEND MESSAGE ─────────────────────────────────────────────
    // FIX: store doctorId as receiverId so chat can be filtered per doctor
    public Map<String, Object> chatPatientSendMessage(String userId, String patientId,
                                                       String message, String doctorId,
                                                       String doctorName) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findByUserStringId(userId);
        if (userOpt.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "User not found!");
            return response;
        }
Optional<Patient> patientOpt = (patientId != null && !patientId.isEmpty())
        ? patientRepository.findByPatientStringId(patientId)
        : Optional.empty();

if (!patientOpt.isPresent() && userId != null && !userId.isEmpty()) {
    Optional<User> userOpt2 = userRepository.findByUserStringId(userId);
    if (userOpt2.isPresent()) {
        patientOpt = patientRepository.findFirstByUserAndRelation(userOpt2.get(), "Self");
    }
}

if (!patientOpt.isPresent()) {
    response.put("status",  "failed");
    response.put("message", "Patient not found!");
    return response;
}

        String chatId = idGenerator.generateUniqueId();
        PatientDoctorChat chat = PatientDoctorChat.builder()
                .chatId(chatId)
                .userId(userId)
                .senderId(patientOpt.get().getPatientStringId())
                .senderType("PATIENT")
                .receiverId(doctorId != null && !doctorId.isEmpty() ? doctorId : "ADMIN") // FIX
                .receiverType("ADMINDOCTOR")
                .message(message)
                .messageStatus("1")
                .doctorId(doctorId)
                .doctorName(doctorName)
                .build();
        patientDoctorChatRepository.save(chat);

        List<Doctor> adminDoctors = doctorRepository.findByDoctorStatusAndIsAdminDoctor("1", "YES");
        String patientName = patientOpt.get().getPatientName();
        String notificationMessage = "New message from " + patientName;
        for (Doctor adminDoctor : adminDoctors) {
            if (adminDoctor.getUser() != null) {
                notificationService.sendPushNotification(
                    adminDoctor.getUser(), "New Chat Message", notificationMessage);
            }
        }

        response.put("status",  "success");
        response.put("message", "Chat saved successfully");
        response.put("result", new ArrayList<>());
        return response;
    }

    // ── GET CHAT PATIENT MESSAGE ──────────────────────────────────────────────
    // FIX: filter by doctorId so each doctor chat screen shows only its messages
    public Map<String, Object> getChatPatientMessage(String userId, String doctorId) {
        Map<String, Object> response = new HashMap<>();

        Optional<User> userOpt = userRepository.findByUserStringId(userId);
        if (userOpt.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "User not found!");
            return response;
        }

        List<Patient> patients = patientRepository.findByUserUserId(userOpt.get().getUserId());
        if (patients.isEmpty()) {
            Map<String, Object> res = new HashMap<>();
            res.put("chatCount", 0);
            res.put("list", new ArrayList<>());
            response.put("status",  "success");
            response.put("message", "No chats found");
            response.put("result",  res);
            return response;
        }

        List<String> patientIds = new ArrayList<>();
        for (Patient p : patients) {
            if (p.getPatientStringId() != null) patientIds.add(p.getPatientStringId());
        }

        List<PatientDoctorChat> allChats = patientDoctorChatRepository.findBySenderIdInOrReceiverIdIn(patientIds);

        // Filter by doctorId if provided — include both patient→doctor and doctor→patient messages
        List<PatientDoctorChat> chats = (doctorId != null && !doctorId.isEmpty())
                ? allChats.stream()
                        .filter(c -> doctorId.equals(c.getDoctorId())
                                  || doctorId.equals(c.getSenderId()))
                        .collect(Collectors.toList())
                : allChats;

        // Sort by createDatetime ascending so app date comparison works correctly
        chats.sort(Comparator.comparing(c -> c.getCreateDatetime() != null ? c.getCreateDatetime() : java.time.LocalDateTime.MIN));

        List<Map<String, Object>> chatList = new ArrayList<>();
        for (PatientDoctorChat chatRow : chats) {
            Map<String, Object> chatMap = new HashMap<>();
            chatMap.put("chatId",        chatRow.getChatId());
            chatMap.put("userId",        chatRow.getUserId());
            chatMap.put("senderId",      chatRow.getSenderId());
            chatMap.put("senderType",    chatRow.getSenderType());
            chatMap.put("receiverId",    chatRow.getReceiverId());
            chatMap.put("receiverType",  "ADMINDOCTOR".equals(chatRow.getReceiverType()) ? "ADMIN" : chatRow.getReceiverType());
            chatMap.put("message",       chatRow.getMessage());
            chatMap.put("messageStatus", chatRow.getMessageStatus());
            chatMap.put("doctorId",      chatRow.getDoctorId());
            chatMap.put("doctorName",    chatRow.getDoctorName());
            chatMap.put("createDatetime", chatRow.getCreateDatetime() != null
                    ? chatRow.getCreateDatetime().toString().replace("T", " ")
                    : "");
            chatList.add(chatMap);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("chatCount", chatList.size());
        res.put("list", chatList);

        response.put("status",  "success");
        response.put("message", "Chat list retrieved successfully");
        response.put("result",  res);
        return response;
    }

    // ── GET PATIENT QUEUE STATUS ──────────────────────────────────────────────
    public Map<String, Object> getPatientQueueStatus(String patientId, String doctorId) {
        Map<String, Object> response = new HashMap<>();

        String appointmentDate = LocalDate.now(ZoneId.of("Asia/Kolkata")).toString();

        Optional<Patient> patientOpt = patientRepository.findByPatientStringId(patientId);
        if (patientOpt.isEmpty() || !"1".equals(patientOpt.get().getPatientStatus())) {
            response.put("status",  "failed");
            response.put("message", "Patient not found!");
            return response;
        }

        Patient patient = patientOpt.get();
        String userId   = patient.getUser().getUserStringId();

        List<Appointment> appointments = appointmentRepository
                .findByDoctorDoctorStringIdAndAppointmentDateAndAppointmentStartTimeLessThanAndAppointmentEndTimeGreaterThan(
                        doctorId, appointmentDate, "23:59", "00:00");

        List<Appointment> patientAppointments = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getPatient().getPatientStringId().equals(patientId) &&
                a.getUserId().equals(userId)) {
                patientAppointments.add(a);
            }
        }

        if (patientAppointments.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "No appointment found for this patient today!");
            return response;
        }

        List<Map<String, Object>> finalList = new ArrayList<>();
        for (Appointment appointment : patientAppointments) {
            String apptDoctorId           = appointment.getDoctor().getDoctorStringId();
            String doctorName             = "N/A";
            String specialisationNameText = "";

            Optional<Doctor> doctorOpt = doctorRepository.findByDoctorStringId(apptDoctorId);
            if (doctorOpt.isPresent()) {
                Doctor doctor = doctorOpt.get();
                doctorName = doctor.getDoctorName();
                if (doctor.getSpecialization() != null && !doctor.getSpecialization().isEmpty()) {
                    String[] specArr = doctor.getSpecialization().split(",");
                    StringBuilder sb = new StringBuilder();
                    for (String specId : specArr) {
                        Optional<DoctorSpecialisation> spec = doctorSpecialisationRepository.findBySpecialisationId(specId.trim());
                        spec.ifPresent(s -> sb.append(s.getSpecialisationName()).append(", "));
                    }
                    if (sb.length() > 0) specialisationNameText = sb.substring(0, sb.length() - 2);
                }
            }

            List<Appointment> allAppointments = appointmentRepository
                    .findByDoctorStringIdAndDateOrdered(apptDoctorId, appointmentDate);

            int patientToken   = 0;
            int completedCount = 0;
            int position       = 0;

            for (int i = 0; i < allAppointments.size(); i++) {
                Appointment appt = allAppointments.get(i);
                int tokenNo = i + 1;
                if ("2".equals(appt.getAppointmentStatus())) completedCount++;
                if (appt.getAppointmentStringId().equals(appointment.getAppointmentStringId())) {
                    patientToken = tokenNo;
                    position     = tokenNo - completedCount;
                }
            }

            int waitingPatients = Math.max(0, position - 1);
            int estimatedWait   = waitingPatients * 15;

            String status;
            if      ("2".equals(appointment.getAppointmentStatus())) status = "COMPLETED";
            else if ("3".equals(appointment.getAppointmentStatus())) status = "CANCELLED";
            else status = waitingPatients == 0 ? "YOUR TURN" : "WAITING";

            Map<String, Object> entry = new HashMap<>();
            entry.put("doctorName",           doctorName);
            entry.put("specialisation",       specialisationNameText);
            entry.put("appointmentId",        appointment.getAppointmentStringId());
            entry.put("appointmentDate",      appointmentDate);
            entry.put("appointmentStartTime", appointment.getAppointmentStartTime());
            entry.put("appointmentEndTime",   appointment.getAppointmentEndTime());
            entry.put("patientName",          patient.getPatientName());
            entry.put("yourQueueNumber",      String.valueOf(patientToken));
            entry.put("estimatedWaitTim",     estimatedWait + " mins");
            entry.put("status",               status);
            finalList.add(entry);
        }

        Map<String, Object> res = new HashMap<>();
        res.put("totalCount", finalList.size());
        res.put("list", finalList);

        response.put("status",  "success");
        response.put("message", "Queue Status");
        response.put("result",  res);
        return response;
    }

    // ── GET LOCATION LIST ─────────────────────────────────────────────────────
    public Map<String, Object> getLocationList() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<com.miracle.model.Location> locations =
                    locationRepository.findByLocationStatusOrderByIdAsc("1");

            List<Map<String, Object>> list = new ArrayList<>();
            for (com.miracle.model.Location loc : locations) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("locationId",   loc.getLocationId());
                item.put("locationName", loc.getLocationName());
                list.add(item);
            }

            Map<String, Object> res = new LinkedHashMap<>();
            res.put("locationCount", list.size());
            res.put("list", list);

            response.put("status",  "success");
            response.put("message", "Location List");
            response.put("result",  res);
        } catch (Exception e) {
            log.error("getLocationList error: {}", e.getMessage(), e);
            response.put("status",  "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // ── GET DOCTORS BY LOCATION ───────────────────────────────────────────────
    public Map<String, Object> getDoctorsByLocation(String locationId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Doctor> doctors = doctorRepository.findByLocationIdAndDoctorStatus(locationId, "1");
            if (doctors.isEmpty()) {
                response.put("status",  "failed");
                response.put("message", "No doctors found for this location!");
                return response;
            }

            List<Map<String, Object>> doctorList = new ArrayList<>();
            for (Doctor doctor : doctors) {
                String doctorProfileImgUrl    = "";
                String specialisationNameText = "";

                if (doctor.getSpecialization() != null && !doctor.getSpecialization().isEmpty()) {
                    String[] specArr = doctor.getSpecialization().split(",");
                    StringBuilder sb = new StringBuilder();
                    for (String specId : specArr) {
                        Optional<DoctorSpecialisation> spec =
                                doctorSpecialisationRepository.findBySpecialisationId(specId.trim());
                        spec.ifPresent(s -> sb.append(s.getSpecialisationName()).append(","));
                    }
                    if (sb.length() > 0) specialisationNameText = sb.substring(0, sb.length() - 1);
                }
                if (doctor.getProfileImage() != null && !doctor.getProfileImage().isEmpty()) {
                    doctorProfileImgUrl = baseUrl + "/assets/doctorProfileImage/" + doctor.getProfileImage();
                }

                Map<String, Object> doc = new LinkedHashMap<>();
                doc.put("doctorId",             doctor.getDoctorStringId());
                doc.put("doctorName",           doctor.getDoctorName());
                doc.put("doctorPhone",          doctor.getPhoneNumber());
                doc.put("doctorProfileImg",     doctorProfileImgUrl);
                doc.put("doctorSpecialisation", specialisationNameText);
                doc.put("doctorShortDesc",      doctor.getShortDescription());
                doc.put("locationId",           locationId);
                doctorList.add(doc);
            }

            Map<String, Object> res = new LinkedHashMap<>();
            res.put("doctorCount", doctorList.size());
            res.put("list", doctorList);

            response.put("status",  "success");
            response.put("message", "Doctors List");
            response.put("result",  res);
        } catch (Exception e) {
            log.error("getDoctorsByLocation error: {}", e.getMessage(), e);
            response.put("status",  "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // ── CRUD ─────────────────────────────────────────────────────────────────
    @Transactional
    public Patient createPatient(PatientDto dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Patient patient = Patient.builder()
                .user(user)
                .patientStringId(idGenerator.generateUniqueId())
                .phoneNumber(dto.getPhoneNumber())
                .dateOfBirth(dto.getDateOfBirth())
                .gender(Patient.Gender.valueOf(dto.getGender().toUpperCase()))
                .address(dto.getAddress())
                .bloodGroup(dto.getBloodGroup())
                .emergencyContact(dto.getEmergencyContact())
                .medicalHistory(dto.getMedicalHistory())
                .allergies(dto.getAllergies())
                .insuranceProvider(dto.getInsuranceProvider())
                .insuranceNumber(dto.getInsuranceNumber())
                .isActive(true)
                .build();
        return patientRepository.save(patient);
    }

    public Patient getPatientById(Long patientId) {
        return patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));
    }

    public Patient getPatientByUserId(Long userId) {
        return patientRepository.findByUserUserId(userId)
                .stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Patient not found for user"));
    }

    @Transactional
    public Patient updatePatient(Long patientId, PatientDto dto) {
        Patient patient = getPatientById(patientId);
        patient.setPhoneNumber(dto.getPhoneNumber());
        patient.setDateOfBirth(dto.getDateOfBirth());
        patient.setGender(Patient.Gender.valueOf(dto.getGender().toUpperCase()));
        patient.setAddress(dto.getAddress());
        patient.setBloodGroup(dto.getBloodGroup());
        patient.setEmergencyContact(dto.getEmergencyContact());
        patient.setMedicalHistory(dto.getMedicalHistory());
        patient.setAllergies(dto.getAllergies());
        patient.setInsuranceProvider(dto.getInsuranceProvider());
        patient.setInsuranceNumber(dto.getInsuranceNumber());
        return patientRepository.save(patient);
    }

    @Transactional
    public void deletePatient(Long patientId) {
        Patient patient = getPatientById(patientId);
        patient.setIsActive(false);
        patientRepository.save(patient);
    }

    public PatientDto convertToDto(Patient patient) {
        return PatientDto.builder()
                .patientId(patient.getPatientId())
                .userId(patient.getUser() != null ? patient.getUser().getUserId() : null)
                .userName(patient.getUser() != null ? patient.getUser().getUserName() : null)
                .email(patient.getUser() != null ? patient.getUser().getEmail() : null)
                .phoneNumber(patient.getPhoneNumber())
                .dateOfBirth(patient.getDateOfBirth())
                .gender(patient.getGender() != null ? patient.getGender().toString() : null)
                .address(patient.getAddress())
                .bloodGroup(patient.getBloodGroup())
                .emergencyContact(patient.getEmergencyContact())
                .medicalHistory(patient.getMedicalHistory())
                .allergies(patient.getAllergies())
                .insuranceProvider(patient.getInsuranceProvider())
                .insuranceNumber(patient.getInsuranceNumber())
                .isActive(patient.getIsActive())
                .userImg(patient.getUser() != null ? patient.getUser().getUserImg() : null)
                .build();
    }

    // ── UTIL ─────────────────────────────────────────────────────────────────
    private String generateOTP(boolean developmentMode) {
        if (developmentMode) return "1234";
        return String.format("%04d", new Random().nextInt(10000));
    }

// ── GET PREGNANCY EVENTS ──────────────────────────────────────────────────
public Map<String, Object> getPregnancyEvents(String patientId) {
    Map<String, Object> response = new HashMap<>();

    Optional<Patient> patientOpt = (patientId != null && !patientId.isEmpty())
            ? patientRepository.findByPatientStringId(patientId)
            : Optional.empty();

    if (!patientOpt.isPresent()) {
        response.put("status",  "failed");
        response.put("message", "Patient not found");
        return response;
    }

    Patient patient = patientOpt.get();

    Map<String, Object> pregnancyData = new HashMap<>();
    pregnancyData.put("patientId",            patient.getPatientStringId());
    pregnancyData.put("patientName",          patient.getPatientName());
    pregnancyData.put("isPregnant",           patient.getIsPregnant());
    pregnancyData.put("lmpDate",              patient.getLmpDate());
    pregnancyData.put("expectedDeliveryDate", patient.getExpectedDeliveryDate());
    pregnancyData.put("patientGender",        patient.getPatientGender());

   // Build events list from uploaded patient documents
List<Map<String, Object>> list = new ArrayList<>();
List<PatientDocument> docs = patientDocumentRepository.findByPatientId(patient.getPatientStringId());
for (PatientDocument d : docs) {
    Map<String, Object> event = new LinkedHashMap<>();
    event.put("scanType",     d.getDocType() != null ? d.getDocType() : "Report");
    event.put("dueWeeks",     0);
    event.put("dueDate",      d.getUploadedAt() != null ? d.getUploadedAt().toLocalDate().toString() : "");
    event.put("status",       "COMPLETED");
    event.put("currentWeeks", 0);
    list.add(event);
}

Map<String, Object> result = new LinkedHashMap<>();
result.put("list", list);

response.put("status",  "success");
response.put("message", "Pregnancy events retrieved");
response.put("result",  result);
return response;
}
}