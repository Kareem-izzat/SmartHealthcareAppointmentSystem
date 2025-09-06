package com.example.smarthealthcareappointmentsystem.mapper.mongo;

import com.example.smarthealthcareappointmentsystem.DTO.MedicalRecordDto;
import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.entites.mongo.MedicalRecord;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
// mapper between dto and entity
public class MedicalRecordMapper {

    private final PrescriptionMapper prescriptionMapper;
    public MedicalRecordMapper(PrescriptionMapper prescriptionMapper) {
        this.prescriptionMapper = prescriptionMapper;
    }
    public MedicalRecordDto toDto(MedicalRecord record) {
        if (record == null) return null;

        List<PrescriptionDto> prescriptions = record.getPrescriptions() != null
                ? record.getPrescriptions().stream()
                .map(prescriptionMapper::toDto)
                .collect(Collectors.toList()) : null;

        return MedicalRecordDto.builder()
                .patientId(record.getPatientId())
                .prescriptions(prescriptions)
                .notes(record.getNotes())
                .build();
    }



}
