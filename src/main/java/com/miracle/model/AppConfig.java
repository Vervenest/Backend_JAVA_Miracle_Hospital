package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_config")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppConfig {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;


    @Column(name = "app_id", nullable = false, unique = true)
    private String appId;


    @Column(name = "app_status", nullable = false)
    private String appStatus;


    @Column(nullable = false)
    private String title;


    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;


    @Column(name = "create_datetime", nullable = false)
    private LocalDateTime createDatetime;

    @Column(name = "update_datetime", nullable = false)
    private LocalDateTime updateDatetime;

    @PrePersist
    protected void onCreate() {
        createDatetime = LocalDateTime.now();
        updateDatetime = LocalDateTime.now();
        if (appStatus == null) {
            appStatus = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updateDatetime = LocalDateTime.now();
    }
}
