package com.miracle.repository;

import com.miracle.model.PatientDoctorChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PatientDoctorChatRepository extends JpaRepository<PatientDoctorChat, Long> {

    @Query("SELECT c FROM PatientDoctorChat c WHERE c.senderId IN :patientIds OR c.receiverId IN :patientIds")
    List<PatientDoctorChat> findBySenderIdInOrReceiverIdIn(@Param("patientIds") List<String> patientIds);

    List<PatientDoctorChat> findByUserIdOrderByCreateDatetimeAsc(String userId);

@Query("SELECT DISTINCT CASE WHEN c.senderType = 'PATIENT' THEN c.senderId ELSE c.receiverId END FROM PatientDoctorChat c WHERE c.senderType = 'PATIENT' OR c.receiverType = 'PATIENT'")
List<String> findDistinctPatientIds();
}