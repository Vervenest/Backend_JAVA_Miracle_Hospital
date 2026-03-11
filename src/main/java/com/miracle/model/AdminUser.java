package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "adminusers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdminUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "adminId", nullable = false, length = 64)
    private String adminId;

    @Column(name = "adminName", nullable = false, length = 250)
    private String adminName;

    @Column(name = "adminEmail", nullable = false, length = 100)
    private String adminEmail;

    @Column(name = "adminPassword", nullable = false, length = 100)
    private String adminPassword;

    @Column(name = "adminStatus", length = 5)
    private String adminStatus = "1";

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