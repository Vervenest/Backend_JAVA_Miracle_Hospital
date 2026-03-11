package com.miracle.controller;

import com.miracle.dto.PatientDto;
import com.miracle.model.Patient;
import com.miracle.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/patients")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class PatientController {

    private final PatientService patientService;

    // ===== AUTHENTICATION ENDPOINTS =====

    @PostMapping("/patientLogin")
    public ResponseEntity<Map<String, Object>> patientLogin(@RequestParam String mobileNo) {
        log.info("Patient login attempt: {}", mobileNo);
        return ResponseEntity.ok(patientService.patientLogin(mobileNo));
    }

    @PostMapping("/patientValidateOtp")
    public ResponseEntity<Map<String, Object>> validatePatientOtp(
            @RequestParam String mobileNo,
            @RequestParam String otp,
            @RequestParam String fcmId) {
        log.info("Validating OTP for: {}", mobileNo);
        return ResponseEntity.ok(patientService.validatePatientOtp(mobileNo, otp, fcmId));
    }

    @PostMapping("/resendOtp")
    public ResponseEntity<Map<String, Object>> resendOtp(@RequestParam String mobileNo) {
        log.info("Resending OTP for: {}", mobileNo);
        return ResponseEntity.ok(patientService.resendOtp(mobileNo));
    }

    // ===== ALIAS ENDPOINTS — Android calls these paths =====

    /** Android APIInterface calls POST userLogin */
    @PostMapping("/userLogin")
    public ResponseEntity<Map<String, Object>> userLogin(@RequestParam String mobileNo) {
        return ResponseEntity.ok(patientService.patientLogin(mobileNo));
    }

    /** Android APIInterface calls POST userValidateOtp */
    @PostMapping("/userValidateOtp")
    public ResponseEntity<Map<String, Object>> userValidateOtp(
            @RequestParam String mobileNo,
            @RequestParam String otp,
            @RequestParam String fcmId) {
        return ResponseEntity.ok(patientService.validatePatientOtp(mobileNo, otp, fcmId));
    }

    // ===== PROFILE ENDPOINTS =====

    @PostMapping("/setProfile")
    public ResponseEntity<Map<String, Object>> setProfile(
            @RequestParam String patientId,
            @RequestParam String userId,
            @RequestParam String patientName,
            @RequestParam String patientRelation,
            @RequestParam String patientPhone,
            @RequestParam String patientDateOfBirth,
            @RequestParam String patientGender,
            @RequestParam(required = false, defaultValue = "") String patientAge,
            @RequestParam(required = false, defaultValue = "0") String isPregnant,
            @RequestParam(required = false, defaultValue = "") String lmpDate,
            @RequestParam(required = false, defaultValue = "") String expectedDeliveryDate) {
        log.info("Setting profile for patient: {}", patientId);
        return ResponseEntity.ok(patientService.setProfile(patientId, userId, patientName,
                patientRelation, patientPhone, patientDateOfBirth, patientGender, patientAge,
                isPregnant, lmpDate, expectedDeliveryDate));
    }

    /**
     * Android sends both patientId and userId — only patientId is used.
     * userId is accepted here so Spring does not reject the request.
     */
   @PostMapping("/getProfile")
