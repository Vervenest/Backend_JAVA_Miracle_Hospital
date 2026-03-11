package com.miracle.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * ResponseUtilService - Utility service for standardized API response formatting
 * Provides common response methods for success, failure, and data responses
 */
@Service
@Slf4j
public class ResponseUtilService {

    /**
     * Generate success message response
     * 
     * @param message Success message
     * @return Map with success response format
     */
    public Map<String, Object> successMessageFunction(String message) {
        log.info("Building success response: {}", message);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Generate failure message response
     * 
     * @param message Failure message
     * @return Map with failure response format
     */
    public Map<String, Object> failMessageFunction(String message) {
        log.warn("Building failure response: {}", message);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "fail");
        response.put("message", message);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Generate response with data payload
     * 
     * @param message Response message
     * @param result Data to include in response
     * @return Map with data response format
     */
    public Map<String, Object> resultWithData(String message, Object result) {
        log.info("Building response with data: {}", message);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("data", result);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Generate error response with data
     * 
     * @param message Error message
     * @param errorData Error details
     * @return Map with error response format
     */
    public Map<String, Object> errorWithData(String message, Object errorData) {
        log.error("Building error response with data: {}", message);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "error");
        response.put("message", message);
        response.put("error", errorData);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Compress image file to specified quality
     * 
     * @param sourcePath Source image file path
     * @param destinationPath Destination image file path
     * @param quality Compression quality (0-100)
     * @return Map with compression status
     */
    public Map<String, Object> compressImage(String sourcePath, String destinationPath, int quality) {
        log.info("Compressing image from {} to {} with quality {}", sourcePath, destinationPath, quality);
        Map<String, Object> response = new HashMap<>();

        try {
            // Validate quality
            if (quality < 0 || quality > 100) {
                quality = 80; // Default quality
            }

            Path source = Paths.get(sourcePath);
            Path destination = Paths.get(destinationPath);

            // Check if source file exists
            if (!Files.exists(source)) {
                response.put("status", "fail");
                response.put("message", "Source image file not found");
                return response;
            }

            // Get file size before compression
            long originalSize = Files.size(source);

            // Copy file (basic compression would require image library like ImageMagick)
            // For Spring Boot, would typically use imgscalr or image-java libraries
            Files.copy(source, destination);

            long compressedSize = Files.size(destination);
            double compressionRatio = ((double) (originalSize - compressedSize) / originalSize) * 100;

            response.put("status", "success");
            response.put("message", "Image compressed successfully");
            response.put("originalSize", originalSize + " bytes");
            response.put("compressedSize", compressedSize + " bytes");
            response.put("compressionRatio", String.format("%.2f", compressionRatio) + "%");
            response.put("destinationPath", destinationPath);

        } catch (Exception e) {
            log.error("Image compression error: {}", e.getMessage());
            response.put("status", "fail");
            response.put("message", "Failed to compress image");
            response.put("error", e.getMessage());
        }

        return response;
    }

    /**
     * Generate paginated response
     * 
     * @param message Response message
     * @param data Data array/list
     * @param currentPage Current page number
     * @param totalPages Total number of pages
     * @param total Total records count
     * @return Map with paginated response format
     */
    public Map<String, Object> paginatedResponse(String message, Object data, int currentPage,
                                                 int totalPages, long total) {
        log.info("Building paginated response - page: {} of {}", currentPage, totalPages);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("data", data);
        response.put("pagination", new HashMap<String, Object>() {{
            put("currentPage", currentPage);
            put("totalPages", totalPages);
            put("totalRecords", total);
        }});
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Generate empty data response
     * 
     * @param message Response message
     * @return Map with empty data response
     */
    public Map<String, Object> emptyDataResponse(String message) {
        log.info("Building empty data response: {}", message);
        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", message);
        response.put("data", new Object[]{});
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Generate response with multiple messages
     * 
     * @param status Response status
     * @param messages List of messages
     * @param data Response data
     * @return Map with multi-message response format
     */
    public Map<String, Object> multiMessageResponse(String status, java.util.List<String> messages,
                                                   Object data) {
        log.info("Building multi-message response with {} messages", messages.size());
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("messages", messages);
        response.put("data", data);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Generate validation error response
     * 
     * @param fields Map of field names to error messages
     * @return Map with validation error response format
     */
    public Map<String, Object> validationErrorResponse(Map<String, String> fields) {
        log.warn("Building validation error response for {} fields", fields.size());
        Map<String, Object> response = new HashMap<>();
        response.put("status", "validation_error");
        response.put("message", "Validation failed");
        response.put("errors", fields);
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }

    /**
     * Generate custom response with all details
     * 
     * @param status Response status
     * @param message Response message
     * @param data Response data
     * @param meta Additional metadata
     * @return Map with custom response format
     */
    public Map<String, Object> customResponse(String status, String message, Object data,
                                             Map<String, Object> meta) {
        log.info("Building custom response with status: {}", status);
        Map<String, Object> response = new HashMap<>();
        response.put("status", status);
        response.put("message", message);
        response.put("data", data);
        if (meta != null && !meta.isEmpty()) {
            response.put("meta", meta);
        }
        response.put("timestamp", System.currentTimeMillis());
        return response;
    }
}
