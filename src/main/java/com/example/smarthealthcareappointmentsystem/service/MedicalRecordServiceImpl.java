package com.example.smarthealthcareappointmentsystem.service;


import com.example.smarthealthcareappointmentsystem.DTO.MedicalRecordDto;
import com.example.smarthealthcareappointmentsystem.entites.mongo.MedicalRecord;
import com.example.smarthealthcareappointmentsystem.entites.mongo.Prescription;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.mongo.MedicalRecordMapper;
import com.example.smarthealthcareappointmentsystem.repository.MedicalRecordRepository;
import com.example.smarthealthcareappointmentsystem.repository.PatientRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PatientRepository patientRepository;
    private final MedicalRecordMapper medicalRecordMapper;

    // this is needed because prescription will be appended to ot so
    // if the patient is a first time user it will automaticly create dont exist
    @Override
    public MedicalRecordDto getOrCreateRecord(Long patientId) {
        MedicalRecord record = getOrCreate(patientId);
        return medicalRecordMapper.toDto(record);
    }

    @Override
    public MedicalRecordDto addPrescriptionToRecord(Long patientId, Prescription prescription) {
        MedicalRecord record = getOrCreate(patientId);
        record.getPrescriptions().add(prescription);
        MedicalRecord updated = medicalRecordRepository.save(record);
        return medicalRecordMapper.toDto(updated);
    }

    private MedicalRecord getOrCreate(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id: " + patientId);
        }
        return medicalRecordRepository.findByPatientId(patientId)
                .orElseGet(() -> medicalRecordRepository.save(
                        MedicalRecord.builder()
                                .patientId(patientId)
                                .prescriptions(new ArrayList<>())
                                .notes(new ArrayList<>())
                                .build()
                ));
    }

}