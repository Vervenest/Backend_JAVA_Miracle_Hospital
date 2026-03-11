package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "app_config_detail")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppConfigDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "detail_id")
    private Long detailId;

    @ManyToOne
    @JoinColumn(name = "app_id", referencedColumnName = "app_id", nullable = false)
    private AppConfig appConfig;

    @Column(name = "version_no")
    private String versionNo;

    @Column(name = "version_code", nullable = false)
    private Integer versionCode;

    @Column(name = "app_current_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AppStatus appCurrentStatus;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "redirect_url")
    private String redirectUrl;

    @Column(name = "delete_status")
    @Builder.Default
    private Integer deleteStatus = 0;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum AppStatus {
        ACTIVE,
        MANDATORY,
        MAINTENANCE,
        DEPRECATED,
        FORCED_UPDATE
    }
}