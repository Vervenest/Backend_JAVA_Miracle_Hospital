package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "doctor_timeslots", indexes = {
    @Index(name = "idx_doctor_day", columnList = "doctor_id, day_of_week")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorTimeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "day_of_week", nullable = false)
    private String dayOfWeek; // Monday, Tuesday, etc.

    @Column(name = "start_time")
    private String startTime; // HH:mm format

    @Column(name = "end_time")
    private String endTime; // HH:mm format

    @Column(name = "is_leave", nullable = false)
    @Builder.Default
    private Integer isLeave = 0; // 1 = on leave, 0 = available

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isLeave == null) {
            isLeave = 0;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
