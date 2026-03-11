package com.miracle.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miracle.model.Doctor;
import com.miracle.model.DoctorSpecialisation;
import com.miracle.model.Location;
import com.miracle.model.Patient;
import com.miracle.model.User;
import com.miracle.model.Appointment;
import com.miracle.model.PatientDocument;
import com.miracle.repository.AppConfigRepository;
import com.miracle.repository.PatientDocumentRepository;
import com.miracle.repository.PatientRepository;
import com.miracle.repository.UserRepository;
import com.miracle.repository.AppointmentRepository;
import com.miracle.repository.DoctorRepository;
import com.miracle.service.AdminService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.miracle.repository.LocationRepository;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AdminViewController {

    private final AdminService adminService;
    private final AppConfigRepository appConfigRepository;
    private final PatientRepository patientRepository;
    private final UserRepository userRepository;
    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientDocumentRepository patientDocumentRepository;
    private final JdbcTemplate jdbcTemplate;
    private final LocationRepository locationRepository;

    @Value("${app.upload.dir:}")
    private String uploadDir;

    @jakarta.annotation.PostConstruct
    public void resolveUploadDir() {
        // If uploadDir not configured, default to user home assets dir (works in both dev and jar)
        if (uploadDir == null || uploadDir.isBlank()) {
            uploadDir = System.getProperty("user.dir") + "/src/main/webapp/assets/doctorProfileImage";
        }
        try {
            java.nio.file.Files.createDirectories(java.nio.file.Paths.get(uploadDir));
            log.info("Doctor image upload dir: {}", uploadDir);
        } catch (Exception e) {
            log.warn("Could not create upload dir {}: {}", uploadDir, e.getMessage());
        }
    }

    private boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("adminEmail") != null;
    }

    // ─── DASHBOARD ────────────────────────────────────────────────────────────
    @GetMapping("/admin/dashboard")
    public String dashboard(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        Map<String, Object> data = adminService.getDashboard();
        model.addAttribute("doctors",              data.getOrDefault("totalDoctors", 0));
        model.addAttribute("patients",             data.getOrDefault("totalPatients", 0));
        model.addAttribute("appointments",         data.getOrDefault("totalAppointments", 0));
        model.addAttribute("totalSpecialisations", data.getOrDefault("totalSpecialisations", 0));
        model.addAttribute("todaysAppointments",   data.getOrDefault("todaysAppointments", 0));
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/dashboard";
    }

    // ─── DOCTOR LIST ──────────────────────────────────────────────────────────
    @GetMapping("/admin/doctor/doctorlist")
    public String doctorList(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        try {
            List<Doctor> docs = adminService.getDoctorList();
            List<DoctorSpecialisation> acts = adminService.getActiveSpecialisations();
            model.addAttribute("doctorList", docs);
            model.addAttribute("activeSpecialisationList", acts);
            model.addAttribute("specialisationMap", adminService.getSpecialisationIdNameMap());
            model.addAttribute("locationList", adminService.getActiveLocations());
        } catch (Exception e) {
            log.error("Failed to load doctor list", e);
            model.addAttribute("doctorList", Collections.emptyList());
            model.addAttribute("activeSpecialisationList", Collections.emptyList());
            model.addAttribute("specialisationMap", Collections.emptyMap());
            model.addAttribute("locationList", Collections.emptyList());
        }
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/doctor/doctorlist";
    }

    // ─── ADD DOCTOR ───────────────────────────────────────────────────────────
    @PostMapping(value = "/adminmodel/adddoctor", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addDoctor(
            @RequestParam String doctorName,
            @RequestParam String doctorPhone,
            @RequestParam(value = "doctorSpecialisation[]", required = false) String[] specialisationIds,
            @RequestParam(required = false) String doctorShortDesc,
            @RequestParam(required = false) String isAdminDoctor,
            @RequestParam(value = "scanType[]", required = false) String[] scanTypes,
            @RequestParam(required = false) String locationId,
            @RequestParam(value = "doctorProfileImage", required = false) MultipartFile profileImage,
            
            HttpSession session) {
        if (!isLoggedIn(session)) return "{\"status\":\"FAIL\",\"message\":\"Not logged in\"}";
        String scanType = scanTypes != null ? String.join(",", scanTypes) : "";
        return toJson(adminService.addDoctor(doctorName, doctorPhone, specialisationIds,
                doctorShortDesc, isAdminDoctor, scanType, locationId, profileImage, uploadDir));
    }

    // ─── EDIT DOCTOR PAGE ─────────────────────────────────────────────────────
    @GetMapping("/admin/doctor/editdoctor/{doctorId}")
    public String editDoctorPage(@PathVariable String doctorId, HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        adminService.getDoctorById(doctorId).ifPresent(d -> model.addAttribute("doctor", d));
        model.addAttribute("activeSpecialisationList", adminService.getActiveSpecialisations());
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        model.addAttribute("locationList", adminService.getActiveLocations());
        return "admin/doctor/editdoctor";
    }

    // ─── UPDATE DOCTOR ────────────────────────────────────────────────────────
    @PostMapping(value = "/adminmodel/editdoctor", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateDoctor(
            @RequestParam String doctorId,
            @RequestParam String doctorName,
            @RequestParam String doctorPhone,
            @RequestParam(value = "doctorSpecialisation[]", required = false) String[] specialisationIds,
            @RequestParam(required = false) String doctorShortDesc,
            @RequestParam(required = false) String isAdminDoctor,
            @RequestParam(value = "scanType[]", required = false) String[] scanTypes,
            @RequestParam(required = false) String locationId,
            @RequestParam(value = "doctorProfileImage", required = false) MultipartFile profileImage,
            HttpSession session) {
        if (!isLoggedIn(session)) return "{\"status\":\"FAIL\",\"message\":\"Not logged in\"}";
        String scanType = scanTypes != null ? String.join(",", scanTypes) : "";
        return toJson(adminService.updateDoctor(doctorId, doctorName, doctorPhone,
                specialisationIds, doctorShortDesc, isAdminDoctor, scanType, locationId, profileImage, uploadDir));
    }

    // ─── UPDATE DOCTOR STATUS ─────────────────────────────────────────────────
    @GetMapping("/adminmodel/updateDoctorStatus/{status}/{doctorId}")
    public String updateDoctorStatus(@PathVariable String status, @PathVariable String doctorId,
                                     HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        adminService.updateDoctorStatus(doctorId, status);
        return "redirect:/admin/doctor/doctorlist";
    }

    // ─── DELETE DOCTOR PROFILE IMAGE ─────────────────────────────────────────
    @GetMapping("/adminmodel/deldoctorprofileimg/{doctorId}")
    public String deleteDoctorProfileImg(@PathVariable String doctorId, HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        adminService.deleteDoctorProfileImage(doctorId, uploadDir);
        return "redirect:/admin/doctor/editdoctor/" + doctorId;
    }


    // ─── DOCTOR TIME SLOTS ────────────────────────────────────────────────────
    @GetMapping("/admin/doctor/doctorTimeSlots/{doctorId}")
    public String doctorTimeSlots(@PathVariable String doctorId, HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        adminService.getDoctorById(doctorId).ifPresent(d -> {
            model.addAttribute("doctorId", doctorId);
            model.addAttribute("doctorName", d.getDoctorName());
        });
        // Get slots as a Map<dayName, slot> so JSP can do ${slots[day]}
        List<com.miracle.model.DoctorAvailableSlotTime> slotList = adminService.getDoctorSlots(doctorId);
        Map<String, com.miracle.model.DoctorAvailableSlotTime> slotsMap = new java.util.LinkedHashMap<>();
        List<String> dayNames = List.of("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday");
        for (String d : dayNames) slotsMap.put(d, null); // init order
        for (com.miracle.model.DoctorAvailableSlotTime s : slotList) {
            if (s.getDayOfWeek() != null) slotsMap.put(s.getDayOfWeek().name(), s);
        }
        model.addAttribute("slots", slotsMap);
        model.addAttribute("days", dayNames);
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/doctor/doctorTimeSlots";
    }

    // ─── SAVE WEEKLY SLOTS ────────────────────────────────────────────────────
    // JSP sends per-day params: startTime[Monday]=09:00, endTime[Monday]=17:00, isLeave[Monday]=1
    @PostMapping("/adminmodel/saveWeeklySlots")
    public String saveWeeklySlots(
            @RequestParam String doctorId,
            jakarta.servlet.http.HttpServletRequest request,
            HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        List<String> dayNames = List.of("Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday");
        String[] days       = dayNames.toArray(new String[0]);
        String[] startTimes = new String[days.length];
        String[] endTimes   = new String[days.length];
        String[] leaveFlags = new String[days.length];
        for (int i = 0; i < days.length; i++) {
            startTimes[i] = request.getParameter("startTime[" + days[i] + "]");
            endTimes[i]   = request.getParameter("endTime["   + days[i] + "]");
            leaveFlags[i] = request.getParameter("isLeave["   + days[i] + "]"); // "1" or null
        }
        adminService.saveWeeklySlots(doctorId, days, startTimes, endTimes, leaveFlags);
        return "redirect:/admin/doctor/doctorTimeSlots/" + doctorId + "?saved=1";
    }

    // ─── DOCTOR CALENDAR ──────────────────────────────────────────────────────
    @GetMapping("/admin/doctor/doctorCalendar/{doctorId}")
    public String doctorCalendar(@PathVariable String doctorId, HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        adminService.getDoctorById(doctorId).ifPresent(d -> {
            model.addAttribute("doctorId", doctorId);
            model.addAttribute("doctorName", d.getDoctorName());
        });
        // Load doctor's appointments for the calendar
        model.addAttribute("appointments", adminService.getDoctorAppointments(doctorId));
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/doctor/doctorCalendar";
    }

    // ─── SPECIALISATION LIST ──────────────────────────────────────────────────
    @GetMapping("/admin/doctor/specialisationlist")
    public String specialisationList(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        adminService.checkConnection().ifPresent(err -> model.addAttribute("dbError", err));
        try {
            model.addAttribute("specialisationList", adminService.getSpecialisationList());
        } catch (Exception e) {
            model.addAttribute("specialisationList", Collections.emptyList());
        }
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/doctor/specialisationlist";
    }

    // ─── ADD SPECIALISATION ───────────────────────────────────────────────────
    @PostMapping(value = "/adminmodel/addspecialisation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addSpecialisation(@RequestParam String specialisationName, HttpSession session) {
        if (!isLoggedIn(session)) return "{\"status\":\"FAIL\",\"message\":\"Not logged in\"}";
        return toJson(adminService.addSpecialisation(specialisationName));
    }

    // ─── UPDATE SPECIALISATION STATUS ─────────────────────────────────────────
    @GetMapping("/adminmodel/updateSpecialisationStatus/{status}/{specialisationId}")
    public String updateSpecialisationStatus(@PathVariable String status,
                                              @PathVariable String specialisationId,
                                              HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        adminService.updateSpecialisationStatus(specialisationId, status);
        return "redirect:/admin/doctor/specialisationlist";
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // PATIENT SECTION
    // ═══════════════════════════════════════════════════════════════════════════

    @GetMapping("/admin/patient/patientlist")
    public String patientList(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        try {
            model.addAttribute("patientList", adminService.getPatientList());
        } catch (Exception e) {
            log.error("Failed to load patient list", e);
            model.addAttribute("patientList", Collections.emptyList());
        }
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/patient/patientlist";
    }

    @GetMapping("/admin/patient/patientDetails/{patientId}")
    public String patientDetails(@PathVariable String patientId, HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        try {
            patientRepository.findByPatientStringId(patientId).ifPresent(patient -> {
                model.addAttribute("patient", patient);
                model.addAttribute("patientId", patient.getPatientStringId());
                model.addAttribute("patientName", patient.getPatientName());
                List<Map<String, Object>> appts = jdbcTemplate.queryForList(
                    "SELECT a.appointmentDate, a.appointmentStartTime, a.appointmentStatus, " +
                    "d.doctorName FROM appointment a " +
                    "LEFT JOIN doctor d ON a.doctorStringId = d.doctorStringId " +
                    "WHERE a.patient_id = ? ORDER BY a.id DESC", patient.getPatientId());
                model.addAttribute("appointmentList", appts);
            });
        } catch (Exception e) {
            log.error("Failed to load patient details: {}", patientId, e);
        }
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/patient/patientDetails";
    }

    @GetMapping("/admin/patient/editpatient/{patientId}")
    public String editPatientPage(@PathVariable String patientId, HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        try {
            patientRepository.findByPatientStringId(patientId).ifPresent(patient -> {
                model.addAttribute("patientId",                    patient.getPatientStringId());
                model.addAttribute("patientName",                  patient.getPatientName());
                model.addAttribute("patientRelation",              patient.getRelation());
                model.addAttribute("patientPhone",                 patient.getPhoneNumber());
                model.addAttribute("patientGender",                patient.getPatientGender());
                model.addAttribute("patientDateOfBirth",           patient.getPatientDateOfBirth());
                model.addAttribute("patientAge",                   patient.getPatientAge());
                model.addAttribute("patientIsPregnant",            patient.getIsPregnant());
                model.addAttribute("patientLmpDate",               patient.getLmpDate());
                model.addAttribute("patientExpectedDeliveryDate",  patient.getExpectedDeliveryDate());
                model.addAttribute("userStringId",
                    patient.getUser() != null ? patient.getUser().getUserStringId() : "");
            });
        } catch (Exception e) {
            log.error("Failed to load patient for edit: {}", patientId, e);
        }
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/patient/editpatient";
    }

    @PostMapping(value = "/adminmodel/updatePatient", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updatePatient(
            @RequestParam String patientId,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String patientRelation,
            @RequestParam(required = false) String patientPhone,
            @RequestParam(required = false) String patientGender,
            @RequestParam(required = false) String patientDateOfBirth,
            @RequestParam(required = false) String patientAge,
            @RequestParam(required = false, defaultValue = "0") String isPregnant,
            @RequestParam(required = false) String lmpDate,
            @RequestParam(required = false) String expectedDeliveryDate,
            HttpSession session) {
        if (!isLoggedIn(session)) return "{\"status\":\"FAIL\",\"message\":\"Not logged in\"}";
        try {
            Patient p = patientRepository.findByPatientStringId(patientId)
                    .orElseThrow(() -> new RuntimeException("Patient not found"));
            if (patientName        != null && !patientName.isBlank())   p.setPatientName(patientName);
            if (patientRelation    != null) p.setRelation(patientRelation);
            if (patientPhone       != null && !patientPhone.isBlank())  p.setPhoneNumber(patientPhone);
            if (patientGender      != null) p.setPatientGender(patientGender);
            if (patientDateOfBirth != null) p.setPatientDateOfBirth(patientDateOfBirth);
            if (patientAge         != null) p.setPatientAge(patientAge);
            p.setIsPregnant(isPregnant);
            if (lmpDate              != null) p.setLmpDate(lmpDate);
            if (expectedDeliveryDate != null) p.setExpectedDeliveryDate(expectedDeliveryDate);
            patientRepository.save(p);
            return "{\"status\":\"success\",\"message\":\"Patient updated successfully\"}";
        } catch (Exception e) {
            log.error("Update patient error", e);
            return "{\"status\":\"FAIL\",\"message\":\"" + e.getMessage() + "\"}";
        }
    }

    @GetMapping("/adminmodel/updatePatientStatus/{status}/{patientId}")
    public String updatePatientStatus(@PathVariable String status, @PathVariable Long patientId,
                                      HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        try {
            patientRepository.findById(patientId).ifPresent(p -> {
                p.setIsActive("1".equals(status));
                p.setPatientStatus(status);
                patientRepository.save(p);
            });
        } catch (Exception e) {
            log.error("Update patient status error", e);
        }
        return "redirect:/admin/patient/patientlist";
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // APPOINTMENT SECTION
    // ═══════════════════════════════════════════════════════════════════════════

    @GetMapping("/admin/appointment/appointmentlist")
    public String appointmentList(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        model.addAttribute("appointmentList", adminService.getAppointmentList());
        model.addAttribute("doctorList", adminService.getActiveDoctorList());
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/appointment/appointmentlist";
    }

    // handle filter form submission (POST) so the filter UI works without 405
    @PostMapping("/admin/appointment/appointmentlist")
    public String appointmentListFilter(@RequestParam(required = false) String appointmentDate,
                                        @RequestParam(required = false) String doctor,
                                        @RequestParam(required = false) String patientName,
                                        HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        List<Map<String,Object>> appts = adminService.getFilteredAppointmentList(
                appointmentDate, doctor, patientName);
        model.addAttribute("appointmentList", appts);
        model.addAttribute("filterAppointmentDate", appointmentDate);
        model.addAttribute("filterDoctorId", doctor);
        model.addAttribute("filterPatientName", patientName);
        model.addAttribute("doctorList", adminService.getActiveDoctorList());
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/appointment/appointmentlist";
    }

    // ─── UPDATE APPOINTMENT STATUS (checks, completes, cancels) ─────────────
    @GetMapping("/adminmodel/updateAppointmentStatus/{status}/{appointmentId}")
    public String updateAppointmentStatus(@PathVariable String status,
                                          @PathVariable String appointmentId,
                                          HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        try {
            // Map numeric status code to enum label for status column
            String statusLabel;
            switch (status) {
                case "2": statusLabel = "COMPLETED"; break;
                case "3": statusLabel = "CANCELLED"; break;
                case "4": statusLabel = "DELAYED"; break;
                default:  statusLabel = "SCHEDULED";
            }
            jdbcTemplate.update(
                "UPDATE appointment SET appointmentStatus = ?, status = ? WHERE appointmentId = ?",
                status, statusLabel, appointmentId);
        } catch (Exception e) {
            log.error("Error updating appointment status for {}: {}", appointmentId, e.getMessage(), e);
        }
        return "redirect:/admin/appointment/appointmentlist";
    }

    // ─── RESCHEDULE APPOINTMENT (AJAX) ──────────────────────────────────────
    @PostMapping(value = "/adminmodel/rescheduleAppointment", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> rescheduleAppointment(
            @RequestParam String appointmentId,
            @RequestParam String appointmentDate,
            @RequestParam String appointmentStartTime,
            @RequestParam(required = false) String appointmentEndTime) {
        Map<String, Object> resp = new HashMap<>();
        try {
            int rows = jdbcTemplate.update(
                "UPDATE appointment SET appointmentDate = ?, appointmentStartTime = ?, " +
                "appointmentEndTime = ?, appointmentStatus = '1', status = 'RESCHEDULED' " +
                "WHERE appointmentId = ?",
                appointmentDate,
                appointmentStartTime,
                appointmentEndTime != null ? appointmentEndTime : "",
                appointmentId);
            if (rows > 0) {
                resp.put("status", "success");
                resp.put("message", "Appointment rescheduled successfully");
            } else {
                resp.put("status", "failed");
                resp.put("message", "Appointment not found with ID: " + appointmentId);
            }
        } catch (Exception e) {
            log.error("Reschedule error for {}: {}", appointmentId, e.getMessage(), e);
            resp.put("status", "failed");
            resp.put("message", "Failed to reschedule: " + e.getMessage());
        }
        return resp;
    }

    // ─── APPOINTMENT DETAILS ─────────────────────────────────────────────────
    @GetMapping("/admin/appointment/details/{appointmentId}")
    public String appointmentDetails(@PathVariable String appointmentId,
                                     HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        try {
            Optional<com.miracle.model.Appointment> apptOpt =
                    appointmentRepository.findByAppointmentStringId(appointmentId);
            if (apptOpt.isEmpty()) return "redirect:/admin/appointment/appointmentlist";

            com.miracle.model.Appointment a = apptOpt.get();
            Map<String, Object> apptMap = new LinkedHashMap<>();
            apptMap.put("appointmentId",        a.getAppointmentStringId());
            apptMap.put("todayTokenNo",          a.getTodayTokenNo() != null ? a.getTodayTokenNo() : "");
            apptMap.put("doctorName",            a.getDoctor() != null ? a.getDoctor().getDoctorName() : "");
            apptMap.put("patientName",           a.getPatient() != null ? a.getPatient().getPatientName() : "");
            apptMap.put("patientId",             a.getPatient() != null ? a.getPatient().getPatientStringId() : "");
            apptMap.put("scanType",              a.getScanType() != null ? a.getScanType() : "");
            apptMap.put("appointmentDate",       a.getAppointmentDate());
            apptMap.put("appointmentStartTime",  a.getAppointmentStartTime());
            apptMap.put("appointmentEndTime",    a.getAppointmentEndTime() != null ? a.getAppointmentEndTime() : "");
            apptMap.put("appointmentStatus",     a.getAppointmentStatus() != null ? a.getAppointmentStatus() : "1");
            String locationName = "N/A";
                 if (a.getDoctor() != null && a.getDoctor().getLocationId() != null && !a.getDoctor().getLocationId().isEmpty()) {
                     locationName = locationRepository.findByLocationId(a.getDoctor().getLocationId())
                     .map(l -> l.getLocationName()).orElse("N/A");
                       }
            apptMap.put("locationName", locationName); 
            apptMap.put("createdAt",             a.getCreatedAt() != null ? a.getCreatedAt().toString().replace("T", " ") : "");
            apptMap.put("updatedAt",             a.getUpdatedAt() != null ? a.getUpdatedAt().toString().replace("T", " ") : "");

            // Load documents for this appointment
            String patientStringId = a.getPatient() != null ? a.getPatient().getPatientStringId() : "";
            List<PatientDocument> docs = patientDocumentRepository.findByPatientId(patientStringId);
            List<Map<String, Object>> docList = new ArrayList<>();
            for (PatientDocument doc : docs) {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("docType",    doc.getDocType() != null ? doc.getDocType() : "Report");
                d.put("fileName",   doc.getFileName());
                d.put("uploadedAt", doc.getUploadedAt() != null ? doc.getUploadedAt().toLocalDate().toString() : "");
                d.put("url",        "/assets/patientDocuments/" + doc.getFileName());
                docList.add(d);
            }

            model.addAttribute("appointment", apptMap);
            model.addAttribute("documents",   docList);
            model.addAttribute("user_name",   session.getAttribute("adminName"));
            model.addAttribute("usertype",    "Admin");
        } catch (Exception e) {
            log.error("appointmentDetails error: {}", e.getMessage(), e);
            return "redirect:/admin/appointment/appointmentlist";
        }
        return "admin/appointment/appointmentDetails";
    }

    // ─── UPLOAD APPOINTMENT DOCUMENT ─────────────────────────────────────────
    @PostMapping("/adminmodel/uploadAppointmentDocument")
    public String uploadAppointmentDocument(
            @RequestParam String appointmentId,
            @RequestParam(required = false) String patientId,
            @RequestParam String docType,
            @RequestParam("documentFile") MultipartFile documentFile,
            HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        try {
            if (documentFile != null && !documentFile.isEmpty()) {
                String uploadDirPath = System.getProperty("user.dir") + "/assets/patientDocuments/";
                new java.io.File(uploadDirPath).mkdirs();
                String fileName = System.currentTimeMillis() + "_" + documentFile.getOriginalFilename();
                java.nio.file.Files.copy(documentFile.getInputStream(),
                        java.nio.file.Paths.get(uploadDirPath + fileName),
                        java.nio.file.StandardCopyOption.REPLACE_EXISTING);
                com.miracle.model.PatientDocument doc = com.miracle.model.PatientDocument.builder()
                        .patientId(patientId != null ? patientId : "")
                        .doctorId("")
                        .appointmentId(appointmentId)
                        .fileName(fileName)
                        .docType(docType)
                        .build();
                patientDocumentRepository.save(doc);
            }
        } catch (Exception e) {
            log.error("uploadAppointmentDocument error: {}", e.getMessage(), e);
        }
        return "redirect:/admin/appointment/details/" + appointmentId;
    }

    @GetMapping("/admin/appointment/addappointment")
    public String addAppointmentPage(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        model.addAttribute("doctorList", adminService.getActiveDoctorList());
        try {
            model.addAttribute("locationList", adminService.getLocationList());
        } catch (Exception e) {
            model.addAttribute("locationList", Collections.emptyList());
        }
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/appointment/addappointment";
    }

    // ─── GET AVAILABLE SLOTS (AJAX) ───────────────────────────────────────────
    @PostMapping(value = "/Patientapi/getAvailableSlots", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> getAvailableSlots(
            @RequestParam(required = false) String doctorId, @RequestParam(required = false) String date) {
        Map<String, Object> response = new HashMap<>();
        if (doctorId == null || doctorId.isBlank() || date == null || date.isBlank()) {
            response.put("status", "failed");
            response.put("message", "doctorId and date are required");
            return response;
        }
        try {
            List<Map<String, Object>> slots = adminService.getAvailableSlots(doctorId, date);
            response.put("status", "success");
            response.put("result", Map.of("list", slots));
        } catch (Exception e) {
            log.error("Get slots error", e);
            response.put("status", "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // ─── GET USER BY PHONE (AJAX) ─────────────────────────────────────────────
    @PostMapping(value = "/Patientapi/getUserByPhone", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> getUserByPhone(@RequestParam String phone) {
        Map<String, Object> response = new HashMap<>();
        try {
            Optional<User> userOpt = userRepository.findByPhoneNumber(phone);
            if (userOpt.isEmpty()) userOpt = userRepository.findByUserPhone(phone);

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Map<String, Object> userMap = new HashMap<>();
                userMap.put("userId",    user.getUserStringId());   // STRING id not Long PK
                userMap.put("userName",  user.getUserName());
                userMap.put("userPhone", user.getUserPhone());
                response.put("status", "success");
                response.put("user", userMap);

                List<Patient> patients = patientRepository.findByUserAndIsActiveTrue(user);
                List<Map<String, Object>> patientList = new ArrayList<>();
                for (Patient p : patients) {
                    Map<String, Object> pm = new HashMap<>();
                    pm.put("patientId",       p.getPatientStringId()); // STRING id for appointment
                    pm.put("patientName",     p.getPatientName());
                    pm.put("patientRelation", p.getRelation());
                    pm.put("patientAge",      p.getPatientAge());
                    patientList.add(pm);
                }
                response.put("patients", patientList);
            } else {
                response.put("status",  "notfound");
                response.put("message", "User not found");
            }
        } catch (Exception e) {
            log.error("getUserByPhone error", e);
            response.put("status", "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // ─── ADD NEW PATIENT (AJAX from appointment page) ─────────────────────────
    @PostMapping(value = "/Patientapi/addPatient", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> addPatientFromAppointment(
            @RequestParam(required = false) String userId,
            @RequestParam String userPhone,
            @RequestParam String patientName,
            @RequestParam(required = false, defaultValue = "Self") String patientRelation,
            @RequestParam(required = false) String patientPhone,
            @RequestParam(required = false) String patientGender,
            @RequestParam(required = false) String patientDateOfBirth,
            @RequestParam(required = false) String patientAge) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Find or create user
            User user = null;
            if (userId != null && !userId.isBlank()) {
                user = userRepository.findByUserStringId(userId).orElse(null);
            }
            if (user == null) {
                Optional<User> uOpt = userRepository.findByPhoneNumber(userPhone);
                if (uOpt.isEmpty()) uOpt = userRepository.findByUserPhone(userPhone);
                if (uOpt.isPresent()) {
                    user = uOpt.get();
                } else {
                    // Create brand new user
                    String newStringId = "USR" + System.currentTimeMillis();
                    user = userRepository.save(User.builder()
                            .userStringId(newStringId)
                            .userName("USER_" + userPhone)
                            .userPhone(userPhone)
                            .phoneNumber(userPhone)
                            .otp("1234")
                            .userRole(User.UserRole.PATIENT)
                            .isActive(true)
                            .serverMode("DEVELOPMENT")
                            .build());
                }
            }

            // Create patient
            String patientStringId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            Patient saved = patientRepository.save(Patient.builder()
                    .patientStringId(patientStringId)
                    .user(user)
                    .patientName(patientName)
                    .relation(patientRelation != null ? patientRelation : "Self")
                    .phoneNumber(patientPhone != null && !patientPhone.isBlank() ? patientPhone : userPhone)
                    .patientGender(patientGender != null ? patientGender : "")
                    .patientDateOfBirth(patientDateOfBirth != null ? patientDateOfBirth : "")
                    .patientAge(patientAge != null ? patientAge : "")
                    .patientStatus("1")
                    .isActive(true)
                    .build());

            Map<String, Object> result = new HashMap<>();
            result.put("patientId",       saved.getPatientStringId()); // STRING id for appointment
            result.put("patientName",     saved.getPatientName());
            result.put("patientRelation", saved.getRelation());
            result.put("patientAge",      saved.getPatientAge());
            result.put("userId",          user.getUserStringId());

            response.put("status",  "success");
            response.put("message", "Patient added successfully");
            response.put("result",  result);
        } catch (Exception e) {
            log.error("addPatient error", e);
            response.put("status",  "failed");
            response.put("message", e.getMessage());
        }
        return response;
    }

    // ─── BOOK APPOINTMENT (AJAX) ──────────────────────────────────────────────
    @PostMapping(value = "/Patientapi/addAppointment", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> addAppointment(
            @RequestParam String userId,
            @RequestParam String doctorId,
            @RequestParam String patientId,
            @RequestParam String date,
            @RequestParam String startTime,
            @RequestParam(required = false) String endTime,
            @RequestParam(required = false) String scanType) {
        Map<String, Object> response = new HashMap<>();
        try {
            // Resolve numeric PKs from string IDs
            Long patientNumericId = patientRepository.findByPatientStringId(patientId)
                    .map(p -> p.getPatientId()).orElse(null);
            Long doctorNumericId = doctorRepository.findByDoctorStringId(doctorId)
                    .map(d -> d.getDoctorId()).orElse(null);

            if (patientNumericId == null) {
                response.put("status", "failed");
                response.put("message", "Patient not found. Please search again.");
                return response;
            }
            if (doctorNumericId == null) {
                response.put("status", "failed");
                response.put("message", "Doctor not found. Please try again.");
                return response;
            }

            // Generate appointment string ID and token
            String apptId  = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
            long tokenCount = appointmentRepository.countByAppointmentDate(date);
            String tokenNo  = String.valueOf(tokenCount + 1);

            // Direct JDBC insert — fills ALL required columns (avoids JPA appointmentId=null issue)
            jdbcTemplate.update(
                "INSERT INTO appointment " +
                "(patient_id, doctor_id, appointmentId, todayTokenNo, " +
                " doctorId, userId, patientId, " +
                " appointmentDate, appointmentStartTime, appointmentEndTime, " +
                " appointmentStatus, appointmentReason, scanType, " +
                " appointmentTime, appointmentDuration, reason_for_visit, notes, status) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)",
                patientNumericId, doctorNumericId,
                apptId, tokenNo,
                doctorId, userId, patientId,
                date, startTime,
                endTime  != null ? endTime  : "",
                "1", "", // appointmentStatus=Booked, appointmentReason=empty
                scanType != null ? scanType : "",
                startTime, "30",
                "Walk-in appointment",
                scanType != null && !scanType.isBlank() ? "Scan Type: " + scanType : "",
                "SCHEDULED");

            response.put("status",        "success");
            response.put("message",       "Appointment booked successfully");
            response.put("appointmentId", apptId);
            response.put("tokenNo",       tokenNo);
            log.info("Appointment created - ID: {}, Token: {}, Doctor: {}, Patient: {}",
                    apptId, tokenNo, doctorId, patientId);

        } catch (Exception e) {
            log.error("Appointment booking error: {}", e.getMessage(), e);
            response.put("status",  "failed");
            response.put("message", "Failed to book appointment: " + e.getMessage());
        }
        return response;
    }

        // ═══════════════════════════════════════════════════════════════════════════
    // OTHER
    // ═══════════════════════════════════════════════════════════════════════════

    @GetMapping("/admin/whatsappLogs")
    public String whatsappLogs(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        model.addAttribute("logs", adminService.getWhatsappLogs());
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/whatsapp_logs";
    }

    @GetMapping("/admin/appconfig")
    public String appConfig(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        model.addAttribute("appList", adminService.getAppConfigList());
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/app-config";
    }

    @GetMapping("/admin/appConfigDetails")
    public String appConfigDetails(@RequestParam String appId, HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        appConfigRepository.findByAppId(appId).ifPresent(cfg -> {
            model.addAttribute("appConfig", cfg);
            model.addAttribute("versionList", adminService.getAppConfigDetails(appId));
        });
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/app-config-details";
    }

    @GetMapping("/admin/debug/db")
    @ResponseBody
    public String debugDb() {
        StringBuilder sb = new StringBuilder("URL: ")
                .append(adminService.getDatasourceUrlInfo()).append("<br/>");
        adminService.checkConnection().ifPresent(err -> sb.append("Error: ").append(err));
        if (!sb.toString().contains("Error")) sb.append("Connection OK");
        return sb.toString();
    }

    // ═══════════════════════════════════════════════════════════════════════════
    // LOCATION SECTION
    // ═══════════════════════════════════════════════════════════════════════════

    @GetMapping("/admin/locations/locationslist")
    public String locationsList(HttpSession session, Model model) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        try {
            model.addAttribute("locationList", adminService.getLocationList());
        } catch (Exception e) {
            model.addAttribute("locationList", Collections.emptyList());
        }
        model.addAttribute("user_name", session.getAttribute("adminName"));
        model.addAttribute("usertype", "Admin");
        return "admin/locations/locationslist";
    }

    @PostMapping(value = "/adminmodel/addlocation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String addLocation(@RequestParam String locationName, HttpSession session) {
        if (!isLoggedIn(session)) return "{\"status\":\"FAIL\",\"message\":\"Not logged in\"}";
        return toJson(adminService.addLocation(locationName));
    }

    @PostMapping(value = "/adminmodel/updatelocation", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String updateLocation(
            @RequestParam String locationId,
            @RequestParam String locationName,
            HttpSession session) {
        if (!isLoggedIn(session)) return "{\"status\":\"FAIL\",\"message\":\"Not logged in\"}";
        return toJson(adminService.updateLocation(locationId, locationName));
    }

@GetMapping("/adminmodel/deleteDocument/{docId}")
public String deleteDocument(@PathVariable String docId, HttpSession session,
                              @RequestHeader(value="Referer", required=false) String referer) {
    if (!isLoggedIn(session)) return "redirect:/admin/login";
    try {
        patientDocumentRepository.deleteById(Long.parseLong(docId));
    } catch (Exception e) {
        log.error("deleteDocument error: {}", e.getMessage(), e);
    }
    return referer != null ? "redirect:" + referer : "redirect:/admin/appointment/appointmentlist";
}

@GetMapping("/assets/patientDocuments/{fileName}")
@ResponseBody
public org.springframework.http.ResponseEntity<byte[]> serveDocument(
        @PathVariable String fileName) {
    try {
        java.nio.file.Path path = java.nio.file.Paths.get(
                System.getProperty("user.dir") + "/assets/patientDocuments/" + fileName);
        byte[] bytes = java.nio.file.Files.readAllBytes(path);
        String contentType = fileName.endsWith(".pdf") ? "application/pdf" :
                             fileName.endsWith(".png") ? "image/png" :
                             fileName.endsWith(".jpg") || fileName.endsWith(".jpeg") ? "image/jpeg" : "application/octet-stream";
        return org.springframework.http.ResponseEntity.ok()
                .header("Content-Type", contentType)
                .header("Content-Disposition", "inline; filename=\"" + fileName + "\"")
                .body(bytes);
    } catch (Exception e) {
        return org.springframework.http.ResponseEntity.notFound().build();
    }
}


    @GetMapping("/adminmodel/updateLocationStatus/{status}/{locationId}")
    public String updateLocationStatus(@PathVariable String status,
                                       @PathVariable String locationId,
                                       HttpSession session) {
        if (!isLoggedIn(session)) return "redirect:/admin/login";
        adminService.updateLocationStatus(locationId, status);
        return "redirect:/admin/locations/locationslist";
    }

    @PostMapping(value = "/adminmodel/addAppConfig", produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public String addAppConfig(@RequestParam String appId, @RequestParam String title,
        @RequestParam(required = false) String message,
        @RequestParam(required = false, defaultValue = "ACTIVE") String appStatus,
        HttpSession session) {
    if (!isLoggedIn(session)) return "{\"status\":\"FAIL\",\"message\":\"Not logged in\"}";
    return toJson(adminService.addAppConfig(appId, title, message, appStatus));
}

@PostMapping(value = "/adminmodel/updateAppConfig", produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public String updateAppConfig(@RequestParam String appId,
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String appStatus,
        HttpSession session) {
    if (!isLoggedIn(session)) return "{\"status\":\"FAIL\",\"message\":\"Not logged in\"}";
    return toJson(adminService.updateAppConfig(appId, title, message, appStatus));
}

@PostMapping(value = "/adminmodel/addAppConfigVersion", produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public String addAppConfigVersion(@RequestParam String appId,
        @RequestParam(required = false) String versionNo,
        @RequestParam Integer versionCode,
        @RequestParam String appCurrentStatus,
        @RequestParam String title,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String redirectUrl,
        HttpSession session) {
    if (!isLoggedIn(session)) return "{\"status\":\"FAIL\",\"message\":\"Not logged in\"}";
    return toJson(adminService.addAppConfigVersion(appId, versionNo, versionCode, appCurrentStatus, title, message, redirectUrl));
}

@PostMapping(value = "/adminmodel/updateAppConfigVersion", produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public String updateAppConfigVersion(@RequestParam Long detailId,
        @RequestParam Integer versionCode,
        @RequestParam String appCurrentStatus,
        @RequestParam String title,
        @RequestParam(required = false) String message,
        @RequestParam(required = false) String redirectUrl,
        HttpSession session) {
    if (!isLoggedIn(session)) return "{\"status\":\"FAIL\",\"message\":\"Not logged in\"}";
    return toJson(adminService.updateAppConfigVersion(detailId, versionCode, appCurrentStatus, title, message, redirectUrl));
}

@PostMapping(value = "/adminmodel/deleteAppConfigVersion", produces = MediaType.APPLICATION_JSON_VALUE)
@ResponseBody
public String deleteAppConfigVersion(@RequestParam Long detailId, HttpSession session) {
    if (!isLoggedIn(session)) return "{\"status\":\"FAIL\",\"message\":\"Not logged in\"}";
    return toJson(adminService.deleteAppConfigVersion(detailId));
}













    private String toJson(Map<String, Object> map) {
        try { return new ObjectMapper().writeValueAsString(map); }
        catch (Exception e) { return "{\"status\":\"FAIL\"}"; }
    }
}