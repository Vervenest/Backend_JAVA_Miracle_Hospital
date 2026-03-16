package com.miracle.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@Slf4j
public class FirebaseConfig {

    @Value("${firebase.project-id:}")
    private String projectId;

    @Value("${FIREBASE_CREDENTIALS_JSON:}")
    private String firebaseCredentialsJson;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        if (projectId == null || projectId.isEmpty()) {
            log.warn("Firebase project ID not configured - Firebase disabled");
            return null;
        }

        if (FirebaseApp.getApps().isEmpty()) {
            InputStream credentialsStream = null;

            // Try environment variable first (Railway)
            if (firebaseCredentialsJson != null && !firebaseCredentialsJson.isEmpty()) {
                log.info("Loading Firebase credentials from environment variable");
                credentialsStream = new ByteArrayInputStream(
                    firebaseCredentialsJson.replace("\\n", "\n").getBytes(StandardCharsets.UTF_8));
            } else {
                // Fall back to file (local)
                ClassPathResource resource = new ClassPathResource("firebase-credentials.json");
                if (resource.exists()) {
                    log.info("Loading Firebase credentials from file");
                    credentialsStream = resource.getInputStream();
                }
            }

            if (credentialsStream == null) {
                log.warn("No Firebase credentials found - Firebase disabled");
                return null;
            }

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(credentialsStream))
                    .setProjectId(projectId)
                    .build();

            FirebaseApp.initializeApp(options);
            log.info("Firebase initialized successfully for project: {}", projectId);
        }

        return FirebaseMessaging.getInstance();
    }
}