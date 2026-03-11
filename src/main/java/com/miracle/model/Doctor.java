package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctor")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long doctorId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "doctorId", length = 64)
    private String doctorStringId;

    @Column(name = "token_id", columnDefinition = "TEXT")
    @Builder.Default
    private String tokenId = "";

    @Column(name = "doctorName", length = 150)
    @Builder.Default
    private String doctorName = "";

    @Column(name = "doctorPhone", length = 20)
    @Builder.Default
    private String phoneNumber = "";

    @Column(name = "doctorProfileImg", length = 250)
    @Builder.Default
    private String profileImage = "";

    @Column(name = "doctorSpecialisation", length = 250)
    @Builder.Default
    private String specialization = "";

    @Column(name = "doctorShortDesc", columnDefinition = "TEXT")
    @Builder.Default
    private String shortDescription = "";

    @Column(name = "scanType", length = 255)
    @Builder.Default
    private String scanType = "";

    @Column(name = "doctorStatus", length = 5)
    @Builder.Default
    private String doctorStatus = "1";

    @Column(name = "isAdminDoctor", length = 64)
    @Builder.Default
    private String isAdminDoctor = "NO";

    @Column(name = "otp", length = 10)
    @Builder.Default
    private String otp = "1234";

    @Column(name = "server_mode", length = 50)
    @Builder.Default
    private String serverMode = "PRODUCTION";

    @Column(name = "qualifications", length = 500)
    private String qualifications;

    @Column(name = "locationId", length = 64)
    private String locationId;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(name = "consultation_fee")
    private Double consultationFee;

    @Column(name = "clinic_address", length = 500)
    private String clinicAddress;

    @Column(name = "clinic_phone", length = 20)
    private String clinicPhone;

    @Column(name = "availability_days", length = 100)
    private String availabilityDays;

    @Column(name = "availability_time", length = 100)
    private String availabilityTime;

    @Column(name = "is_verified")
    @Builder.Default
    private Boolean isVerified = false;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;

    @PrePersist
    protected void onCreate() {
        createDatetime = LocalDateTime.now();
        updateDatetime = LocalDateTime.now();
        if (isVerified == null) isVerified = false;
        if (isActive == null) isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updateDatetime = LocalDateTime.now();
    }
}