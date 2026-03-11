package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patientdocuments")
@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class PatientDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "appointmentId", length = 50)
    private String appointmentId;

    @Column(name = "patientId", length = 50, nullable = false)
    private String patientId;

    @Column(name = "doctorId", length = 50)
    private String doctorId;

    @Column(name = "fileName", length = 255, nullable = false)
    private String fileName;

    @Column(name = "docType", length = 100, nullable = false)
    private String docType;

    @Column(name = "uploaded_at")
    private LocalDateTime uploadedAt;

    @PrePersist
    protected void onCreate() {
        if (uploadedAt == null) uploadedAt = LocalDateTime.now();
    }
}