package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    @Column(name = "userId", length = 50, unique = true)
    private String userStringId;

    @Column(name = "token_id", length = 255)
    @Builder.Default
    private String tokenId = "";

    @Column(name = "otp", length = 10)
    @Builder.Default
    private String otp = "";

    @Column(name = "userName", length = 100)
    private String userName;

    @Column(name = "userPhone", length = 15, unique = true)
    private String userPhone;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "userRole", length = 20)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Column(name = "server_mode", length = 50)
    @Builder.Default
    private String serverMode = "PRODUCTION";

    @Column(name = "user_img", length = 255)
    private String userImg;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (isActive == null) isActive = true;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum UserRole {
        ADMIN, DOCTOR, PATIENT
    }
}