public ResponseEntity<Map<String, Object>> getProfile(
        @RequestParam String patientId,
        @RequestParam(required = false, defaultValue = "") String userId) {
    return ResponseEntity.ok(patientService.getProfile(patientId, userId));
}

    @PostMapping("/addPatientUsers")
    public ResponseEntity<Map<String, Object>> addPatientUsers(
            @RequestParam String userId,
            @RequestParam String patientName,
            @RequestParam String patientRelation,
            @RequestParam String patientPhone,
            @RequestParam String patientDateOfBirth,
            @RequestParam String patientGender,
            @RequestParam(required = false, defaultValue = "") String patientAge,
            @RequestParam(required = false, defaultValue = "0") String isPregnant,
            @RequestParam(required = false, defaultValue = "") String lmpDate,
            @RequestParam(required = false, defaultValue = "") String expectedDeliveryDate) {
        log.info("Adding patient user for userId: {}", userId);
        return ResponseEntity.ok(patientService.addPatientUser(userId, patientName, patientRelation,
                patientPhone, patientDateOfBirth, patientGender, patientAge,
                isPregnant, lmpDate, expectedDeliveryDate));
    }

    @PostMapping("/getPatientsList")
    public ResponseEntity<Map<String, Object>> getPatientsList(@RequestParam String userId) {
        log.info("Getting patients list for userId: {}", userId);
        return ResponseEntity.ok(patientService.getPatientsList(userId));
    }

    // ===== DOCTOR ENDPOINTS =====

    @GetMapping("/getDoctorList")
    public ResponseEntity<Map<String, Object>> getDoctorList() {
        log.info("Fetching doctor list");
        return ResponseEntity.ok(patientService.getDoctorList());
    }

    @GetMapping("/getDoctorSpecialisationList")
    public ResponseEntity<Map<String, Object>> getDoctorSpecialisationList() {
        log.info("Fetching doctor specialisation list");
        return ResponseEntity.ok(patientService.getDoctorSpecialisationList());
    }

    @PostMapping("/getDoctorDetailsById")
    public ResponseEntity<Map<String, Object>> getDoctorDetailsById(@RequestParam String doctorId) {
        log.info("Fetching doctor details for doctorId: {}", doctorId);
        return ResponseEntity.ok(patientService.getDoctorDetailsById(doctorId));
    }

    @PostMapping("/doctorAvailableSlotTime")
    public ResponseEntity<Map<String, Object>> doctorAvailableSlotTime(
            @RequestParam String doctorId,
            @RequestParam String date) {
        log.info("Fetching available slots for doctorId: {} on date: {}", doctorId, date);
        return ResponseEntity.ok(patientService.doctorAvailableSlotTime(doctorId, date));
    }

    // ===== APPOINTMENT ENDPOINTS =====

    @PostMapping("/addAppointment")
    public ResponseEntity<Map<String, Object>> addAppointment(
            @RequestParam String userId,
            @RequestParam String doctorId,
            @RequestParam String patientId,
            @RequestParam String date,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam(required = false, defaultValue = "") String scanType) {
        log.info("Adding appointment for userId: {}, doctorId: {}, patientId: {}", userId, doctorId, patientId);
        return ResponseEntity.ok(patientService.addAppointment(userId, doctorId, patientId, date, startTime, endTime, scanType));
    }

    @PostMapping("/appointmentHistory")
    public ResponseEntity<Map<String, Object>> appointmentHistory(@RequestParam String userId) {
        log.info("Getting appointment history for userId: {}", userId);
        return ResponseEntity.ok(patientService.getAppointmentHistory(userId));
    }

    /**
     * Android lateNoteAsync calls this endpoint.
     * Sends: appointmentId, status ("3"=cancel, "4"=late), delayTime, doctorId, userId, patientId.
     * Only appointmentId + status are persisted; delayTime is accepted but unused.
     */
    @PostMapping("/updateAppointmentStatusById")
    public ResponseEntity<Map<String, Object>> updateAppointmentStatusById(
            @RequestParam String appointmentId,
            @RequestParam String status,
            @RequestParam(required = false, defaultValue = "") String delayTime,
            @RequestParam(required = false, defaultValue = "") String doctorId,
            @RequestParam(required = false, defaultValue = "") String userId,
            @RequestParam(required = false, defaultValue = "") String patientId) {
        log.info("Updating appointment status: {} to {}", appointmentId, status);
        return ResponseEntity.ok(patientService.updateAppointmentStatus(appointmentId, status));
    }

    // ===== NOTIFICATION ENDPOINTS =====

    /**
     * Android sends: userId, patientId.
     * patientId is accepted but unused (notifications are fetched by userId).
     */
    @PostMapping("/notificationList")
    public ResponseEntity<Map<String, Object>> notificationList(
            @RequestParam String userId,
            @RequestParam(required = false, defaultValue = "") String patientId) {
        log.info("Getting notification list for userId: {}", userId);
        return ResponseEntity.ok(patientService.getNotificationList(userId));
    }

    // ===== CHAT ENDPOINTS =====

    @PostMapping("/chatPatientSendMessage")
    public ResponseEntity<Map<String, Object>> chatPatientSendMessage(
            @RequestParam String userId,
            @RequestParam String patientId,
            @RequestParam String message,
            @RequestParam(required = false, defaultValue = "") String doctorId,
            @RequestParam(required = false, defaultValue = "") String doctorName) {
        log.info("Patient sending chat message, userId: {}", userId);
        return ResponseEntity.ok(patientService.chatPatientSendMessage(userId, patientId, message, doctorId, doctorName));
    }

