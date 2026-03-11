package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "notification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(name = "notification_type")
    @Enumerated(EnumType.STRING)
    private NotificationType notificationType;

    @Column(name = "fcm_token")
    private String fcmToken;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    @Column(name = "related_id")
    private Long relatedId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum NotificationType {
        APPOINTMENT_REMINDER,
        APPOINTMENT_CONFIRMATION,
        APPOINTMENT_CANCELLED,
        PRESCRIPTION_READY,
        TEST_RESULT_AVAILABLE,
        MESSAGE,
        SYSTEM_NOTIFICATION
    }
}