package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "doctorAvailableSlotTime",
       uniqueConstraints = @UniqueConstraint(columnNames = {"doctorId", "dayOfWeek"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorAvailableSlotTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "doctorId", nullable = false, length = 64)
    private String doctorId;

    @Column(name = "dayOfWeek", nullable = false)
    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    @Column(name = "startTime")
    private LocalTime startTime;

    @Column(name = "endTime")
    private LocalTime endTime;

    @Column(name = "isLeave")
    @Builder.Default
    private Boolean isLeave = false;

    @Column(name = "remarks", length = 255)
    private String remarks;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isLeave == null) isLeave = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum DayOfWeek {
        Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday
    }
}