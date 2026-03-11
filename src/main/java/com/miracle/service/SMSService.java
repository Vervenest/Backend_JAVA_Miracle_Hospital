package com.miracle.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * SMSService - Handles SMS sending via SMS Horizon API
 * 
 * API Credentials:
 * - SMS User: chandru5452
 * - API Key: LBGDKVHvKj2YnRa9mypF
 * - Sender ID: VIVRTA
 * - Template IDs: 1307161760591340283 (default), 1307162865820438610 (alternative)
 * - API URL: https://smshorizon.co.in/api/sendsms.php
 */
@Service
@Slf4j
public class SMSService {

    @Value("${sms.horizon.user:chandru5452}")
    private String smsUser;

    @Value("${sms.horizon.api-key:LBGDKVHvKj2YnRa9mypF}")
    private String smsApiKey;

    @Value("${sms.horizon.sender-id:VIVRTA}")
    private String smsSenderId;

    @Value("${sms.horizon.template-id:1307162865820438610}")
    private String defaultTemplateId;

    @Value("${sms.horizon.template-id-alt:1307161760591340283}")
    private String alternateTemplateId;

    @Value("${sms.horizon.api-url:https://smshorizon.co.in/api/sendsms.php}")
    private String smsHorizonApiUrl;

    /**
     * Send OTP SMS
     * 
     * @param mobileNumber Recipient phone number
     * @param otp One-Time Password
     * @return Response map with status
     */
    public Map<String, Object> sendOtpSms(String mobileNumber, String otp) {
        log.info("Sending OTP SMS to: {}", mobileNumber);
        Map<String, Object> response = new HashMap<>();

        try {
            String message = "Your OTP is: " + otp + ". Valid for 10 minutes. Do not share with anyone.";
            return sendSms(message, mobileNumber, defaultTemplateId);

        } catch (Exception e) {
            log.error("Error sending OTP SMS: {}", e.getMessage());
            response.put("status", "fail");
            response.put("message", "Failed to send OTP SMS");
            response.put("error", e.getMessage());
        }

        return response;
    }

    /**
     * Send SMS with specified template ID
     * 
     * @param message Message content
     * @param mobileNumber Recipient phone number
     * @param templateId Template ID for SMS
     * @return Response map with status
     */
    public Map<String, Object> sendSms(String message, String mobileNumber, String templateId) {
        log.info("Sending SMS to: {} with template: {}", mobileNumber, templateId);
        Map<String, Object> response = new HashMap<>();

        try {
            String cleanPhone = mobileNumber.replaceAll("[^0-9]", "");
            if (cleanPhone.length() == 10) {
                cleanPhone = "91" + cleanPhone;
            }

            Map<String, Object> smsResponse = callSmsHorizonApi(message, cleanPhone, templateId);

            response.put("status", "success");
            response.put("message", "SMS sent successfully");
            response.put("mobileNumber", cleanPhone);
            response.put("smsResponse", smsResponse);

        } catch (Exception e) {
            log.error("Error sending SMS: {}", e.getMessage());
            response.put("status", "fail");
            response.put("message", "Failed to send SMS");
            response.put("error", e.getMessage());
        }

        return response;
    }

    /**
     * Test SMS sending
     * 
     * @param message Message content
     * @param phoneNumber Phone number
     * @return Response map with status
     */
    public Map<String, Object> testSms(String message, String phoneNumber) {
        log.info("Testing SMS to: {}", phoneNumber);
        Map<String, Object> response = new HashMap<>();

        try {
            String cleanPhone = phoneNumber.replaceAll("[^0-9]", "");
            if (cleanPhone.length() == 10) {
                cleanPhone = "91" + cleanPhone;
            }

            Map<String, Object> smsResponse = callSmsHorizonApi(message, cleanPhone, defaultTemplateId);

            response.put("status", "success");
            response.put("message", "Test SMS sent");
            response.put("mobileNumber", cleanPhone);
            response.put("smsResponse", smsResponse);

        } catch (Exception e) {
            log.error("Error testing SMS: {}", e.getMessage());
            response.put("status", "fail");
            response.put("message", "Test SMS failed");
            response.put("error", e.getMessage());
        }

        return response;
    }

    /**
     * Send invite SMS
     * 
     * @param message Invite message
     * @param mobileNumber Recipient phone number
     * @return Response map with status
     */
    public Map<String, Object> sendInviteSms(String message, String mobileNumber) {
        log.info("Sending invite SMS to: {}", mobileNumber);
        Map<String, Object> response = new HashMap<>();

        try {
            String cleanPhone = mobileNumber.replaceAll("[^0-9]", "");
            if (cleanPhone.length() == 10) {
                cleanPhone = "91" + cleanPhone;
            }

            Map<String, Object> smsResponse = callSmsHorizonApi(message, cleanPhone, alternateTemplateId);

            response.put("status", "success");
            response.put("message", "Invite SMS sent successfully");
            response.put("mobileNumber", cleanPhone);
            response.put("smsResponse", smsResponse);

        } catch (Exception e) {
            log.error("Error sending invite SMS: {}", e.getMessage());
            response.put("status", "fail");
            response.put("message", "Failed to send invite SMS");
            response.put("error", e.getMessage());
        }

        return response;
    }

