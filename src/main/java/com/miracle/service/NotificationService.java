package com.miracle.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Notification;
import com.miracle.model.NotificationEntity;
import com.miracle.model.User;
import com.miracle.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    
    @Autowired(required = false)
    private FirebaseMessaging firebaseMessaging;

    @Transactional
    public void sendAppointmentNotification(User user, String title, String message) {
        saveNotification(user, title, message, NotificationEntity.NotificationType.APPOINTMENT_CONFIRMATION);
        sendPushNotification(user, title, message);
    }

    @Transactional
    public void sendAppointmentReminderNotification(User user, String title, String message) {
        saveNotification(user, title, message, NotificationEntity.NotificationType.APPOINTMENT_REMINDER);
        sendPushNotification(user, title, message);
    }

    @Transactional
    private void saveNotification(User user, String title, String message, 
                                  NotificationEntity.NotificationType type) {
        NotificationEntity notification = NotificationEntity.builder()
                .user(user)
                .title(title)
                .message(message)
                .notificationType(type)
                .isRead(false)
                .build();

        notificationRepository.save(notification);
    }

    @Async
    public void sendPushNotification(User user, String title, String message) {
        if (firebaseMessaging == null) {
            log.warn("Firebase Messaging not configured");
            return;
        }

        try {
            // This would need FCM token from user's device
            Notification.builder()
                    .setTitle(title)
                    .setBody(message)
                    .build();

            log.info("Push notification sent to user: {}", user.getUserId());
        } catch (Exception e) {
            log.error("Error sending push notification", e);
        }
    }

    public List<NotificationEntity> getUserNotifications(Long userId) {
        return notificationRepository.findByUserUserIdOrderByCreatedAtDesc(userId);
    }

    public List<NotificationEntity> getUnreadNotifications(Long userId) {
        return notificationRepository.findByUserUserIdAndIsReadFalseOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        });
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        getUnreadNotifications(userId).forEach(notification -> {
            notification.setIsRead(true);
            notificationRepository.save(notification);
        });
    }

    public Map<String, Object> sendTestPushNotification(String fcmToken, String title, String body) {
    Map<String, Object> response = new HashMap<>();
    try {
        if (firebaseMessaging == null) {
            response.put("status", "failed");
            response.put("message", "Firebase not configured");
            return response;
        }
        com.google.firebase.messaging.Message message = com.google.firebase.messaging.Message.builder()
            .setToken(fcmToken)
            .setNotification(com.google.firebase.messaging.Notification.builder()
                .setTitle(title)
                .setBody(body)
                .build())
            .build();
        String result = firebaseMessaging.send(message);
        response.put("status", "success");
        response.put("message", "Notification sent successfully");
        response.put("messageId", result);
    } catch (Exception e) {
        log.error("Test notification error: {}", e.getMessage(), e);
        response.put("status", "failed");
        response.put("message", e.getMessage());
    }
    return response;
}
}
