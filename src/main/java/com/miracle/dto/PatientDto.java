package com.miracle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PatientDto {
    private Long patientId;
    private Long userId;
    private String userName;
    private String email;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private String gender;
    private String address;
    private String bloodGroup;
    private String emergencyContact;
    private String medicalHistory;
    private String allergies;
    private String insuranceProvider;
    private String insuranceNumber;
    private Boolean isActive;
    private String userImg;
}
