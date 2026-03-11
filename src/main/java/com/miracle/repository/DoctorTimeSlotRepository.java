package com.miracle.repository;

import com.miracle.model.DoctorTimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorTimeSlotRepository extends JpaRepository<DoctorTimeSlot, Long> {
    
    List<DoctorTimeSlot> findByDoctorDoctorId(Long doctorId);
    
    Optional<DoctorTimeSlot> findByDoctorDoctorIdAndDayOfWeek(Long doctorId, String dayOfWeek);
    
    void deleteByDoctorDoctorId(Long doctorId);
}
