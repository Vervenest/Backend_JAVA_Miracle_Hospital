package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long appointmentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "appointmentId", length = 64)
    private String appointmentStringId;

    @Column(name = "todayTokenNo", length = 5)
    @Builder.Default
    private String todayTokenNo = "";

    @Column(name = "userId", length = 255)
    @Builder.Default
    private String userId = "";

    @Column(name = "appointmentDate", length = 64)
    @Builder.Default
    private String appointmentDate = "";

    @Column(name = "appointmentStartTime", length = 64)
    @Builder.Default
    private String appointmentStartTime = "";

    @Column(name = "appointmentEndTime", length = 64)
    @Builder.Default
    private String appointmentEndTime = "";

    @Column(name = "appointmentStatus", length = 10)
    @Builder.Default
    private String appointmentStatus = "1";

    @Column(name = "delayTime", length = 64)
    @Builder.Default
    private String delayTime = "";

    @Column(name = "appointmentReason", columnDefinition = "MEDIUMTEXT")
    @Builder.Default
    private String appointmentReason = "";

    @Column(name = "scanType", length = 255)
    private String scanType;

    @Column(name = "appointment_date_time")
    private LocalDateTime appointmentDateTime;

    @Column(name = "reason_for_visit", length = 500)
    private String reasonForVisit;

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status;

    @Column(name = "create_datetime")
    private LocalDateTime createdAt;

    @Column(name = "update_datetime")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = AppointmentStatus.SCHEDULED;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum AppointmentStatus {
        SCHEDULED, COMPLETED, CANCELLED, NO_SHOW, RESCHEDULED
    }
}