@PostMapping("/getChatPatientMessage")
public ResponseEntity<Map<String, Object>> getChatPatientMessage(
        @RequestParam String userId,
        @RequestParam(required = false, defaultValue = "") String doctorId) {
    return ResponseEntity.ok(patientService.getChatPatientMessage(userId, doctorId));
}

    // ===== QUEUE ENDPOINTS =====

    @PostMapping("/getPatientQueueStatus")
    public ResponseEntity<Map<String, Object>> getPatientQueueStatus(
            @RequestParam String patientId,
            @RequestParam String doctorId) {
        log.info("Getting queue status for patientId: {}, doctorId: {}", patientId, doctorId);
        return ResponseEntity.ok(patientService.getPatientQueueStatus(patientId, doctorId));
    }

    // ===== LOCATION ENDPOINTS =====

    @GetMapping("/getLocationList")
    public ResponseEntity<Map<String, Object>> getLocationList() {
        log.info("Fetching location list");
        return ResponseEntity.ok(patientService.getLocationList());
    }

    @PostMapping("/getDoctorsByLocation")
    public ResponseEntity<Map<String, Object>> getDoctorsByLocation(@RequestParam String locationId) {
        log.info("Fetching doctors for locationId: {}", locationId);
        return ResponseEntity.ok(patientService.getDoctorsByLocation(locationId));
    }

    // ===== DELETE =====

    @PostMapping("/deleteAccountPermanently")
    public ResponseEntity<Map<String, Object>> deleteAccountPermanently(
            @RequestParam String patientId) {
        log.info("Deleting account permanently for patient: {}", patientId);
        return ResponseEntity.ok(patientService.deleteAccountPermanently(patientId));
    }

    // ===== BASIC CRUD (internal use) =====

    @GetMapping("/{patientId}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long patientId) {
        Patient patient = patientService.getPatientById(patientId);
        return ResponseEntity.ok(patientService.convertToDto(patient));
    }

    @PostMapping
    public ResponseEntity<PatientDto> createPatient(@RequestBody PatientDto patientDto) {
        Patient patient = patientService.createPatient(patientDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.convertToDto(patient));
    }

    @PutMapping("/{patientId}")
    public ResponseEntity<PatientDto> updatePatient(
            @PathVariable Long patientId,
            @RequestBody PatientDto patientDto) {
        Patient patient = patientService.updatePatient(patientId, patientDto);
        return ResponseEntity.ok(patientService.convertToDto(patient));
    }

    @DeleteMapping("/{patientId}")
    public ResponseEntity<String> deletePatient(@PathVariable Long patientId) {
        patientService.deletePatient(patientId);
        return ResponseEntity.ok("Patient deleted successfully");
    }

    @PostMapping("/getPatientDetailsbyId")
public ResponseEntity<Map<String, Object>> getPatientDetailsById(
        @RequestParam String patientId,
        @RequestParam(required = false, defaultValue = "") String userId) {
    log.info("Getting patient details for patientId: {}", patientId);
    return ResponseEntity.ok(patientService.getProfile(patientId, userId));
}

@PostMapping("/getPregnancyEvents")
public ResponseEntity<Map<String, Object>> getPregnancyEvents(
        @RequestParam(required = false, defaultValue = "") String patientId) {
    log.info("Getting pregnancy events for patientId: {}", patientId);
    return ResponseEntity.ok(patientService.getPregnancyEvents(patientId));
}
@PostMapping("/getPatientsListWithDocuments")
public ResponseEntity<Map<String, Object>> getPatientsListWithDocuments(@RequestParam String userId) {
    return ResponseEntity.ok(patientService.getPatientsListWithDocuments(userId));
  
}
}