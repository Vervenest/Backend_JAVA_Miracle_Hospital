package com.miracle.config;

import com.miracle.security.JwtAuthenticationFilter;
import com.miracle.security.JwtAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception
                .authenticationEntryPoint(jwtAuthenticationEntryPoint))
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))

            .authorizeHttpRequests(authz -> authz

                // ── Static / Admin ────────────────────────────────────────
                .requestMatchers("/", "/index.html", "/app.js", "/error").permitAll()
                .requestMatchers("/assets/**", "/static/**", "/WEB-INF/**").permitAll()
                .requestMatchers("/admin/login").permitAll()
                .requestMatchers("/adminmodel/loginemail").permitAll()
                .requestMatchers("/admin/**").permitAll()
                .requestMatchers("/adminmodel/**").permitAll()
                .requestMatchers("/Patientapi/**").permitAll()

                // ── Shared API ────────────────────────────────────────────
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/config/**").permitAll()
                .requestMatchers("/api/privacy/**").permitAll()

                // ── Patient Auth ──────────────────────────────────────────
                .requestMatchers("/api/patients/patientLogin").permitAll()
                .requestMatchers("/api/patients/patientValidateOtp").permitAll()
                .requestMatchers("/api/patients/resendOtp").permitAll()
                .requestMatchers("/api/patients/userLogin").permitAll()
                .requestMatchers("/api/patients/userValidateOtp").permitAll()

                // ── Patient Profile ───────────────────────────────────────
                .requestMatchers("/api/patients/setProfile").permitAll()
                .requestMatchers("/api/patients/getProfile").permitAll()
                .requestMatchers("/api/patients/addPatientUsers").permitAll()
                .requestMatchers("/api/patients/getPatientsList").permitAll()
                .requestMatchers("/api/patients/deleteAccountPermanently").permitAll()
                .requestMatchers("/api/patients/getPatientDetailsbyId").permitAll()
                .requestMatchers("/api/patients/getPregnancyEvents").permitAll()

                // ── Doctor (Patient-facing) ───────────────────────────────
                .requestMatchers("/api/patients/getDoctorList").permitAll()
                .requestMatchers("/api/patients/getDoctorSpecialisationList").permitAll()
                .requestMatchers("/api/patients/getDoctorDetailsById").permitAll()
                .requestMatchers("/api/patients/getDoctorsByLocation").permitAll()
                .requestMatchers("/api/patients/doctorAvailableSlotTime").permitAll()
                .requestMatchers("/api/patients/getLocationList").permitAll()

                // ── Patient Appointment ───────────────────────────────────
                .requestMatchers("/api/patients/addAppointment").permitAll()
                .requestMatchers("/api/patients/appointmentHistory").permitAll()
                .requestMatchers("/api/patients/updateAppointmentStatusById").permitAll()
                .requestMatchers("/api/patients/getPatientQueueStatus").permitAll()

                // ── Patient Chat ──────────────────────────────────────────
                .requestMatchers("/api/patients/chatPatientSendMessage").permitAll()
                .requestMatchers("/api/patients/getChatPatientMessage").permitAll()

                // ── Patient Notifications ─────────────────────────────────
                .requestMatchers("/api/patients/notificationList").permitAll()

                // ── Doctor Auth ───────────────────────────────────────────
                .requestMatchers("/api/doctors/doctorLogin").permitAll()
                .requestMatchers("/api/doctors/doctorValidateOtp").permitAll()
                .requestMatchers("/api/doctors/resendOtp").permitAll()
                .requestMatchers("/api/doctors/all").permitAll()
                .requestMatchers("/api/doctors/specialization/**").permitAll()

                // ── Doctor Profile ────────────────────────────────────────
                .requestMatchers("/api/doctors/getProfile").permitAll()
                .requestMatchers("/api/doctors/deleteAccountPermanently").permitAll()

                // ── Doctor Appointments ───────────────────────────────────
                .requestMatchers("/api/doctors/appointmentHistory").permitAll()
                .requestMatchers("/api/doctors/getAppointmenDetailsById").permitAll()
                .requestMatchers("/api/doctors/getAppointmentDetailsById").permitAll()
                .requestMatchers("/api/doctors/updateAppointmentStatusById").permitAll()

                // ── Doctor Notifications ──────────────────────────────────
                .requestMatchers("/api/doctors/notificationList").permitAll()
                .requestMatchers("/api/doctors/sendAppointmentPreNotificationToPatient").permitAll()

                // ── Doctor Chat ───────────────────────────────────────────
                .requestMatchers("/api/doctors/getPatientChatList").permitAll()
                .requestMatchers("/api/doctors/chatDoctorSendMessage").permitAll()
                .requestMatchers("/api/doctors/getChatDoctorMessage").permitAll()

                // ── Doctor Patient ────────────────────────────────────────
              .requestMatchers("/api/doctors/getPatientDetails").permitAll()
              .requestMatchers("/api/doctors/uploadFile").permitAll()
              .requestMatchers("/api/doctors/getReportsList").permitAll()
              .requestMatchers("/api/doctors/deleteReport").permitAll()
              

             .requestMatchers("/api/patients/getPatientsListWithDocuments").permitAll()

                // ── Doctor Queue ──────────────────────────────────────────
                .requestMatchers("/api/doctors/getDoctorQueueOverview").permitAll()

                // ── Everything else requires authentication ────────────────
                .anyRequest().authenticated())

            .cors(cors -> {});

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}