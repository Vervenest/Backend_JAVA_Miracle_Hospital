package com.miracle.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorDto {
    private Long doctorId;
    private Long userId;
    private String userName;
    private String email;
    private String specialization;
    private String qualifications;
    private Integer experienceYears;
    private Double consultationFee;
    private String clinicAddress;
    private String clinicPhone;
    private String availabilityDays;
    private String availabilityTime;
    private Boolean isVerified;
    private Boolean isActive;
    private String userImg;
}