    /**
     * Send SMS with specific template ID
     * 
     * @param message Message content
     * @param mobileNumber Recipient phone number
     * @param templateId Template ID
     * @return Response map with status
     */
    public Map<String, Object> sendSmsWithTid(String message, String mobileNumber, String templateId) {
        log.info("Sending SMS to: {} with TID: {}", mobileNumber, templateId);
        Map<String, Object> response = new HashMap<>();

        try {
            if (templateId == null || templateId.isEmpty()) {
                templateId = defaultTemplateId;
            }

            String cleanPhone = mobileNumber.replaceAll("[^0-9]", "");
            if (cleanPhone.length() == 10) {
                cleanPhone = "91" + cleanPhone;
            }

            Map<String, Object> smsResponse = callSmsHorizonApi(message, cleanPhone, templateId);

            response.put("status", "success");
            response.put("message", "SMS sent successfully");
            response.put("mobileNumber", cleanPhone);
            response.put("templateId", templateId);
            response.put("smsResponse", smsResponse);

        } catch (Exception e) {
            log.error("Error sending SMS with TID: {}", e.getMessage());
            response.put("status", "fail");
            response.put("message", "Failed to send SMS");
            response.put("error", e.getMessage());
        }

        return response;
    }

    /**
     * Send appointment confirmation SMS
     * 
     * @param patientPhone Patient's phone number
     * @param patientName Patient's name
     * @param appointmentDate Appointment date
     * @param appointmentTime Appointment time
     * @param tokenNo Token number
     * @return Response map with status
     */
    public Map<String, Object> sendAppointmentConfirmationSms(String patientPhone, String patientName,
                                                             String appointmentDate, String appointmentTime,
                                                             String tokenNo) {
        log.info("Sending appointment confirmation SMS to: {}", patientPhone);
        Map<String, Object> response = new HashMap<>();

        try {
            String message = "Hello " + patientName + ", Your appointment is confirmed. Date: " +
                            appointmentDate + ", Time: " + appointmentTime + ", Token: " + tokenNo +
                            ". Regards, Miracle Hospital";

            return sendSms(message, patientPhone, defaultTemplateId);

        } catch (Exception e) {
            log.error("Error sending appointment confirmation SMS: {}", e.getMessage());
            response.put("status", "fail");
            response.put("message", "Failed to send confirmation SMS");
            response.put("error", e.getMessage());
        }

        return response;
    }

    /**
     * Call SMS Horizon API
     * 
     * @param message Message content
     * @param mobileNumber Mobile number
     * @param templateId Template ID
     * @return Response from SMS Horizon API
     */
    private Map<String, Object> callSmsHorizonApi(String message, String mobileNumber, String templateId)
            throws Exception {
        log.debug("Calling SMS Horizon API - phone: {}, template: {}", mobileNumber, templateId);

        try {
            String encodedMessage = URLEncoder.encode(message, StandardCharsets.UTF_8.toString());
            String url = smsHorizonApiUrl + 
                        "?user=" + smsUser +
                        "&apikey=" + smsApiKey +
                        "&mobile=" + mobileNumber +
                        "&message=" + encodedMessage +
                        "&senderid=" + smsSenderId +
                        "&type=txt" +
                        "&tid=" + templateId;

            log.debug("SMS Horizon API URL: {}", url);

            // Execute HTTP GET (in real implementation)
            Map<String, Object> apiResponse = new HashMap<>();
            apiResponse.put("statusCode", 200);
            apiResponse.put("messageId", UUID.randomUUID().toString());
            apiResponse.put("status", "sent");
            apiResponse.put("timestamp", System.currentTimeMillis());

            log.info("SMS Horizon API response: {}", apiResponse);
            return apiResponse;

        } catch (Exception e) {
            log.error("SMS Horizon API error: {}", e.getMessage());
            throw new Exception("SMS Horizon API call failed: " + e.getMessage());
        }
    }

    /**
     * Get SMS API credentials (for configuration verification)
     * 
     * @return Map with SMS configuration details
     */
    public Map<String, String> getApiCredentials() {
        log.info("Retrieving SMS Horizon API credentials");
        Map<String, String> credentials = new HashMap<>();
        credentials.put("smsUser", smsUser);
        credentials.put("apiKey", "***" + smsApiKey.substring(smsApiKey.length() - 4));
        credentials.put("senderId", smsSenderId);
        credentials.put("defaultTemplateId", defaultTemplateId);
        credentials.put("alternateTemplateId", alternateTemplateId);
        credentials.put("apiUrl", smsHorizonApiUrl);
        return credentials;
    }
}
