package com.miracle.repository;

import com.miracle.model.PatientDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatientDocumentRepository extends JpaRepository<PatientDocument, Long> {
    List<PatientDocument> findByPatientId(String patientId);
    List<PatientDocument> findByPatientIdAndAppointmentId(String patientId, String appointmentId);
}