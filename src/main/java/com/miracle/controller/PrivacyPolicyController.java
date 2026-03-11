package com.miracle.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/privacy")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@Slf4j
public class PrivacyPolicyController {

    @GetMapping("/policy")
    public ResponseEntity<?> getPrivacyPolicy() {
        log.info("Fetching privacy policy");
        
        String privacyPolicy = "PRIVACY POLICY\n\n" +
                              "Effective Date: January 1, 2024\n\n" +
                              "1. INTRODUCTION\n" +
                              "Miracle Hospital (\"Company\", \"we\", \"our\", or \"us\") operates the Miracle Hospital mobile application and website.\n\n" +
                              "2. INFORMATION WE COLLECT\n" +
                              "We collect information you provide directly to us, such as when you:\n" +
                              "- Create an account\n" +
                              "- Book an appointment\n" +
                              "- Update your profile\n" +
                              "- Contact customer support\n\n" +
                              "3. HOW WE USE YOUR INFORMATION\n" +
                              "We use the information we collect to:\n" +
                              "- Provide, maintain and improve our services\n" +
                              "- Send you technical notices and support messages\n" +
                              "- Respond to your comments and questions\n" +
                              "- Send marketing communications (with your consent)\n\n" +
                              "4. DATA SECURITY\n" +
                              "We implement appropriate technical and organizational measures to protect your personal data.\n\n" +
                              "5. YOUR RIGHTS\n" +
                              "You have the right to:\n" +
                              "- Access your personal data\n" +
                              "- Correct inaccurate data\n" +
                              "- Request deletion of your data\n" +
                              "- Withdraw consent\n\n" +
                              "6. CONTACT US\n" +
                              "If you have privacy concerns, please contact us at privacy@miraclehospital.com\n";

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Privacy policy retrieved successfully");
        response.put("policy", privacyPolicy);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/terms")
    public ResponseEntity<?> getTermsOfService() {
        log.info("Fetching terms of service");
        
        String termsOfService = "TERMS OF SERVICE\n\n" +
                               "Effective Date: January 1, 2024\n\n" +
                               "1. ACCEPTANCE OF TERMS\n" +
                               "By accessing and using this application, you accept and agree to be bound by the terms and provision of this agreement.\n\n" +
                               "2. USE LICENSE\n" +
                               "Permission is granted to temporarily download one copy of the materials (information or software) on Miracle Hospital's application for personal, non-commercial transitory viewing only.\n\n" +
                               "3. DISCLAIMER\n" +
                               "The materials on Miracle Hospital's application are provided on an 'as is' basis.\n\n" +
                               "4. LIMITATIONS\n" +
                               "In no event shall Miracle Hospital or its suppliers be liable for any damages (including, without limitation, damages for loss of data or profit, or due to business interruption).\n\n" +
                               "5. ACCURACY OF MATERIALS\n" +
                               "The materials appearing on Miracle Hospital's application could include technical, typographical, or photographic errors. Miracle Hospital does not warrant that any of the materials on our application are accurate, complete, or current.\n\n" +
                               "6. MODIFICATIONS\n" +
                               "Miracle Hospital may revise these terms of service for our application at any time without notice.\n\n" +
                               "7. GOVERNING LAW\n" +
                               "These terms and conditions are governed by and construed in accordance with the laws of India.\n";

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Terms of service retrieved successfully");
        response.put("terms", termsOfService);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/about")
    public ResponseEntity<?> getAboutUs() {
        log.info("Fetching about us information");
        
        String aboutUs = "ABOUT MIRACLE HOSPITAL\n\n" +
                        "Welcome to Miracle Hospital - Your Trusted Healthcare Partner\n\n" +
                        "Miracle Hospital is a leading healthcare provider committed to delivering exceptional patient care, innovative medical services, and compassionate treatment.\n\n" +
                        "OUR MISSION:\n" +
                        "To provide high-quality, accessible healthcare services to all patients in our community.\n\n" +
                        "OUR VALUES:\n" +
                        "- Patient-Centered Care: Putting patients' needs first\n" +
                        "- Excellence: Delivering the highest standard of care\n" +
                        "- Integrity: Acting with honesty and transparency\n" +
                        "- Compassion: Treating all with dignity and respect\n" +
                        "- Innovation: Embracing new technologies and methods\n\n" +
                        "OUR SERVICES:\n" +
                        "- Outpatient Consultations\n" +
                        "- Diagnostic Services\n" +
                        "- Specialist Care\n" +
                        "- Emergency Services\n" +
                        "- Preventive Care\n\n" +
                        "For more information, visit our website or call our customer support.\n";

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "About us information retrieved successfully");
        response.put("about", aboutUs);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/disclaimer")
    public ResponseEntity<?> getDisclaimer() {
        log.info("Fetching disclaimer");
        
        String disclaimer = "DISCLAIMER\n\n" +
                           "IMPORTANT: Please read this disclaimer carefully before using Miracle Hospital's application and services.\n\n" +
                           "1. MEDICAL DISCLAIMER\n" +
                           "The content and information provided through this application are for informational purposes only and should not be construed as professional medical advice.\n\n" +
                           "2. NOT A SUBSTITUTE FOR PROFESSIONAL ADVICE\n" +
                           "The information on this application is not intended to replace professional medical consultation, diagnosis, or treatment.\n\n" +
                           "3. EMERGENCY SITUATIONS\n" +
                           "In case of medical emergency, please call 911 (or your local emergency number) immediately.\n\n" +
                           "4. USER RESPONSIBILITY\n" +
                           "Users are responsible for evaluating the accuracy and completeness of the information provided.\n\n" +
                           "5. LIMITED LIABILITY\n" +
                           "Miracle Hospital shall not be liable for any indirect, incidental, special, or consequential damages arising from the use of this application.\n";

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Disclaimer retrieved successfully");
        response.put("disclaimer", disclaimer);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/contact-us")
    public ResponseEntity<?> getContactInfo() {
        log.info("Fetching contact information");
        
        Map<String, Object> contactInfo = new HashMap<>();
        contactInfo.put("email", "support@miraclehospital.com");
        contactInfo.put("phone", "+91-9380012244");
        contactInfo.put("address", "Miracle Hospital, Healthcare City, India");
        contactInfo.put("website", "https://miraclehospital.com");
        contactInfo.put("hours", "24/7 Available");

        Map<String, Object> response = new HashMap<>();
        response.put("status", "success");
        response.put("message", "Contact information retrieved successfully");
        response.put("contactInfo", contactInfo);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }
}
