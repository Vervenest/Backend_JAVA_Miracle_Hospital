package com.miracle.controller;

import com.miracle.model.AppConfig;
import com.miracle.model.AppConfigDetail;
import com.miracle.service.AppConfigService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/config")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class ApiController {

    private final AppConfigService appConfigService;

    /**
     * App Config Platform - POST /api/config/appConfigPlatform
     * Checks if app is active and retrieves configuration status
     */
    @PostMapping("/appConfigPlatform")
    public ResponseEntity<Map<String, Object>> appConfigPlatform(
            @RequestParam String applicationId,
            @RequestParam Integer versionCode) {
        log.info("Checking app config for application: {}", applicationId);

        try {
            AppConfig appConfig = appConfigService.getAppConfig(applicationId);
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("appStatus", appConfig.getAppStatus());
            response.put("title", appConfig.getTitle());
            response.put("message", appConfig.getMessage());

            if (appConfig.getAppStatus().toString().equals("STOP")) {
                response.put("status", "failed");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "failed");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * App Config Details - POST /api/config/appConfigDetails
     * Gets app configuration details and update information
     */
    @PostMapping("/appConfigDetails")
    public ResponseEntity<Map<String, Object>> appConfigDetails(
            @RequestParam String applicationId,
            @RequestParam Integer versionCode) {
        log.info("Getting app config details for application: {}", applicationId);

        try {
            List<AppConfigDetail> updates = appConfigService.getAppUpdates(applicationId, versionCode);
            Map<String, Object> response = new HashMap<>();

            if (!updates.isEmpty()) {
                AppConfigDetail detail = updates.get(0);
                response.put("status", "success");
                response.put("appStatus", detail.getAppCurrentStatus());
                response.put("title", detail.getTitle());
                response.put("message", detail.getMessage());
                response.put("redirect", detail.getRedirectUrl());
            } else {
                response.put("status", "success");
                response.put("appStatus", "ACTIVE");
                response.put("message", "No Update Available");
            }

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "failed");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Add App Config Details - POST /api/config/addAppConfigDetails
     * Adds or updates app configuration details
     */
    @PostMapping("/addAppConfigDetails")
    public ResponseEntity<Map<String, Object>> addAppConfigDetails(
            @RequestParam String applicationId,
            @RequestParam String title,
            @RequestParam String message,
            @RequestParam(required = false) String redirectUrl) {
        log.info("Adding app config details for application: {}", applicationId);

        try {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "success");
            response.put("applicationId", applicationId);
            response.put("title", title);
            response.put("message", message);
            response.put("redirectUrl", redirectUrl);
            response.put("message", "App config details added successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("status", "failed");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }
}
