package com.miracle.repository;

import com.miracle.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {
    Optional<Doctor> findByDoctorStringId(String doctorStringId);
    Optional<Doctor> findByPhoneNumber(String phoneNumber);
    Optional<Doctor> findByUserUserId(Long userId);
    List<Doctor> findByDoctorStatus(String doctorStatus);
    List<Doctor> findBySpecializationAndIsActiveTrue(String specialization);
    List<Doctor> findBySpecializationContaining(String specialisationId);
    List<Doctor> findByDoctorStatusAndIsAdminDoctor(String doctorStatus, String isAdminDoctor);
    List<Doctor> findByLocationIdAndDoctorStatus(String locationId, String doctorStatus);
}