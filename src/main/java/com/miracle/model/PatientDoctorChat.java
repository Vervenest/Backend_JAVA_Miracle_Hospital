package com.miracle.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patientDoctorChat")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDoctorChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "chatId", length = 64)
    private String chatId;

    @Column(name = "userId", length = 64)
    private String userId;

    @Column(name = "senderId", length = 64)
    private String senderId;

    @Column(name = "senderType", length = 50)
    private String senderType;

    @Column(name = "receiverId", length = 64)
    private String receiverId;

    @Column(name = "receiverType", length = 50)
    private String receiverType;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "messageStatus", length = 5)
    @Builder.Default
    private String messageStatus = "1";

    @Column(name = "doctorId", length = 64)
    private String doctorId;

    @Column(name = "doctorName", length = 150)
    private String doctorName;

    @Column(name = "create_datetime")
    private LocalDateTime createDatetime;

    @Column(name = "update_datetime")
    private LocalDateTime updateDatetime;

    @PrePersist
    protected void onCreate() {
        createDatetime = LocalDateTime.now();
        updateDatetime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updateDatetime = LocalDateTime.now();
    }
}