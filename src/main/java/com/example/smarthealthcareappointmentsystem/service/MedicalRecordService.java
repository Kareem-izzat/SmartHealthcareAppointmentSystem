package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.MedicalRecordDto;
import com.example.smarthealthcareappointmentsystem.entites.mongo.Prescription;

public interface MedicalRecordService {
    MedicalRecordDto getOrCreateRecord(Long patientId);
    MedicalRecordDto addPrescriptionToRecord(Long patientId, Prescription prescription);
}

