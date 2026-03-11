package com.miracle.service;

import com.miracle.dto.DoctorDto;
import com.miracle.model.*;
import com.miracle.repository.*;
import com.miracle.util.IdGenerator;
import com.miracle.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class DoctorService {

    private final DoctorRepository               doctorRepository;
    private final UserRepository                 userRepository;
    private final JwtUtil                        jwtUtil;
    private final AppointmentRepository          appointmentRepository;
    private final DoctorSpecialisationRepository doctorSpecialisationRepository;
    private final PatientDoctorChatRepository    chatRepository;
    private final PatientRepository              patientRepository;
    private final IdGenerator                    idGenerator;
    private final PatientDocumentRepository      patientDocumentRepository;
    private final WhatsappService whatsappService;


    @Value("${app.base-url:http://localhost:8080}")
    private String baseUrl;

    // =========================================================================
    // DOCTOR LOGIN
    // =========================================================================
    public Map<String, Object> doctorLogin(String mobileNo) {
        Map<String, Object> response = new HashMap<>();
        Optional<Doctor> doctorOpt = doctorRepository.findByPhoneNumber(mobileNo);

        if (!doctorOpt.isPresent()) {
            response.put("status",  "failed");
            response.put("message", "Doctor not found, please contact admin");
            return response;
        }

        Doctor doctor = doctorOpt.get();
        String otp = generateOTP(true);
        doctor.setOtp(otp);
        doctorRepository.save(doctor);
        String otpMsg = "Dr. " + doctor.getDoctorName() + ", Your OTP is " + otp + " Regards, Miracle Hospital.";
        whatsappService.sendMessage(mobileNo, otpMsg);

        Map<String, Object> result = new HashMap<>();
        result.put("doctorId",     doctor.getDoctorStringId());
        result.put("doctorStatus", "DOCTOR-EXIST");

        response.put("status",  "success");
        response.put("message", "OTP sent successfully");
        response.put("result",  result);
        return response;
    }

    // =========================================================================
    // VALIDATE OTP
    // =========================================================================
    @Transactional
    public Map<String, Object> validateDoctorOtp(String mobileNo, String otp, String fcmId) {
        Map<String, Object> response = new HashMap<>();
        Optional<Doctor> doctorOpt = doctorRepository.findByPhoneNumber(mobileNo);

        if (!doctorOpt.isPresent()) {
            response.put("status",  "failed");
            response.put("message", "Doctor not found");
            return response;
        }

        Doctor doctor = doctorOpt.get();

        if (!doctor.getIsActive()) {
            response.put("status",  "failed");
            response.put("message", "Account is deactivated, please contact admin");
            return response;
        }

        if (!doctor.getOtp().equals(otp)) {
            response.put("status",  "failed");
            response.put("message", "Invalid OTP");
            return response;
        }

        doctor.setTokenId(fcmId);
        doctorRepository.save(doctor);

        String profileStatus = (doctor.getDoctorName() != null && !doctor.getDoctorName().isEmpty())
                ? "COMPLETED" : "PENDING";

        String token = jwtUtil.generateToken("DOCTOR_" + doctor.getDoctorId(), "DOCTOR");

        String specialisationNameText = resolveSpecialisationNames(doctor.getSpecialization());

        Map<String, Object> result = new HashMap<>();
        result.put("doctorId",             doctor.getDoctorStringId());
        result.put("doctorName",           doctor.getDoctorName());
        result.put("mobileNo",             mobileNo);
        result.put("doctorSpecialisation", specialisationNameText);
        result.put("isAdminDoctor",        doctor.getIsAdminDoctor() != null ? doctor.getIsAdminDoctor() : "NO");
        result.put("profileStatus",        profileStatus);

        response.put("status",  "success");
        response.put("message", "OTP verified successfully");
        response.put("token",   token);
        response.put("result",  result);
        return response;
    }

    // =========================================================================
    // RESEND OTP
    // =========================================================================
    public Map<String, Object> resendOtp(String mobileNo) {
        Map<String, Object> response = new HashMap<>();
        Optional<Doctor> doctorOpt = doctorRepository.findByPhoneNumber(mobileNo);

        if (!doctorOpt.isPresent()) {
            response.put("status",  "failed");
            response.put("message", "Doctor not found");
            return response;
        }

        Doctor doctor = doctorOpt.get();
        String otp = generateOTP(true);
        doctor.setOtp(otp);
        doctorRepository.save(doctor);
        String otpMsg = "Dr. " + doctor.getDoctorName() + ", Your OTP is " + otp + " Regards, Miracle Hospital.";
        whatsappService.sendMessage(mobileNo, otpMsg);

        response.put("status",  "success");
        response.put("message", "OTP sent successfully");
        return response;
    }

    // =========================================================================
    // GET PROFILE
    // =========================================================================
    public Map<String, Object> getProfile(String doctorId) {
        Map<String, Object> response = new HashMap<>();

        Optional<Doctor> doctorOpt = doctorRepository.findByDoctorStringId(doctorId);
        if (!doctorOpt.isPresent()) {
            response.put("status",  "failed");
            response.put("message", "Doctor not found");
            return response;
        }

        Doctor doctor = doctorOpt.get();
        String specialisationNameText = resolveSpecialisationNames(doctor.getSpecialization());

        Map<String, Object> doctorDetails = new HashMap<>();
        doctorDetails.put("doctorId",             doctor.getDoctorStringId());
        doctorDetails.put("tokenId",              doctor.getTokenId());
        doctorDetails.put("doctorName",           doctor.getDoctorName());
        doctorDetails.put("doctorPhone",          doctor.getPhoneNumber());
        doctorDetails.put("doctorProfileImg",
                (doctor.getProfileImage() != null && !doctor.getProfileImage().isEmpty())
                        ? baseUrl + "/assets/doctorProfileImage/" + doctor.getProfileImage() : "");
        doctorDetails.put("doctorSpecialisation", specialisationNameText);
        doctorDetails.put("doctorShortDesc",      doctor.getShortDescription());
        doctorDetails.put("isAdminDoctor",        doctor.getIsAdminDoctor() != null ? doctor.getIsAdminDoctor() : "NO");

        Map<String, Object> result = new HashMap<>();
        result.put("doctorDetails", doctorDetails);

        response.put("status",  "success");
        response.put("message", "Doctor details retrieved");
        response.put("result",  result);
        return response;
    }

    // =========================================================================
    // DELETE ACCOUNT
    // =========================================================================
    @Transactional
    public Map<String, Object> deleteAccountPermanently(String doctorId) {
        Map<String, Object> response = new HashMap<>();

        Optional<Doctor> doctorOpt = doctorRepository.findByDoctorStringId(doctorId);
        if (!doctorOpt.isPresent()) {
            response.put("status",  "failed");
            response.put("message", "Doctor not found");
            return response;
        }

        doctorRepository.deleteById(doctorOpt.get().getDoctorId());
        response.put("status",  "success");
        response.put("message", "Doctor data deleted successfully");
        return response;
    }

    // =========================================================================
    // APPOINTMENT HISTORY
    // =========================================================================
    public Map<String, Object> getAppointmentHistory(String doctorId, String date) {
        String queryDate = (date != null && !date.isEmpty())
                ? date
                : LocalDate.now().toString();

        List<Appointment> list = appointmentRepository.findByDoctorStringIdAndDateOrdered(doctorId, queryDate);

        List<Map<String, Object>> apptList = new ArrayList<>();
        for (Appointment a : list) {
            Map<String, Object> appt = new HashMap<>();
            appt.put("appointmentId",        a.getAppointmentStringId());
            appt.put("appointmentNo",        a.getAppointmentStringId());
            appt.put("appointmentDate",      a.getAppointmentDate());
            appt.put("appointmentStartTime", a.getAppointmentStartTime());
            appt.put("appointmentEndTime",   a.getAppointmentEndTime());
            String statusText = switch (a.getAppointmentStatus() != null ? a.getAppointmentStatus() : "1") {
                case "2" -> "COMPLETED";
                case "3" -> "CANCELLED";
                default  -> "PENDING";
            };
            appt.put("appointmentStatus", statusText);
            appt.put("appointmentFor",    "");
            appt.put("patientName",  a.getPatient() != null ? a.getPatient().getPatientName() : "");
            appt.put("patientPhone", a.getPatient() != null ? a.getPatient().getPhoneNumber()  : "");
            apptList.add(appt);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("appointmentCount", apptList.size());
        result.put("slotTime",         "15");
        result.put("list",             apptList);

        Map<String, Object> response = new HashMap<>();
        response.put("status",  "success");
        response.put("message", "Appointments list");
        response.put("result",  result);
        return response;
    }

    // =========================================================================
    // GET APPOINTMENT DETAILS BY ID
    // =========================================================================
    public Map<String, Object> getAppointmentDetailsById(String appointmentId, String doctorId) {
        Map<String, Object> response = new HashMap<>();

        Optional<Appointment> apptOpt = appointmentRepository.findByAppointmentStringId(appointmentId);
        if (apptOpt.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "Appointment not found");
            return response;
        }

        Appointment a = apptOpt.get();
        String statusText = switch (a.getAppointmentStatus() != null ? a.getAppointmentStatus() : "1") {
            case "2" -> "COMPLETED";
            case "3" -> "CANCELLED";
            default  -> "PENDING";
        };

        Map<String, Object> result = new HashMap<>();
        result.put("appointmentId",        a.getAppointmentStringId());
        result.put("appointmentNo",        a.getAppointmentStringId());
        result.put("appointmentDate",      a.getAppointmentDate());
        result.put("appointmentStartTime", a.getAppointmentStartTime());
        result.put("appointmentEndTime",   a.getAppointmentEndTime());
        result.put("appointmentStatus",    statusText);
        result.put("doctorId",             doctorId);
        result.put("patientId",    a.getPatient() != null ? a.getPatient().getPatientStringId() : "");
        result.put("patientName",  a.getPatient() != null ? a.getPatient().getPatientName()     : "");
        result.put("patientPhone", a.getPatient() != null ? a.getPatient().getPhoneNumber()     : "");

        response.put("status",  "success");
        response.put("message", "Appointment Details");
        response.put("result",  result);
        return response;
    }

    // =========================================================================
    // UPDATE APPOINTMENT STATUS BY ID
    // =========================================================================
    public Map<String, Object> updateAppointmentStatusById(String appointmentId, String status) {
        Map<String, Object> response = new HashMap<>();

        Optional<Appointment> apptOpt = appointmentRepository.findByAppointmentStringId(appointmentId);
        if (apptOpt.isEmpty()) {
            response.put("status",  "failed");
            response.put("message", "Appointment not found");
            return response;
        }

        Appointment appt = apptOpt.get();
        appt.setAppointmentStatus(status);
        appointmentRepository.save(appt);

        response.put("status",  "success");
        response.put("message", "Appointment status updated successfully");
        response.put("msgId",   "");
        response.put("result",  new ArrayList<>());
        return response;
    }

    // =========================================================================
    // GET NOTIFICATION LIST
    // =========================================================================
    public Map<String, Object> getNotificationList(String doctorId) {
        List<Map<String, Object>> list = new ArrayList<>();
        try {
            List<Appointment> appts = appointmentRepository.findByDoctorDoctorStringId(doctorId);
            for (Appointment a : appts) {
                Map<String, Object> item = new LinkedHashMap<>();
                String patientName = a.getPatient() != null ? a.getPatient().getPatientName() : "Patient";
                item.put("notification_id",      a.getAppointmentStringId());
                item.put("notification_title",   getDoctorNotificationTitle(a.getAppointmentStatus()));
                item.put("notification_message", "Appointment with " + patientName
                        + " on " + a.getAppointmentDate()
                        + " at " + a.getAppointmentStartTime());
                item.put("read_status",          "0");
                item.put("navigation_id",        a.getAppointmentStringId());
                list.add(item);
            }
        } catch (Exception e) {
            log.error("getNotificationList error: {}", e.getMessage(), e);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("notificationsCount", list.size());
        result.put("list",               list);

        Map<String, Object> response = new HashMap<>();
        response.put("status",  "success");
        response.put("message", "Notification List");
        response.put("result",  result);
        return response;
    }

    // =========================================================================
    // CHAT: SEND MESSAGE
    // =========================================================================
    public Map<String, Object> chatDoctorSendMessage(String doctorId, String doctorName,
                                                      String userId, String patientId,
                                                      String message) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Patient> patientOpt = patientRepository.findByPatientStringId(patientId);
            if (patientOpt.isEmpty()) {
                response.put("status",  "failed");
                response.put("message", "Patient not found!");
                return response;
            }

            String resolvedUserId = patientOpt.get().getUser() != null
                    ? patientOpt.get().getUser().getUserStringId() : "";

            String chatId = idGenerator.generateUniqueId();
            PatientDoctorChat chat = PatientDoctorChat.builder()
                    .chatId(chatId)
                    .userId(resolvedUserId)
                    .senderId(doctorId)
                    .senderType("ADMINDOCTOR")
                    .receiverId(patientId)
                    .receiverType("PATIENT")
                    .message(message)
                    .messageStatus("1")
                    .doctorId(doctorId)
                    .doctorName(doctorName != null ? doctorName : "")
                    .build();
            chatRepository.save(chat);

            response.put("status",  "success");
            response.put("message", "Chat saved successfully");
            response.put("result",  new ArrayList<>());
        } catch (Exception e) {
            log.error("chatDoctorSendMessage error: {}", e.getMessage(), e);
            response.put("status",  "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // =========================================================================
    // CHAT: GET MESSAGES
    // =========================================================================
    public Map<String, Object> getChatDoctorMessage(String patientId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Patient> patientOpt = patientRepository.findByPatientStringId(patientId);
            if (patientOpt.isEmpty()) {
                response.put("status",  "failed");
                response.put("message", "Patient not found!");
                return response;
            }

            String userId = patientOpt.get().getUser().getUserStringId();
            List<PatientDoctorChat> chats = chatRepository.findByUserIdOrderByCreateDatetimeAsc(userId);

            if (chats == null || chats.isEmpty()) {
                Map<String, Object> res = new LinkedHashMap<>();
                res.put("chatCount", 0);
                res.put("list",      new ArrayList<>());
                response.put("status",  "success");
                response.put("message", "Chat list");
                response.put("result",  res);
                return response;
            }

            List<Map<String, Object>> list = chats.stream().map(c -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("chatId",         c.getChatId());
                m.put("userId",         c.getUserId());
                m.put("senderId",       c.getSenderId());
                m.put("senderType",     c.getSenderType());
                m.put("receiverId",     c.getReceiverId());
                m.put("receiverType",   c.getReceiverType());
                m.put("message",        c.getMessage());
                m.put("messageStatus",  c.getMessageStatus());
                m.put("doctorId",       c.getDoctorId());
                m.put("doctorName",     c.getDoctorName());
                m.put("createDatetime", c.getCreateDatetime() != null
                        ? c.getCreateDatetime().toString().replace("T", " ") : "");
                return m;
            }).toList();

            Map<String, Object> res = new LinkedHashMap<>();
            res.put("chatCount", list.size());
            res.put("list",      list);

            response.put("status",  "success");
            response.put("message", "Chat list");
            response.put("result",  res);
        } catch (Exception e) {
            log.error("getChatDoctorMessage error: {}", e.getMessage(), e);
            response.put("status",  "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // =========================================================================
    // CHAT: GET PATIENT LIST
    // =========================================================================
    public Map<String, Object> getPatientChatList() {
        Map<String, Object> response = new HashMap<>();
        try {
            List<Patient> allPatients = patientRepository.findAll();
            List<Map<String, Object>> list = new ArrayList<>();
            for (Patient p : allPatients) {
                if (p.getPatientName() == null || p.getPatientName().isEmpty()) continue;
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("patientId",   p.getPatientStringId());
                m.put("patientName", p.getPatientName());
                list.add(m);
            }

            Map<String, Object> res = new LinkedHashMap<>();
            res.put("list", list);

            response.put("status",  "success");
            response.put("message", "Chat Patient list");
            response.put("result",  res);
        } catch (Exception e) {
            log.error("getPatientChatList error: {}", e.getMessage(), e);
            response.put("status",  "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // =========================================================================
    // DOCTOR QUEUE OVERVIEW
    // =========================================================================
    public Map<String, Object> getDoctorQueueOverview(String doctorId) {
        Map<String, Object> response = new HashMap<>();
        try {
            String today = LocalDate.now(ZoneId.of("Asia/Kolkata")).toString();

            Optional<Doctor> doctorOpt = doctorRepository.findByDoctorStringId(doctorId);
            if (doctorOpt.isEmpty() || !"1".equals(doctorOpt.get().getDoctorStatus())) {
                response.put("status",  "error");
                response.put("message", "Doctor not found or inactive.");
                response.put("data",    new HashMap<>());
                return response;
            }

            List<Appointment> allAppointments = appointmentRepository
                    .findByDoctorStringIdAndDateOrdered(doctorId, today);
            List<Appointment> nonCancelled = allAppointments.stream()
                    .filter(a -> !"3".equals(a.getAppointmentStatus()))
                    .collect(java.util.stream.Collectors.toList());

            int totalAppointments = nonCancelled.size();
            if (totalAppointments == 0) {
                response.put("status",  "error");
                response.put("message", "No appointments scheduled for today.");
                response.put("data",    new HashMap<>());
                return response;
            }

            long completedPatients = nonCancelled.stream()
                    .filter(a -> "2".equals(a.getAppointmentStatus())).count();

            List<Appointment> waitingList = nonCancelled.stream()
                    .filter(a -> "1".equals(a.getAppointmentStatus()))
                    .sorted(java.util.Comparator.comparingInt(a ->
                            a.getTodayTokenNo() != null ? Integer.parseInt(a.getTodayTokenNo()) : 0))
                    .collect(java.util.stream.Collectors.toList());

            long waitingPatients = waitingList.size();

            Map<String, Object> currentPatientData = null;
            if (!waitingList.isEmpty()) {
                Appointment current = waitingList.get(0);
                currentPatientData = new LinkedHashMap<>();
                currentPatientData.put("tokenNumber", current.getTodayTokenNo());
                currentPatientData.put("patientName", current.getPatient() != null
                        ? current.getPatient().getPatientName() : "Unknown");
                currentPatientData.put("status", "OPEN/WAITING");
            }

            Map<String, Object> nextPatientData = null;
            if (waitingList.size() > 1) {
                Appointment next = waitingList.get(1);
                nextPatientData = new LinkedHashMap<>();
                nextPatientData.put("tokenNumber", next.getTodayTokenNo());
                nextPatientData.put("patientName", next.getPatient() != null
                        ? next.getPatient().getPatientName() : "Unknown");
                nextPatientData.put("status", "WAITING");
            }

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("doctorId",               doctorId);
            data.put("appointmentDate",        today);
            data.put("currentlyServing",       currentPatientData);
            data.put("nextPatient",            nextPatientData);
            data.put("totalAppointmentsToday", totalAppointments);
            data.put("completedPatients",      completedPatients);
            data.put("waitingPatients",        waitingPatients);

            final Map<String, Object> dataFinal = data;
            response.put("status",  "success");
            response.put("message", "Que list");
            response.put("result",  new LinkedHashMap<String, Object>() {{
                put("list", Collections.singletonList(new LinkedHashMap<String, Object>() {{
                    put("status",  "success");
                    put("message", "Doctor Queue Overview");
                    put("data",    dataFinal);
                }}));
            }});
        } catch (Exception e) {
            log.error("getDoctorQueueOverview error: {}", e.getMessage(), e);
            response.put("status",  "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // =========================================================================
    // GET PATIENT DETAILS
    // =========================================================================
    public Map<String, Object> getPatientDetails(String patientId) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<Patient> patientOpt = patientRepository.findByPatientStringId(patientId);

            if (patientOpt.isEmpty()) {
                List<Patient> byUser = patientRepository.findByUserUserStringId(patientId);
                if (!byUser.isEmpty()) patientOpt = Optional.of(byUser.get(0));
            }

            if (patientOpt.isEmpty()) {
                response.put("status",       "success");
                response.put("message",      "Patient not found");
                response.put("patientName",  "");
                response.put("patientPhone", "");
                return response;
            }

            Patient patient = patientOpt.get();
            response.put("status",       "success");
            response.put("message",      "Patient details");
            response.put("patientName",  patient.getPatientName()  != null ? patient.getPatientName()  : "");
            response.put("patientPhone", patient.getPhoneNumber()  != null ? patient.getPhoneNumber()  : "");
        } catch (Exception e) {
            log.error("getPatientDetails error: {}", e.getMessage(), e);
            response.put("status",       "failed");
            response.put("message",      e.getMessage());
            response.put("patientName",  "");
            response.put("patientPhone", "");
        }
        return response;
    }

    // =========================================================================
    // GET REPORTS LIST
    // =========================================================================
    public Map<String, Object> getReportsList(String patientId) {
        Map<String, Object> response = new HashMap<>();
        try {
            List<PatientDocument> docs = patientDocumentRepository.findByPatientId(patientId);
            List<Map<String, Object>> list = new ArrayList<>();
            for (PatientDocument doc : docs) {
                Map<String, Object> item = new HashMap<>();
                item.put("id",         String.valueOf(doc.getId()));
                item.put("title",      doc.getDocType());
                item.put("reportDate", doc.getUploadedAt() != null
                        ? doc.getUploadedAt().toLocalDate().toString() : "");
                item.put("url",        baseUrl + "/assets/patientDocuments/" + doc.getFileName());
                list.add(item);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("reportsCount", list.size());
            result.put("list",         list);

            response.put("status",  "success");
            response.put("message", "Reports list");
            response.put("result",  result);
        } catch (Exception e) {
            log.error("getReportsList error: {}", e.getMessage(), e);
            response.put("status",  "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // =========================================================================
    // UPLOAD FILE
    // =========================================================================
    public Map<String, Object> uploadFile(String patientId, String doctorId,
                                          String uploadFileTime, MultipartFile file) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (file == null || file.isEmpty()) {
                response.put("status",  "failed");
                response.put("message", "No file provided");
                return response;
            }

            String uploadDir = System.getProperty("user.dir") + "/assets/patientDocuments/";
            new java.io.File(uploadDir).mkdirs();
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            java.nio.file.Path filePath = java.nio.file.Paths.get(uploadDir + fileName);
            java.nio.file.Files.copy(file.getInputStream(), filePath,
                    java.nio.file.StandardCopyOption.REPLACE_EXISTING);

            PatientDocument doc = PatientDocument.builder()
                    .patientId(patientId)
                    .doctorId(doctorId)
                    .appointmentId("")
                    .fileName(fileName)
                    .docType("Report")
                    .build();
            patientDocumentRepository.save(doc);

            response.put("status",  "success");
            response.put("message", "File uploaded successfully");
            response.put("result",  new ArrayList<>());
        } catch (Exception e) {
            log.error("uploadFile error: {}", e.getMessage(), e);
            response.put("status",  "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }


    private String getDoctorNotificationTitle(String status) {
    if (status == null) return "New Appointment";
    return switch (status) {
        case "2" -> "Appointment Completed";
        case "3" -> "Appointment Cancelled";
        case "4" -> "Appointment Delayed";
        default  -> "New Appointment";
    };
}
    // =========================================================================
    // EXISTING CRUD
    // =========================================================================
    @Transactional
    public Doctor createDoctor(DoctorDto doctorDto) {
        User user = userRepository.findById(doctorDto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Doctor doctor = Doctor.builder()
                .user(user)
                .doctorName("")
                .phoneNumber("")
                .specialization(doctorDto.getSpecialization())
                .qualifications(doctorDto.getQualifications())
                .experienceYears(doctorDto.getExperienceYears())
                .consultationFee(doctorDto.getConsultationFee())
                .clinicAddress(doctorDto.getClinicAddress())
                .clinicPhone(doctorDto.getClinicPhone())
                .availabilityDays(doctorDto.getAvailabilityDays())
                .availabilityTime(doctorDto.getAvailabilityTime())
                .isVerified(false)
                .isActive(true)
                .build();
        return doctorRepository.save(doctor);
    }

    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new RuntimeException("Doctor not found"));
    }

    public Doctor getDoctorByUserId(Long userId) {
        return doctorRepository.findByUserUserId(userId)
                .orElseThrow(() -> new RuntimeException("Doctor not found for user"));
    }

    public List<Doctor> getAllActiveDoctors() {
        return doctorRepository.findByDoctorStatus("1");
    }

    public List<Doctor> getDoctorsBySpecialization(String specialization) {
        return doctorRepository.findBySpecializationAndIsActiveTrue(specialization);
    }

    @Transactional
    public Doctor updateDoctor(Long doctorId, DoctorDto doctorDto) {
        Doctor doctor = getDoctorById(doctorId);
        doctor.setSpecialization(doctorDto.getSpecialization());
        doctor.setQualifications(doctorDto.getQualifications());
        doctor.setExperienceYears(doctorDto.getExperienceYears());
        doctor.setConsultationFee(doctorDto.getConsultationFee());
        doctor.setClinicAddress(doctorDto.getClinicAddress());
        doctor.setClinicPhone(doctorDto.getClinicPhone());
        doctor.setAvailabilityDays(doctorDto.getAvailabilityDays());
        doctor.setAvailabilityTime(doctorDto.getAvailabilityTime());
        return doctorRepository.save(doctor);
    }

    @Transactional
    public Doctor verifyDoctor(Long doctorId) {
        Doctor doctor = getDoctorById(doctorId);
        doctor.setIsVerified(true);
        return doctorRepository.save(doctor);
    }

    @Transactional
    public void deleteDoctor(Long doctorId) {
        Doctor doctor = getDoctorById(doctorId);
        doctor.setIsActive(false);
        doctorRepository.save(doctor);
    }

    public DoctorDto convertToDto(Doctor doctor) {
        return DoctorDto.builder()
                .doctorId(doctor.getDoctorId())
                .userId(doctor.getUser().getUserId())
                .userName(doctor.getUser().getUserName())
                .email(doctor.getUser().getEmail())
                .specialization(doctor.getSpecialization())
                .qualifications(doctor.getQualifications())
                .experienceYears(doctor.getExperienceYears())
                .consultationFee(doctor.getConsultationFee())
                .clinicAddress(doctor.getClinicAddress())
                .clinicPhone(doctor.getClinicPhone())
                .availabilityDays(doctor.getAvailabilityDays())
                .availabilityTime(doctor.getAvailabilityTime())
                .isVerified(doctor.getIsVerified())
                .isActive(doctor.getIsActive())
                .userImg(doctor.getUser().getUserImg())
                .build();
    }

    // =========================================================================
    // UTIL
    // =========================================================================
    private String resolveSpecialisationNames(String specialization) {
        if (specialization == null || specialization.isEmpty()) return "";
        String[] specArr = specialization.split(",");
        StringBuilder sb = new StringBuilder();
        for (String specId : specArr) {
            Optional<DoctorSpecialisation> spec =
                    doctorSpecialisationRepository.findBySpecialisationId(specId.trim());
            spec.ifPresent(s -> sb.append(s.getSpecialisationName()).append(","));
        }
        return sb.length() > 0 ? sb.substring(0, sb.length() - 1) : "";
    }

    private String generateOTP(boolean developmentMode) {
        if (developmentMode) return "1234";
        return String.format("%04d", new Random().nextInt(10000));
    }
}