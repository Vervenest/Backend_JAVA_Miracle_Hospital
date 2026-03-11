package com.miracle.controller;

import com.miracle.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserNotifications(@PathVariable Long userId) {
        log.info("Fetching notifications for user: {}", userId);
        
        var notifications = notificationService.getUserNotifications(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", notifications);
        response.put("count", notifications.size());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}/unread")
    public ResponseEntity<Map<String, Object>> getUnreadNotifications(@PathVariable Long userId) {
        log.info("Fetching unread notifications for user: {}", userId);
        
        var notifications = notificationService.getUnreadNotifications(userId);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("data", notifications);
        response.put("count", notifications.size());
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("{notificationId}/read")
    public ResponseEntity<Map<String, String>> markAsRead(@PathVariable Long notificationId) {
        log.info("Marking notification as read: {}", notificationId);
        
        notificationService.markAsRead(notificationId);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Notification marked as read");
        
        return ResponseEntity.ok(response);
    }

    @PutMapping("/user/{userId}/read-all")
    public ResponseEntity<Map<String, String>> markAllAsRead(@PathVariable Long userId) {
        log.info("Marking all notifications as read for user: {}", userId);
        
        notificationService.markAllAsRead(userId);
        Map<String, String> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "All notifications marked as read");
        
        return ResponseEntity.ok(response);
    }
}
