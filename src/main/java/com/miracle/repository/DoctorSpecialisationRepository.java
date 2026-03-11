package com.miracle.repository;

import com.miracle.model.DoctorSpecialisation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorSpecialisationRepository extends JpaRepository<DoctorSpecialisation, Integer> {
    List<DoctorSpecialisation> findBySpecialisationStatus(String status);
    Optional<DoctorSpecialisation> findBySpecialisationId(String specialisationId);
    Optional<DoctorSpecialisation> findBySpecialisationNameIgnoreCase(String name);
}