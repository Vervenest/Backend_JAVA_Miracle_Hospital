package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patients")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long patientId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "patientId", length = 50)
    private String patientStringId;

    @Column(name = "patientName", length = 100)
    private String patientName;

    @Column(name = "patientRelation", length = 50)
    private String relation;

    @Column(name = "patientPhone", length = 15)
    private String phoneNumber;

    @Column(name = "patientGender", length = 50)
    @Builder.Default
    private String patientGender = "";

    @Column(name = "patientDateOfBirth", length = 50)
    @Builder.Default
    private String patientDateOfBirth = "";

    @Column(name = "patientAge", length = 10)
    @Builder.Default
    private String patientAge = "";

    @Column(name = "patientStatus", length = 10)
    @Builder.Default
    private String patientStatus = "1";

    @Column(name = "isPregnant", length = 5)
    @Builder.Default
    private String isPregnant = "0";

    @Column(name = "lmpDate", length = 50)
    private String lmpDate;

    @Column(name = "expectedDeliveryDate", length = 50)
    private String expectedDeliveryDate;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "gender", length = 20)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name = "address", length = 500)
    private String address;

    @Column(name = "blood_group", length = 10)
    private String bloodGroup;

    @Column(name = "emergency_contact", length = 20)
    private String emergencyContact;

    @Column(name = "medical_history", columnDefinition = "TEXT")
    private String medicalHistory;

    @Column(name = "allergies", columnDefinition = "TEXT")
    private String allergies;

    @Column(name = "insurance_provider", length = 100)
    private String insuranceProvider;

    @Column(name = "insurance_number", length = 100)
    private String insuranceNumber;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (isActive == null) isActive = true;
    }

    public enum Gender {
        MALE, FEMALE, OTHER
    }
}