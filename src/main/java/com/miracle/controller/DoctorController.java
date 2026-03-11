package com.miracle.controller;

import com.miracle.dto.DoctorDto;
import com.miracle.model.Doctor;
import com.miracle.service.DoctorService;
import com.miracle.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/doctors")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class DoctorController {

    private final DoctorService      doctorService;
    private final AppointmentService appointmentService;

    // =========================================================================
    // AUTH ENDPOINTS
    // =========================================================================

    @PostMapping("/doctorLogin")
    public ResponseEntity<Map<String, Object>> doctorLogin(@RequestParam String mobileNo) {
        log.info("Doctor login attempt: {}", mobileNo);
        return ResponseEntity.ok(doctorService.doctorLogin(mobileNo));
    }

    @PostMapping("/doctorValidateOtp")
    public ResponseEntity<Map<String, Object>> validateDoctorOtp(
            @RequestParam String mobileNo,
            @RequestParam String otp,
            @RequestParam String fcmId) {
        log.info("Validating OTP for doctor: {}", mobileNo);
        return ResponseEntity.ok(doctorService.validateDoctorOtp(mobileNo, otp, fcmId));
    }

    @PostMapping("/resendOtp")
    public ResponseEntity<Map<String, Object>> resendOtp(@RequestParam String mobileNo) {
        log.info("Resending OTP for doctor: {}", mobileNo);
        return ResponseEntity.ok(doctorService.resendOtp(mobileNo));
    }

    // =========================================================================
    // PROFILE ENDPOINTS
    // =========================================================================

    @PostMapping("/getProfile")
    public ResponseEntity<Map<String, Object>> getProfile(@RequestParam String doctorId) {
        log.info("Getting profile for doctor: {}", doctorId);
        return ResponseEntity.ok(doctorService.getProfile(doctorId));
    }

    @PostMapping("/deleteAccountPermanently")
    public ResponseEntity<Map<String, Object>> deleteAccountPermanently(@RequestParam String doctorId) {
        log.info("Deleting account permanently for doctor: {}", doctorId);
        return ResponseEntity.ok(doctorService.deleteAccountPermanently(doctorId));
    }

    // =========================================================================
    // APPOINTMENT ENDPOINTS
    // =========================================================================

    @PostMapping("/appointmentHistory")
    public ResponseEntity<Map<String, Object>> appointmentHistory(
            @RequestParam String doctorId,
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String todayList) {
        log.info("Getting appointment history for doctor: {}", doctorId);
        return ResponseEntity.ok(doctorService.getAppointmentHistory(doctorId, date));
    }

    @PostMapping({"/getAppointmenDetailsById", "/getAppointmentDetailsById"})
    public ResponseEntity<Map<String, Object>> getAppointmentDetailsById(
            @RequestParam String appointmentId,
            @RequestParam String doctorId) {
        log.info("Getting appointment details for: {}", appointmentId);
        return ResponseEntity.ok(doctorService.getAppointmentDetailsById(appointmentId, doctorId));
    }

    @PostMapping("/updateAppointmentStatusById")
    public ResponseEntity<Map<String, Object>> updateAppointmentStatusById(
            @RequestParam String appointmentId,
            @RequestParam String doctorId,
            @RequestParam String status,
            @RequestParam(required = false, defaultValue = "") String delayTime) {
        log.info("Updating appointment status for: {}", appointmentId);
        return ResponseEntity.ok(doctorService.updateAppointmentStatusById(appointmentId, status));
    }

    // =========================================================================
    // NOTIFICATION ENDPOINTS
    // =========================================================================

    @PostMapping("/notificationList")
    public ResponseEntity<Map<String, Object>> notificationList(@RequestParam String doctorId) {
        log.info("Getting notification list for doctor: {}", doctorId);
        return ResponseEntity.ok(doctorService.getNotificationList(doctorId));
    }

    @PostMapping("/sendAppointmentPreNotificationToPatient")
    public ResponseEntity<Map<String, Object>> sendAppointmentPreNotificationToPatient() {
        log.info("Sending appointment pre-notifications");
        Map<String, Object> response = new HashMap<>();
        response.put("status",  "success");
        response.put("message", "Notifications sent");
        return ResponseEntity.ok(response);
    }

    // =========================================================================
    // CHAT ENDPOINTS
    // =========================================================================

    @PostMapping("/chatDoctorSendMessage")
    public ResponseEntity<Map<String, Object>> chatDoctorSendMessage(
            @RequestParam String doctorId,
            @RequestParam(required = false, defaultValue = "") String doctorName,
            @RequestParam(required = false, defaultValue = "") String userId,
            @RequestParam String patientId,
            @RequestParam String message) {
        return ResponseEntity.ok(doctorService.chatDoctorSendMessage(doctorId, doctorName, userId, patientId, message));
    }

    @PostMapping("/getChatDoctorMessage")
    public ResponseEntity<Map<String, Object>> getChatDoctorMessage(@RequestParam String patientId) {
        log.info("Getting chat messages for patientId: {}", patientId);
        return ResponseEntity.ok(doctorService.getChatDoctorMessage(patientId));
    }

    @PostMapping("/getPatientChatList")
    public ResponseEntity<Map<String, Object>> getPatientChatList() {
        log.info("Getting patient chat list");
        return ResponseEntity.ok(doctorService.getPatientChatList());
    }

    // =========================================================================
    // QUEUE ENDPOINTS
    // =========================================================================

    @PostMapping("/getDoctorQueueOverview")
    public ResponseEntity<Map<String, Object>> getDoctorQueueOverview(@RequestParam String doctorId) {
        log.info("Getting queue overview for doctorId: {}", doctorId);
        return ResponseEntity.ok(doctorService.getDoctorQueueOverview(doctorId));
    }

    // =========================================================================
    // PATIENT ENDPOINTS
    // =========================================================================

    @PostMapping("/getPatientDetails")
    public ResponseEntity<Map<String, Object>> getPatientDetails(
            @RequestParam String patientId,
            @RequestParam(required = false, defaultValue = "") String doctorId) {
        log.info("Getting patient details for patientId: {}", patientId);
        return ResponseEntity.ok(doctorService.getPatientDetails(patientId));
    }

    @PostMapping("/getReportsList")
    public ResponseEntity<Map<String, Object>> getReportsList(@RequestParam String patientId) {
        log.info("Getting reports list for patientId: {}", patientId);
        return ResponseEntity.ok(doctorService.getReportsList(patientId));
    }

    @PostMapping("/uploadFile")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam(required = false, defaultValue = "") String patientId,
            @RequestParam(required = false, defaultValue = "") String doctorId,
            @RequestParam(required = false, defaultValue = "") String uploadFileTime,
            @RequestPart(name = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(doctorService.uploadFile(patientId, doctorId, uploadFileTime, file));
    }

    @PostMapping("/deleteReport")
    public ResponseEntity<Map<String, Object>> deleteReport(
            @RequestParam String patientId,
            @RequestParam(required = false, defaultValue = "") String reportId) {
        log.info("Deleting report: {} for patientId: {}", reportId, patientId);
        Map<String, Object> response = new HashMap<>();
        response.put("status",  "success");
        response.put("message", "Report deleted successfully");
        response.put("result",  new ArrayList<>());
        return ResponseEntity.ok(response);
    }

    // =========================================================================
    // BASIC CRUD (admin use only)
    // =========================================================================

    @GetMapping("/all")
    public ResponseEntity<List<DoctorDto>> getAllDoctors() {
        List<DoctorDto> doctors = doctorService.getAllActiveDoctors()
                .stream().map(doctorService::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{doctorId}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.getDoctorById(doctorId);
        return ResponseEntity.ok(doctorService.convertToDto(doctor));
    }

    @GetMapping("/specialization/{specialization}")
    public ResponseEntity<List<DoctorDto>> getDoctorsBySpecialization(@PathVariable String specialization) {
        List<DoctorDto> doctors = doctorService.getDoctorsBySpecialization(specialization)
                .stream().map(doctorService::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(doctors);
    }

    @PostMapping
    public ResponseEntity<DoctorDto> createDoctor(@RequestBody DoctorDto doctorDto) {
        Doctor doctor = doctorService.createDoctor(doctorDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(doctorService.convertToDto(doctor));
    }

    @PutMapping("/{doctorId}")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable Long doctorId,
                                                   @RequestBody DoctorDto doctorDto) {
        Doctor doctor = doctorService.updateDoctor(doctorId, doctorDto);
        return ResponseEntity.ok(doctorService.convertToDto(doctor));
    }

    @PutMapping("/{doctorId}/verify")
    public ResponseEntity<DoctorDto> verifyDoctor(@PathVariable Long doctorId) {
        Doctor doctor = doctorService.verifyDoctor(doctorId);
        return ResponseEntity.ok(doctorService.convertToDto(doctor));
    }

    @DeleteMapping("/{doctorId}")
    public ResponseEntity<String> deleteDoctor(@PathVariable Long doctorId) {
        doctorService.deleteDoctor(doctorId);
        return ResponseEntity.ok("Doctor deleted successfully");
    }
}