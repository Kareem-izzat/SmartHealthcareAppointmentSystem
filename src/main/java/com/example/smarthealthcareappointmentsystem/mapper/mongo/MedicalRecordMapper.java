package com.example.smarthealthcareappointmentsystem.mapper.mongo;

import com.example.smarthealthcareappointmentsystem.DTO.mongo.MedicalRecordDto;
import com.example.smarthealthcareappointmentsystem.DTO.mongo.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.entites.mongo.MedicalRecord;
import com.example.smarthealthcareappointmentsystem.entites.mongo.Prescription;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
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

    public MedicalRecord toEntity(MedicalRecordDto dto) {
        if (dto == null) return null;

        List<Prescription> prescriptions = dto.getPrescriptions() != null
                ? dto.getPrescriptions().stream()
                .map(prescriptionMapper::toEntity)
                .collect(Collectors.toList()) : null;

        return MedicalRecord.builder()
                .patientId(dto.getPatientId())
                .prescriptions(prescriptions)
                .notes(dto.getNotes())
                .build();
    }


}
