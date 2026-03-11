package com.miracle.service;

import com.miracle.model.*;
import com.miracle.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminService {

    private final DoctorRepository doctorRepository;
    private final DoctorSpecialisationRepository specialisationRepository;
    private final DoctorAvailableSlotTimeRepository slotRepository;
    private final PatientRepository patientRepository;
    private final LocationRepository locationRepository;
    private final AppointmentRepository appointmentRepository;
    private final AppConfigRepository appConfigRepository;
    private final AppConfigDetailRepository appConfigDetailRepository;
    private final JdbcTemplate jdbcTemplate;
    private final WhatsappService whatsappService;
    private final SMSService smsService;

    // ── DASHBOARD ────────────────────────────────────────────────────────────

    public Map<String, Object> getDashboard() {
        Map<String, Object> data = new HashMap<>();
        try {
            data.put("totalDoctors",        doctorRepository.findByDoctorStatus("1").size());
            data.put("totalPatients",        patientRepository.findByPatientStatus("1").size());
            data.put("totalAppointments",    appointmentRepository.count());
            data.put("totalSpecialisations", specialisationRepository.findBySpecialisationStatus("1").size());
            String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            data.put("todaysAppointments",   appointmentRepository.countByAppointmentDate(today));
        } catch (Exception e) {
            log.error("Dashboard error: {}", e.getMessage());
        }
        return data;
    }

    // ── DOCTOR LIST ──────────────────────────────────────────────────────────
    public List<Doctor> getDoctorList() {
        try {
            // Return ALL doctors (active + inactive) so status toggle doesn't look like deletion
            List<Doctor> list = doctorRepository.findAll();
            log.info("getDoctorList returned {} records using DB {}", list.size(), getDatasourceUrlInfo());
            return list;
        } catch (Exception e) {
            log.error("Error retrieving doctor list", e);
            return Collections.emptyList();
        }
    }

    // ── ACTIVE DOCTORS ONLY (for dropdowns in appointment booking) ────────────
    public List<Doctor> getActiveDoctorList() {
        try {
            return doctorRepository.findByDoctorStatus("1");
        } catch (Exception e) {
            log.error("Error retrieving active doctor list", e);
            return Collections.emptyList();
        }
    }

    // ── ADD DOCTOR ───────────────────────────────────────────────────────────
    public Map<String, Object> addDoctor(String doctorName, String doctorPhone,
            String[] specialisationIds, String doctorShortDesc,
            String isAdminDoctor, String scanType,String locationId,
            MultipartFile profileImage, String uploadDir) {
        Map<String, Object> response = new HashMap<>();
        try {
            // FIX: findByPhoneNumber (field is phoneNumber in Doctor.java)
            if (doctorRepository.findByPhoneNumber(doctorPhone).isPresent()) {
                response.put("status", "EXIST");
                response.put("message", "Doctor with this phone number already exists");
                return response;
            }
            String doctorId = generateHexId();
            String imgFileName = "";
            if (profileImage != null && !profileImage.isEmpty()) {
                imgFileName = doctorId + "-" + profileImage.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                Files.createDirectories(uploadPath);
                profileImage.transferTo(new File(uploadPath + "/" + imgFileName));
            }
            String specialisationStr = specialisationIds != null ? String.join(",", specialisationIds) : "";

            // FIX: builder uses correct field names from Doctor.java
            Doctor doctor = Doctor.builder()
                    .doctorStringId(doctorId)
                    .doctorName(doctorName)
                    .phoneNumber(doctorPhone)
                    .profileImage(imgFileName)
                    .specialization(specialisationStr)
                    .shortDescription(doctorShortDesc != null ? doctorShortDesc : "")
                    .scanType(scanType != null ? scanType : "")
                    .isAdminDoctor("YES".equals(isAdminDoctor) ? "YES" : "NO")
                    .locationId(locationId != null ? locationId : "")
                    .doctorStatus("1")
                    .otp("1234")
                    .serverMode("PRODUCTION")
                    .tokenId("")
                    .build();
            doctorRepository.save(doctor);

            // Create default weekly slots
            for (DoctorAvailableSlotTime.DayOfWeek day : DoctorAvailableSlotTime.DayOfWeek.values()) {
                boolean isWeekend = day == DoctorAvailableSlotTime.DayOfWeek.Saturday
                        || day == DoctorAvailableSlotTime.DayOfWeek.Sunday;
                slotRepository.save(DoctorAvailableSlotTime.builder()
                        .doctorId(doctorId)
                        .dayOfWeek(day)
                        .startTime(isWeekend ? null : LocalTime.of(9, 0))
                        .endTime(isWeekend ? null : LocalTime.of(20, 0))
                        .isLeave(isWeekend)
                        .remarks(isWeekend ? "On leave" : null)
                        .build());
            }
            response.put("status", "SUCCESS");
            response.put("message", "Doctor added successfully");
            response.put("doctorId", doctorId);
        } catch (Exception e) {
            log.error("Add doctor error: {} | Cause: {}", e.getMessage(), e.getCause(), e);
            response.put("status", "FAIL");
            response.put("message", "Failed to add doctor: " + e.getMessage());
            response.put("error", e.getClass().getSimpleName());
            response.put("details", e.getCause() != null ? e.getCause().getMessage() : "Unknown error");
        }
        return response;
    }

    // ── GET DOCTOR BY ID ─────────────────────────────────────────────────────
    // FIX: findByDoctorStringId (the varchar doctorId column)
    public Optional<Doctor> getDoctorById(String doctorId) {
        return doctorRepository.findByDoctorStringId(doctorId);
    }

    // ── UPDATE DOCTOR ────────────────────────────────────────────────────────
    public Map<String, Object> updateDoctor(String doctorId, String doctorName, String doctorPhone,
            String[] specialisationIds, String doctorShortDesc, String isAdminDoctor,
            String scanType,String locationId, MultipartFile profileImage, String uploadDir) {
        Map<String, Object> response = new HashMap<>();
        try {
            // FIX: findByDoctorStringId
            Optional<Doctor> opt = doctorRepository.findByDoctorStringId(doctorId);
            if (opt.isEmpty()) {
                response.put("status", "FAIL");
                response.put("message", "Doctor not found");
                return response;
            }
            Doctor doctor = opt.get();
            // FIX: correct setter names from Doctor.java
            doctor.setDoctorName(doctorName);
            doctor.setPhoneNumber(doctorPhone);
            doctor.setShortDescription(doctorShortDesc != null ? doctorShortDesc : "");
            doctor.setIsAdminDoctor("YES".equals(isAdminDoctor) ? "YES" : "NO");
            doctor.setScanType(scanType != null ? scanType : "");
            if (locationId != null && !locationId.isEmpty()) {
                doctor.setLocationId(locationId);
            }
            doctor.setSpecialization(specialisationIds != null ? String.join(",", specialisationIds) : "");
            if (profileImage != null && !profileImage.isEmpty()) {
                String imgFileName = doctorId + "-" + profileImage.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir);
                Files.createDirectories(uploadPath);
                profileImage.transferTo(new File(uploadPath + "/" + imgFileName));
                // FIX: setProfileImage
                doctor.setProfileImage(imgFileName);
            }
            doctorRepository.save(doctor);
            response.put("status", "SUCCESS");
            response.put("message", "Doctor updated successfully");
        } catch (Exception e) {
            log.error("Update doctor error: {}", e.getMessage(), e);
            response.put("status", "FAIL");
            response.put("message", "Failed to update doctor");
        }
        return response;
    }

    // ── UPDATE DOCTOR STATUS ─────────────────────────────────────────────────
    public void updateDoctorStatus(String doctorId, String status) {
        // FIX: findByDoctorStringId
        doctorRepository.findByDoctorStringId(doctorId).ifPresent(d -> {
            d.setDoctorStatus(status);
            doctorRepository.save(d);
        });
    }


    // ─── DELETE DOCTOR PROFILE IMAGE ─────────────────────────────────────────
    public void deleteDoctorProfileImage(String doctorId, String uploadDir) {
        doctorRepository.findByDoctorStringId(doctorId).ifPresent(d -> {
            try {
                String imgFile = d.getProfileImage();
                if (imgFile != null && !imgFile.isBlank()) {
                    java.io.File file = new java.io.File(uploadDir + "/" + imgFile);
                    if (file.exists()) {
                        file.delete();
                        log.info("Deleted doctor image: {}", imgFile);
                    }
                }
                d.setProfileImage("");
                doctorRepository.save(d);
            } catch (Exception e) {
                log.error("Delete image error: {}", e.getMessage());
            }
        });
    }

    // ── DOCTOR TIME SLOTS ────────────────────────────────────────────────────
    public List<DoctorAvailableSlotTime> getDoctorSlots(String doctorId) {
        List<DoctorAvailableSlotTime> slots = new ArrayList<>(slotRepository.findByDoctorId(doctorId));
        List<DoctorAvailableSlotTime.DayOfWeek> existing = slots.stream()
                .map(DoctorAvailableSlotTime::getDayOfWeek).toList();
        for (DoctorAvailableSlotTime.DayOfWeek day : DoctorAvailableSlotTime.DayOfWeek.values()) {
            if (!existing.contains(day)) {
                DoctorAvailableSlotTime slot = DoctorAvailableSlotTime.builder()
                        .doctorId(doctorId).dayOfWeek(day).isLeave(false).build();
                slotRepository.save(slot);
                slots.add(slot);
            }
        }
        List<DoctorAvailableSlotTime.DayOfWeek> order = List.of(
            DoctorAvailableSlotTime.DayOfWeek.Monday,    DoctorAvailableSlotTime.DayOfWeek.Tuesday,
            DoctorAvailableSlotTime.DayOfWeek.Wednesday, DoctorAvailableSlotTime.DayOfWeek.Thursday,
            DoctorAvailableSlotTime.DayOfWeek.Friday,    DoctorAvailableSlotTime.DayOfWeek.Saturday,
            DoctorAvailableSlotTime.DayOfWeek.Sunday);
        slots.sort(Comparator.comparingInt(s -> order.indexOf(s.getDayOfWeek())));
        return slots;
    }

    @Transactional
    public Map<String, Object> saveWeeklySlots(String doctorId, String[] days,
            String[] startTimes, String[] endTimes, String[] leaveFlags) {
        Map<String, Object> response = new HashMap<>();
        try {
            for (int i = 0; i < days.length; i++) {
                DoctorAvailableSlotTime.DayOfWeek day = DoctorAvailableSlotTime.DayOfWeek.valueOf(days[i]);
                boolean isLeave = leaveFlags != null && i < leaveFlags.length
                        && leaveFlags[i] != null && !leaveFlags[i].isBlank();
                Optional<DoctorAvailableSlotTime> opt = slotRepository.findByDoctorIdAndDayOfWeek(doctorId, day);
                DoctorAvailableSlotTime slot = opt.orElse(
                        DoctorAvailableSlotTime.builder().doctorId(doctorId).dayOfWeek(day).build());
                slot.setIsLeave(isLeave);
                if (!isLeave && startTimes != null && i < startTimes.length && !startTimes[i].isBlank()) {
                    slot.setStartTime(LocalTime.parse(startTimes[i]));
                    slot.setEndTime(endTimes != null && i < endTimes.length ? LocalTime.parse(endTimes[i]) : null);
                } else {
                    slot.setStartTime(null);
                    slot.setEndTime(null);
                }
                slotRepository.save(slot);
            }
            response.put("status", "SUCCESS");
            response.put("message", "Slots saved");
        } catch (Exception e) {
            log.error("Save slots error: {}", e.getMessage(), e);
            response.put("status", "FAIL");
            response.put("message", "Failed to save slots");
        }
        return response;
    }

    // ── SPECIALISATION ───────────────────────────────────────────────────────
    public List<DoctorSpecialisation> getSpecialisationList() {
        try {
            List<DoctorSpecialisation> list = specialisationRepository.findAll().stream()
                    .filter(s -> s != null && !"2".equals(s.getSpecialisationStatus()))
                    .sorted(Comparator.comparing(DoctorSpecialisation::getId))
                    .toList();
            log.info("getSpecialisationList returned {} records using DB {}", list.size(), getDatasourceUrlInfo());
            return list;
        } catch (Exception e) {
            log.error("Error retrieving specialisation list", e);
            return Collections.emptyList();
        }
    }

    public List<DoctorSpecialisation> getActiveSpecialisations() {
        try {
            List<DoctorSpecialisation> list = specialisationRepository.findBySpecialisationStatus("1");
            log.info("getActiveSpecialisations returned {} records using DB {}", list.size(), getDatasourceUrlInfo());
            return list;
        } catch (Exception e) {
            log.error("Error retrieving active specialisations", e);
            return Collections.emptyList();
        }
    }

    public Map<String, Object> addSpecialisation(String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (specialisationRepository.findBySpecialisationNameIgnoreCase(name.trim()).isPresent()) {
                response.put("status", "EXIST");
                response.put("message", "Specialisation already exists");
                return response;
            }
            specialisationRepository.save(DoctorSpecialisation.builder()
                    .specialisationId(generateHexId())
                    .specialisationName(name.trim())
                    .specialisationStatus("1")
                    .build());
            response.put("status", "SUCCESS");
            response.put("message", "Specialisation added");
        } catch (Exception e) {
            log.error("Add spec error", e);
            response.put("status", "FAIL");
            response.put("message", e.getMessage());
        }
        return response;
    }

    public void updateSpecialisationStatus(String specialisationId, String status) {
        specialisationRepository.findBySpecialisationId(specialisationId).ifPresent(s -> {
            s.setSpecialisationStatus(status);
            specialisationRepository.save(s);
        });
    }

    // ── PATIENT LIST ─────────────────────────────────────────────────────────
    public List<Patient> getPatientList() {
        try {
            // Return ALL patients (active + inactive) so status toggle doesn't look like deletion
            List<Patient> list = patientRepository.findAll();
            log.info("getPatientList returned {} records using DB {}", list.size(), getDatasourceUrlInfo());
            return list;
        } catch (Exception e) {
            log.error("Error retrieving patient list", e);
            return Collections.emptyList();
        }
    }

    // ── AVAILABLE SLOTS ──────────────────────────────────────────────────────
    public List<Map<String, Object>> getAvailableSlots(String doctorId, String dateStr) {
        try {
            // Get day of week from date string (yyyy-MM-dd)
            java.time.LocalDate date = java.time.LocalDate.parse(dateStr);
            String dayName = date.getDayOfWeek().getDisplayName(
                java.time.format.TextStyle.FULL, java.util.Locale.ENGLISH);

            // Find the doctor's slot for that day
            DoctorAvailableSlotTime.DayOfWeek day =
                DoctorAvailableSlotTime.DayOfWeek.valueOf(dayName);
            Optional<DoctorAvailableSlotTime> slotOpt =
                slotRepository.findByDoctorIdAndDayOfWeek(doctorId, day);

            List<Map<String, Object>> result = new ArrayList<>();
            if (slotOpt.isPresent() && !slotOpt.get().getIsLeave()
                    && slotOpt.get().getStartTime() != null) {
                DoctorAvailableSlotTime slot = slotOpt.get();
                java.time.LocalTime cursor = slot.getStartTime();
                java.time.LocalTime end = slot.getEndTime();
                // Generate 30-min slots
                while (cursor.plusMinutes(30).compareTo(end) <= 0) {
                    Map<String, Object> s = new HashMap<>();
                    s.put("startTime", cursor.toString());
                    s.put("endTime",   cursor.plusMinutes(30).toString());
                    result.add(s);
                    cursor = cursor.plusMinutes(30);
                }
            }
            return result;
        } catch (Exception e) {
            log.error("getAvailableSlots error: {}", e.getMessage());
            return new ArrayList<>();
        }
    }

    // ── APPOINTMENT LIST ─────────────────────────────────────────────────────
    public List<Map<String, Object>> getAppointmentList() {
        try {
            return jdbcTemplate.queryForList(
                "SELECT a.appointmentId, a.todayTokenNo, a.doctorId, a.patientId, " +
                "a.appointmentDate, a.appointmentStartTime, a.appointmentEndTime, " +
                "a.appointmentStatus, a.scanType, a.create_datetime, " +
                "d.doctorName, p.patientName " +
                "FROM appointment a " +
                "LEFT JOIN doctor d ON a.doctor_id = d.id " +
                "LEFT JOIN patients p ON a.patient_id = p.id " +
                "ORDER BY a.create_datetime DESC");
        } catch (Exception e) {
            log.error("Appointment list error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    public List<Map<String, Object>> getFilteredAppointmentList(String date, String doctorId, String patientName) {
        try {
            StringBuilder sql = new StringBuilder(
                "SELECT a.appointmentId, a.todayTokenNo, a.doctorId, a.patientId, " +
                "a.appointmentDate, a.appointmentStartTime, a.appointmentEndTime, " +
                "a.appointmentStatus, a.scanType, a.create_datetime, " +
                "d.doctorName, p.patientName " +
                "FROM appointment a " +
                "LEFT JOIN doctor d ON a.doctor_id = d.id " +
                "LEFT JOIN patients p ON a.patient_id = p.id " +
                "WHERE 1=1");
            List<Object> params = new ArrayList<>();
            if (date != null && !date.isBlank()) {
                sql.append(" AND a.appointmentDate = ?");
                params.add(date);
            }
            if (doctorId != null && !doctorId.isBlank()) {
                sql.append(" AND a.doctor_id = ?");
                params.add(doctorId);
            }
            if (patientName != null && !patientName.isBlank()) {
                sql.append(" AND p.patientName LIKE ?");
                params.add("%" + patientName + "%");
            }
            sql.append(" ORDER BY a.create_datetime DESC");
            return jdbcTemplate.queryForList(sql.toString(), params.toArray());
        } catch (Exception e) {
            log.error("Filtered appointment list error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // ── DOCTOR CALENDAR APPOINTMENTS ─────────────────────────────────────────
    public List<Map<String, Object>> getDoctorAppointments(String doctorId) {
        try {
            return jdbcTemplate.queryForList(
                "SELECT a.appointmentId, a.appointmentDate, a.appointmentStartTime, " +
                "a.appointmentEndTime, a.appointmentStatus, a.todayTokenNo, " +
                "a.patientId, p.patientName, d.doctorName " +
                "FROM appointment a " +
                "LEFT JOIN patients p ON a.patientId = p.patientId " +
                "LEFT JOIN doctor d ON a.doctorId = d.doctorId " +
                "WHERE a.doctorId = ? " +
                "ORDER BY a.appointmentDate DESC, a.appointmentStartTime ASC",
                doctorId);
        } catch (Exception e) {
            log.error("getDoctorAppointments error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // ── WHATSAPP LOGS ────────────────────────────────────────────────────────
    public List<Map<String, Object>> getWhatsappLogs() {
        try {
            List<Map<String, Object>> raw = jdbcTemplate.queryForList(
                "SELECT id, mobile, message, response, status, " +
                "DATE_FORMAT(created_at, '%d-%m-%Y %H:%i') AS createdAtFormatted " +
                "FROM whatsapp_logs ORDER BY created_at DESC");
            return raw;
        } catch (Exception e) {
            log.error("WhatsApp logs error: {}", e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    // ── APP CONFIG ───────────────────────────────────────────────────────────
    public List<AppConfig> getAppConfigList() {
        return appConfigRepository.findAll();
    }

    // FIX: findByAppConfig_AppId (navigates @ManyToOne relationship)
    public List<AppConfigDetail> getAppConfigDetails(String appId) {
        return appConfigDetailRepository.findByAppConfig_AppId(appId);
    
    }

// ── APP CONFIG CRUD ──────────────────────────────────────────────────────
    public Map<String, Object> addAppConfig(String appId, String title, String message, String appStatus) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (appConfigRepository.findByAppId(appId).isPresent()) {
                response.put("status", "EXIST");
                response.put("message", "App ID already exists");
                return response;
            }
            appConfigRepository.save(AppConfig.builder()
                    .appId(appId).title(title)
                    .message(message != null ? message : "")
                    .appStatus(appStatus != null ? appStatus : "ACTIVE")
                    .build());
            response.put("status", "SUCCESS");
            response.put("message", "App config added");
        } catch (Exception e) {
            log.error("addAppConfig error: {}", e.getMessage(), e);
            response.put("status", "FAIL");
            response.put("message", e.getMessage());
        }
        return response;
    }

    public Map<String, Object> updateAppConfig(String appId, String title, String message, String appStatus) {
        Map<String, Object> response = new HashMap<>();
        try {
            AppConfig cfg = appConfigRepository.findByAppId(appId)
                    .orElseThrow(() -> new RuntimeException("App not found"));
            if (title != null && !title.isBlank()) cfg.setTitle(title);
            if (message != null) cfg.setMessage(message);
            if (appStatus != null && !appStatus.isBlank()) cfg.setAppStatus(appStatus);
            appConfigRepository.save(cfg);
            response.put("status", "SUCCESS");
            response.put("message", "App config updated");
        } catch (Exception e) {
            log.error("updateAppConfig error: {}", e.getMessage(), e);
            response.put("status", "FAIL");
            response.put("message", e.getMessage());
        }
        return response;
    }

    public Map<String, Object> addAppConfigVersion(String appId, String versionNo, Integer versionCode,
            String appCurrentStatus, String title, String message, String redirectUrl) {
        Map<String, Object> response = new HashMap<>();
        try {
            AppConfig cfg = appConfigRepository.findByAppId(appId)
                    .orElseThrow(() -> new RuntimeException("App not found"));
            AppConfigDetail.AppStatus status = AppConfigDetail.AppStatus.valueOf(appCurrentStatus);
            appConfigDetailRepository.save(AppConfigDetail.builder()
                    .appConfig(cfg)
                    .versionNo(versionNo != null ? versionNo : "")
                    .versionCode(versionCode)
                    .appCurrentStatus(status)
                    .title(title)
                    .message(message != null ? message : "")
                    .redirectUrl(redirectUrl != null ? redirectUrl : "")
                    .build());
            response.put("status", "SUCCESS");
            response.put("message", "Version added");
        } catch (Exception e) {
            log.error("addAppConfigVersion error: {}", e.getMessage(), e);
            response.put("status", "FAIL");
            response.put("message", e.getMessage());
        }
        return response;
    }

    public Map<String, Object> updateAppConfigVersion(Long detailId, Integer versionCode,
            String appCurrentStatus, String title, String message, String redirectUrl) {
        Map<String, Object> response = new HashMap<>();
        try {
            AppConfigDetail ver = appConfigDetailRepository.findById(detailId)
                    .orElseThrow(() -> new RuntimeException("Version not found"));
            ver.setVersionCode(versionCode);
            ver.setAppCurrentStatus(AppConfigDetail.AppStatus.valueOf(appCurrentStatus));
            ver.setTitle(title);
            if (message != null) ver.setMessage(message);
            if (redirectUrl != null) ver.setRedirectUrl(redirectUrl);
            appConfigDetailRepository.save(ver);
            response.put("status", "SUCCESS");
            response.put("message", "Version updated");
        } catch (Exception e) {
            log.error("updateAppConfigVersion error: {}", e.getMessage(), e);
            response.put("status", "FAIL");
            response.put("message", e.getMessage());
        }
        return response;
    }

    public Map<String, Object> deleteAppConfigVersion(Long detailId) {
        Map<String, Object> response = new HashMap<>();
        try {
            appConfigDetailRepository.deleteById(detailId);
            response.put("status", "SUCCESS");
            response.put("message", "Version deleted");
        } catch (Exception e) {
            log.error("deleteAppConfigVersion error: {}", e.getMessage(), e);
            response.put("status", "FAIL");
            response.put("message", e.getMessage());
        }
        return response;
    }





    // ── SEND MESSAGES ────────────────────────────────────────────────────────
    public Map<String, Object> sendWhatsappMessage(String mobile, String message) {
        try {
            return whatsappService.sendMessage(mobile, message);
        } catch (Exception e) {
            return Map.of("status", "FAIL", "message", e.getMessage());
        }
    }

    public Map<String, Object> sendSmsMessage(String mobile, String message) {
        try {
            return smsService.sendSms(message, mobile, "1307162865820438610");
        } catch (Exception e) {
            return Map.of("status", "FAIL", "message", e.getMessage());
        }
    }

    // ── SPECIALISATION ID → NAME MAP ─────────────────────────────────────────
    public Map<String, String> getSpecialisationIdNameMap() {
        Map<String, String> map = new HashMap<>();
        specialisationRepository.findAll()
                .forEach(s -> map.put(s.getSpecialisationId(), s.getSpecialisationName()));
        return map;
    }

    // ── LOCATION ─────────────────────────────────────────────────────────────
    public List<Location> getLocationList() {
        try {
            return locationRepository.findAll().stream()
                    .filter(l -> !"2".equals(l.getLocationStatus()))
                    .sorted(Comparator.comparing(Location::getId))
                    .toList();
        } catch (Exception e) {
            log.error("Error retrieving location list", e);
            return Collections.emptyList();
        }
    }

    public List<Location> getActiveLocations() {
        try {
            return locationRepository.findByLocationStatusOrderByIdAsc("1");
        } catch (Exception e) {
            log.error("Error retrieving active locations", e);
            return Collections.emptyList();
        }
    }

    public Map<String, Object> addLocation(String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            if (name == null || name.isBlank()) {
                response.put("status", "FAIL");
                response.put("message", "Location name is required");
                return response;
            }
            if (locationRepository.findByLocationNameIgnoreCase(name.trim()).isPresent()) {
                response.put("status", "EXIST");
                response.put("message", "Location already exists");
                return response;
            }
            locationRepository.save(Location.builder()
                    .locationId(generateHexId())
                    .locationName(name.trim())
                    .locationStatus("1")
                    .build());
            response.put("status", "SUCCESS");
            response.put("message", "Location added successfully");
        } catch (Exception e) {
            log.error("Add location error", e);
            response.put("status", "FAIL");
            response.put("message", e.getMessage());
        }
        return response;
    }

    public Map<String, Object> updateLocation(String locationId, String name) {
        Map<String, Object> response = new HashMap<>();
        try {
            Location loc = locationRepository.findByLocationId(locationId)
                    .orElseThrow(() -> new RuntimeException("Location not found"));
            if (name == null || name.isBlank()) {
                response.put("status", "FAIL");
                response.put("message", "Location name is required");
                return response;
            }
            loc.setLocationName(name.trim());
            locationRepository.save(loc);
            response.put("status", "SUCCESS");
            response.put("message", "Location updated successfully");
        } catch (Exception e) {
            log.error("Update location error", e);
            response.put("status", "FAIL");
            response.put("message", e.getMessage());
        }
        return response;
    }

    public void updateLocationStatus(String locationId, String status) {
        locationRepository.findByLocationId(locationId).ifPresent(l -> {
            l.setLocationStatus(status);
            locationRepository.save(l);
        });
    }

    // ── UTIL ─────────────────────────────────────────────────────────────────
    private String generateHexId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }


    // ── CHECK CONNECTION ──────────────────────────────────────────────────────

    // ── CHECK CONNECTION ──────────────────────────────────────────────────────
    public Optional<String> checkConnection() {
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.of(e.getMessage());
        }
    }

    // ── DATASOURCE URL INFO ───────────────────────────────────────────────────
    public String getDatasourceUrlInfo() {
        try {
            return jdbcTemplate.queryForObject("SELECT DATABASE()", String.class);
        } catch (Exception e) {
            return "unknown";
        }
    }
}