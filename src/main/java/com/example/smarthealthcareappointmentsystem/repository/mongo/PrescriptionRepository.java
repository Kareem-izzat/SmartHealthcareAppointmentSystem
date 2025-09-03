package com.example.smarthealthcareappointmentsystem.repository.mongo;

import com.example.smarthealthcareappointmentsystem.entites.mongo.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepository extends JpaRepository<Prescription,String> {
    List<Prescription> findByPatientId(Long patientId);
}
