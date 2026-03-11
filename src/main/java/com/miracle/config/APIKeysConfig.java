package com.miracle.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * APIKeysConfig - Configuration class for all external API credentials
 * 
 * This class centralizes all API keys and credentials from:
 * - Whatsapp (Gupshup API)
 * - SMS (SMS Horizon)  
 * - Firebase (FCM)
 * - Other third-party services
 */
@Configuration
@Component
@Data
public class APIKeysConfig {

    // ========== WHATSAPP / GUPSHUP API ==========
    @Value("${whatsapp.gupshup.api-key}")
    private String gupshupApiKey;

    @Value("${whatsapp.gupshup.template-id}")
    private String gupshupTemplateId;

    @Value("${whatsapp.gupshup.source-number}")
    private String gupshupSourceNumber;

    @Value("${whatsapp.gupshup.api-url}")
    private String gupshupApiUrl;

    // ========== SMS / SMS HORIZON API ==========
    @Value("${sms.horizon.user}")
    private String smsHorizonUser;

    @Value("${sms.horizon.api-key}")
    private String smsHorizonApiKey;

    @Value("${sms.horizon.sender-id}")

    private String smsHorizonSenderId;

    @Value("${sms.horizon.template-id}")
    private String smsHorizonDefaultTemplateId;

    @Value("${sms.horizon.template-id-alt}")
    private String smsHorizonAlternateTemplateId;

    @Value("${sms.horizon.api-url}")
    private String smsHorizonApiUrl;

    // ========== WHATSAPP / CLOUD WHATSAPP API ==========
    @Value("${whatsapp.cloud.api-key}")
    private String cloudWhatsappApiKey;

    @Value("${whatsapp.cloud.api-url}")
    private String cloudWhatsappApiUrl;

    // ========== FIREBASE / FCM ==========
    @Value("${firebase.doctor.api-key:}")
    private String firebaseDoctorFcmKey;

    @Value("${firebase.patient.api-key:}")
    private String firebasePatientFcmKey;

    @Value("${firebase.doctor-app-id:}")
    private String firebaseDoctorAppId;

    @Value("${firebase.patient-app-id:}")
    private String firebasePatientAppId;

    @Value("${firebase.fcm-url:https://fcm.googleapis.com/fcm/send}")
    private String firebaseFcmUrl;

    // ========== APPLICATION CONFIGURATION ==========
    @Value("${app.server.mode:DEVELOPMENT}")
    private String appServerMode;

    @Value("${app.server.url:http://localhost:8080}")
    private String appServerUrl;

    @Value("${app.timezone:Asia/Kolkata}")
    private String appTimezone;

    @Value("${app.version:1.0.0}")
    private String appVersion;

    // ========== DATABASE CONFIGURATION ==========
    @Value("${spring.datasource.url:jdbc:mysql://localhost:3306/miracle_hospital}")
    private String databaseUrl;

    @Value("${spring.datasource.username:root}")
    private String databaseUsername;

    @Value("${spring.datasource.password:}")
    private String databasePassword;

    // ========== JWT CONFIGURATION ==========
    @Value("${jwt.secret:}")
    private String jwtSecret;

    @Value("${jwt.expiration:86400000}")
    private Long jwtExpiration;

    /**
     * Get Whatsapp (Gupshup) configuration
     */
    public WhatsappConfig getWhatsappConfig() {
        return new WhatsappConfig(
            gupshupApiKey,
            gupshupTemplateId,
            gupshupSourceNumber,
            gupshupApiUrl,
            cloudWhatsappApiKey,
            cloudWhatsappApiUrl
        );
    }

    /**
     * Get SMS (SMS Horizon) configuration
     */
    public SmsConfig getSmsConfig() {
        return new SmsConfig(
            smsHorizonUser,
            smsHorizonApiKey,
            smsHorizonSenderId,
            smsHorizonDefaultTemplateId,
            smsHorizonAlternateTemplateId,
            smsHorizonApiUrl
        );
    }

    /**
     * Get Firebase (FCM) configuration
     */
    public FirebaseConfig getFirebaseConfig() {
        return new FirebaseConfig(
            firebaseDoctorFcmKey,
            firebasePatientFcmKey,
            firebaseDoctorAppId,
            firebasePatientAppId,
            firebaseFcmUrl
        );
    }

    @Data
    public static class WhatsappConfig {
        public final String gupshupApiKey;
        public final String gupshupTemplateId;
        public final String gupshupSourceNumber;
        public final String gupshupApiUrl;
        public final String cloudWhatsappApiKey;
        public final String cloudWhatsappApiUrl;

        public WhatsappConfig(String gupshupApiKey, String gupshupTemplateId,
                             String gupshupSourceNumber, String gupshupApiUrl,
                             String cloudWhatsappApiKey, String cloudWhatsappApiUrl) {
            this.gupshupApiKey = gupshupApiKey;
            this.gupshupTemplateId = gupshupTemplateId;
            this.gupshupSourceNumber = gupshupSourceNumber;
            this.gupshupApiUrl = gupshupApiUrl;
            this.cloudWhatsappApiKey = cloudWhatsappApiKey;
            this.cloudWhatsappApiUrl = cloudWhatsappApiUrl;
        }
    }

    @Data
    public static class SmsConfig {
        public final String smsUser;
        public final String apiKey;
        public final String senderId;
        public final String defaultTemplateId;
        public final String alternateTemplateId;
        public final String apiUrl;

        public SmsConfig(String smsUser, String apiKey, String senderId,
                       String defaultTemplateId, String alternateTemplateId, String apiUrl) {
            this.smsUser = smsUser;
            this.apiKey = apiKey;
            this.senderId = senderId;
            this.defaultTemplateId = defaultTemplateId;
            this.alternateTemplateId = alternateTemplateId;
            this.apiUrl = apiUrl;
        }
    }

    @Data
    public static class FirebaseConfig {
        public final String doctorFcmKey;
        public final String patientFcmKey;
        public final String doctorAppId;
        public final String patientAppId;
        public final String fcmUrl;

        public FirebaseConfig(String doctorFcmKey, String patientFcmKey,
                             String doctorAppId, String patientAppId, String fcmUrl) {
            this.doctorFcmKey = doctorFcmKey;
            this.patientFcmKey = patientFcmKey;
            this.doctorAppId = doctorAppId;
            this.patientAppId = patientAppId;
            this.fcmUrl = fcmUrl;
        }
    }
}
