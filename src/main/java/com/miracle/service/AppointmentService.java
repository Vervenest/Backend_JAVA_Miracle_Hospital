package com.miracle.service;

import com.miracle.dto.AppointmentDto;
import com.miracle.model.Appointment;
import com.miracle.model.Doctor;
import com.miracle.model.Patient;
import com.miracle.model.DoctorAvailableSlotTime;
import com.miracle.repository.AppointmentRepository;
import com.miracle.repository.DoctorRepository;
import com.miracle.repository.PatientRepository;
import com.miracle.repository.DoctorAvailableSlotTimeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final NotificationService notificationService;
    private final DoctorAvailableSlotTimeRepository slotRepository;

    @Transactional
    public Appointment createAppointment(AppointmentDto appointmentDto) {
        Patient patient = patientRepository.findById(appointmentDto.getPatientId())
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        Doctor doctor = doctorRepository.findById(appointmentDto.getDoctorId())
                .orElseThrow(() -> new RuntimeException("Doctor not found"));

        // ═════════════════════════════════════════════════════════════════════
        // VALIDATE SLOT AVAILABILITY BEFORE BOOKING
        // ═════════════════════════════════════════════════════════════════════
        validateSlotAvailability(doctor, appointmentDto.getAppointmentDateTime());

        Appointment appointment = Appointment.builder()
                .patient(patient)
                .doctor(doctor)
                .appointmentDateTime(appointmentDto.getAppointmentDateTime())
                .reasonForVisit(appointmentDto.getReasonForVisit())
                .notes(appointmentDto.getNotes())
                .build();

        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Send notification to patient
        notificationService.sendAppointmentNotification(
                patient.getUser(),
                "Appointment Confirmed",
                "Your appointment with Dr. " + doctor.getUser().getUserName() + 
                " has been scheduled for " + appointmentDto.getAppointmentDateTime()
        );

        return savedAppointment;
    }

    // ═════════════════════════════════════════════════════════════════════════
    // SLOT VALIDATION LOGIC
    // ═════════════════════════════════════════════════════════════════════════
    
    /**
     * Validates that the appointment time slot is available for the doctor.
     * Throws RuntimeException if slot is not available.
     */
    private void validateSlotAvailability(Doctor doctor, LocalDateTime appointmentDateTime) {
        if (appointmentDateTime == null) {
            throw new RuntimeException("Appointment date and time cannot be null");
        }

        LocalDate appointmentDate = appointmentDateTime.toLocalDate();
        LocalTime appointmentTime = appointmentDateTime.toLocalTime();

        // Step 1: Get the day of week
        String dayName = appointmentDate.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, Locale.ENGLISH);

        log.info("Validating slot for Doctor: {}, Date: {}, Day: {}, Time: {}",
                doctor.getDoctorId(), appointmentDate, dayName, appointmentTime);

        try {
            DoctorAvailableSlotTime.DayOfWeek dayOfWeek = 
                    DoctorAvailableSlotTime.DayOfWeek.valueOf(dayName);

            // Step 2: Find the doctor's slot for that day
            var slotOpt = slotRepository.findByDoctorIdAndDayOfWeek(
                    doctor.getDoctorStringId(), dayOfWeek);

            if (slotOpt.isEmpty()) {
                log.warn("NO SLOT FOUND for doctor {} on {}", doctor.getDoctorId(), dayName);
                throw new RuntimeException(
                        "Doctor is not available on " + dayName + ". No slots configured.");
            }

            DoctorAvailableSlotTime slot = slotOpt.get();

            // Step 3: Check if doctor is on leave
            if (slot.getIsLeave() != null && slot.getIsLeave()) {
                log.warn("DOCTOR ON LEAVE: {} on {}", doctor.getDoctorId(), dayName);
                throw new RuntimeException(
                        "Doctor is on leave on " + dayName + ". Please choose another date.");
            }

            // Step 4: Check if slot has valid start and end times
            if (slot.getStartTime() == null || slot.getEndTime() == null) {
                log.warn("INVALID SLOT TIMES for doctor {} on {}", doctor.getDoctorId(), dayName);
                throw new RuntimeException(
                        "Doctor's slot timings are not configured for " + dayName);
            }

            // Step 5: Verify appointment time is within doctor's working hours
            if (appointmentTime.isBefore(slot.getStartTime()) || 
                appointmentTime.isAfter(slot.getEndTime())) {
                log.warn("APPOINTMENT TIME OUT OF BOUNDS: {} - Doctor works {} to {}",
                        appointmentTime, slot.getStartTime(), slot.getEndTime());
                throw new RuntimeException(
                        "Appointment time is outside doctor's working hours. " +
                        "Doctor is available from " + slot.getStartTime() + 
                        " to " + slot.getEndTime());
            }

            // Step 6: Check if the time slot is already booked
            if (isTimeSlotBooked(doctor.getDoctorId(), appointmentDateTime)) {
                log.warn("TIME SLOT ALREADY BOOKED for doctor {} at {}", 
                        doctor.getDoctorId(), appointmentDateTime);
                throw new RuntimeException(
                        "This time slot is already booked. Please select another time.");
            }

            log.info("✓ SLOT VALIDATION PASSED for doctor {} on {} at {}",
                    doctor.getDoctorId(), appointmentDate, appointmentTime);

        } catch (IllegalArgumentException e) {
            log.error("Invalid day of week: {}", dayName, e);
            throw new RuntimeException("Invalid date provided");
        }
    }

    /**
     * Checks if the time slot is already booked by another appointment.
     */
    private boolean isTimeSlotBooked(Long doctorId, LocalDateTime appointmentDateTime) {
        // Get all appointments for this doctor on the same date
        String appointmentDate = appointmentDateTime.toLocalDate().toString();
        List<Appointment> existingAppointments = appointmentRepository
                .findByDoctorDoctorId(doctorId)
                .stream()
                .filter(apt -> apt.getAppointmentDateTime() != null &&
                        apt.getAppointmentDateTime().toLocalDate()
                           .equals(appointmentDateTime.toLocalDate()) &&
                        apt.getStatus() != Appointment.AppointmentStatus.CANCELLED)
                .collect(Collectors.toList());

        // Check if any appointment has overlapping time
        LocalTime appointmentTimeStart = appointmentDateTime.toLocalTime();
        LocalTime appointmentTimeEnd = appointmentTimeStart.plusMinutes(30); // Assuming 30-min slots

        for (Appointment apt : existingAppointments) {
            if (apt.getAppointmentDateTime() != null) {
                LocalTime existingStart = apt.getAppointmentDateTime().toLocalTime();
                LocalTime existingEnd = existingStart.plusMinutes(30);

                // Check for overlap
                if (!(appointmentTimeEnd.isBefore(existingStart) || 
                      appointmentTimeStart.isAfter(existingEnd))) {
                    log.warn("TIME CONFLICT: New slot {} overlaps with existing {}",
                            appointmentTimeStart, existingStart);
                    return true;
                }
            }
        }

        return false;
    }

    public Appointment getAppointmentById(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientPatientId(patientId);
    }

    public List<Appointment> getDoctorAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorDoctorId(doctorId);
    }

    @Transactional
    public Appointment updateAppointment(Long appointmentId, AppointmentDto appointmentDto) {
        Appointment appointment = getAppointmentById(appointmentId);

        appointment.setAppointmentDateTime(appointmentDto.getAppointmentDateTime());
        appointment.setReasonForVisit(appointmentDto.getReasonForVisit());
        appointment.setNotes(appointmentDto.getNotes());
        appointment.setStatus(Appointment.AppointmentStatus.valueOf(appointmentDto.getStatus()));

        return appointmentRepository.save(appointment);
    }

    @Transactional
    public void cancelAppointment(Long appointmentId) {
        Appointment appointment = getAppointmentById(appointmentId);
        appointment.setStatus(Appointment.AppointmentStatus.CANCELLED);
        appointmentRepository.save(appointment);

        // Send notification to patient
        notificationService.sendAppointmentNotification(
                appointment.getPatient().getUser(),
                "Appointment Cancelled",
                "Your appointment has been cancelled"
        );
    }

    public List<AppointmentDto> convertToDto(List<Appointment> appointments) {
        return appointments.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public AppointmentDto convertToDto(Appointment appointment) {
        return AppointmentDto.builder()
                .appointmentId(appointment.getAppointmentId())
                .patientId(appointment.getPatient().getPatientId())
                .patientName(appointment.getPatient().getUser().getUserName())
                .doctorId(appointment.getDoctor().getDoctorId())
                .doctorName(appointment.getDoctor().getUser().getUserName())
                .doctorSpecialization(appointment.getDoctor().getSpecialization())
                .appointmentDateTime(appointment.getAppointmentDateTime())
                .reasonForVisit(appointment.getReasonForVisit())
                .status(appointment.getStatus().toString())
                .notes(appointment.getNotes())
                .build();
    }
}
