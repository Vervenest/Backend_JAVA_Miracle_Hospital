package com.miracle.controller;

import com.miracle.dto.AppointmentDto;
import com.miracle.model.Appointment;
import com.miracle.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/appointments")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {

    private final AppointmentService appointmentService;

    /**
     * Create a new appointment with automatic slot validation.
     * The service will check:
     * - Doctor's availability on the selected date
     * - Doctor is not on leave
     * - Time is within doctor's working hours
     * - Time slot is not already booked
     */
    @PostMapping
    public ResponseEntity<Map<String, Object>> createAppointment(@RequestBody AppointmentDto appointmentDto) {
        Map<String, Object> response = new HashMap<>();
        try {
            log.info("Creating new appointment for patient: {}", appointmentDto.getPatientId());
            Appointment appointment = appointmentService.createAppointment(appointmentDto);
            response.put("status", "SUCCESS");
            response.put("message", "Appointment booked successfully");
            response.put("data", appointmentService.convertToDto(appointment));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            log.error("Appointment booking failed: {}", e.getMessage());
            response.put("status", "FAIL");
            response.put("message", e.getMessage());
            response.put("error", "SLOT_NOT_AVAILABLE");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            log.error("Unexpected error while booking appointment", e);
            response.put("status", "ERROR");
            response.put("message", "An unexpected error occurred while booking the appointment");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointmentById(@PathVariable Long appointmentId) {
        log.info("Fetching appointment with ID: {}", appointmentId);
        Appointment appointment = appointmentService.getAppointmentById(appointmentId);
        return ResponseEntity.ok(appointmentService.convertToDto(appointment));
    }

    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDto>> getPatientAppointments(@PathVariable Long patientId) {
        log.info("Fetching appointments for patient: {}", patientId);
        List<Appointment> appointments = appointmentService.getPatientAppointments(patientId);
        return ResponseEntity.ok(appointmentService.convertToDto(appointments));
    }

    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDto>> getDoctorAppointments(@PathVariable Long doctorId) {
        log.info("Fetching appointments for doctor: {}", doctorId);
        List<Appointment> appointments = appointmentService.getDoctorAppointments(doctorId);
        return ResponseEntity.ok(appointmentService.convertToDto(appointments));
    }

    @PutMapping("/{appointmentId}")
    public ResponseEntity<AppointmentDto> updateAppointment(
            @PathVariable Long appointmentId,
            @RequestBody AppointmentDto appointmentDto) {
        log.info("Updating appointment with ID: {}", appointmentId);
        Appointment appointment = appointmentService.updateAppointment(appointmentId, appointmentDto);
        return ResponseEntity.ok(appointmentService.convertToDto(appointment));
    }

    @DeleteMapping("/{appointmentId}")
    public ResponseEntity<String> cancelAppointment(@PathVariable Long appointmentId) {
        log.info("Cancelling appointment with ID: {}", appointmentId);
        appointmentService.cancelAppointment(appointmentId);
        return ResponseEntity.ok("Appointment cancelled successfully");
    }
}
