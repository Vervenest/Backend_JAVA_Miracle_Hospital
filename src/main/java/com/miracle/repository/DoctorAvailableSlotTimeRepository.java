package com.miracle.repository;

import com.miracle.model.DoctorAvailableSlotTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface DoctorAvailableSlotTimeRepository extends JpaRepository<DoctorAvailableSlotTime, Integer> {
    List<DoctorAvailableSlotTime> findByDoctorId(String doctorId);
    Optional<DoctorAvailableSlotTime> findByDoctorIdAndDayOfWeek(String doctorId, DoctorAvailableSlotTime.DayOfWeek dayOfWeek);
    void deleteByDoctorId(String doctorId);
}
