package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctorspecialisation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorSpecialisation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "specialisationId", nullable = false, length = 64)
    private String specialisationId;

    @Column(name = "specialisationName", nullable = false, length = 150)
    private String specialisationName;

    @Column(name = "specialisationStatus", length = 5)
    @Builder.Default
    private String specialisationStatus = "1";

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;

    @PrePersist
    protected void onCreate() {
        createDatetime = LocalDateTime.now();
        updateDatetime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDatetime = LocalDateTime.now();
    }
}