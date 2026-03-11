package com.miracle.repository;

import com.miracle.model.Appointment;
import com.miracle.model.Patient;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    Optional<Appointment> findByAppointmentStringId(String appointmentStringId);
    List<Appointment> findByPatientPatientId(Long patientId);
    List<Appointment> findByDoctorDoctorId(Long doctorId);
    List<Appointment> findByAppointmentDate(String date);
    List<Appointment> findByUserId(String userId);
    long countByAppointmentDate(String date);
    List<Appointment> findByDoctorDoctorStringIdAndAppointmentDateAndAppointmentStartTime(
    String doctorId, String appointmentDate, String appointmentStartTime);
    List<Appointment> findByDoctorDoctorStringIdAndAppointmentDateAndAppointmentStartTimeLessThanAndAppointmentEndTimeGreaterThan(
    String doctorId, String appointmentDate, String endTime, String startTime);
    List<Appointment> findByPatient(Patient patient);

long countByAppointmentDateAndDoctorDoctorStringId(String date, String doctorId);
@Query("SELECT a FROM Appointment a WHERE a.doctor.doctorStringId = :doctorId AND a.appointmentDate = :date AND a.appointmentStatus <> '0' ORDER BY a.appointmentStartTime ASC")
List<Appointment> findByDoctorStringIdAndDateOrdered(@Param("doctorId") String doctorId, @Param("date") String date);

List<Appointment> findByDoctorDoctorStringId(String doctorStringId);

}