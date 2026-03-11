package com.miracle.repository;

import com.miracle.model.Patient;
import com.miracle.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    Optional<Patient> findByPatientStringId(String patientStringId);
    Optional<Patient> findByPhoneNumber(String phoneNumber);
    Optional<Patient> findFirstByUserAndRelation(User user, String relation);
Optional<Patient> findByUserAndRelation(User user, String relation);
    List<Patient> findByUserAndIsActiveTrue(User user);
    List<Patient> findByUserUserId(Long userId);
    List<Patient> findByPatientStatus(String status); 
List<Patient> findByUserUserStringId(String userStringId);
Optional<Patient> findByPatientStringIdAndUserUserStringId(String patientStringId, String userStringId);
}