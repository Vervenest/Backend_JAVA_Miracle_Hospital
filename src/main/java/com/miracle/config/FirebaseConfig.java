package com.miracle.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

@Configuration
@ConditionalOnResource(resources = "classpath:firebase-credentials.json")
@Slf4j
public class FirebaseConfig {

    @Value("${firebase.project-id:}")
    private String projectId;

    @Bean
    public FirebaseMessaging firebaseMessaging() throws IOException {
        if (FirebaseApp.getApps().isEmpty()) {
            ClassPathResource serviceAccount = new ClassPathResource("firebase-credentials.json");
            
            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(
                            serviceAccount.getInputStream()))
                    .setProjectId(projectId)
                    .build();

            FirebaseApp.initializeApp(options);
            log.info("Firebase initialized successfully");
        }

        return FirebaseMessaging.getInstance();
    }
}
