package com.example.smarthealthcareappointmentsystem.repository.mongo;

import com.example.smarthealthcareappointmentsystem.entites.mongo.MedicalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalRecordRepository extends JpaRepository<MedicalRecord,String> {
    List<MedicalRecord> findByPatientId(Long patientId);
}
