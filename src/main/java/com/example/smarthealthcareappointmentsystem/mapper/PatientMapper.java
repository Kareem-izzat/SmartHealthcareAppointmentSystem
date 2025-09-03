package com.example.smarthealthcareappointmentsystem.mapper;

import com.example.smarthealthcareappointmentsystem.DTO.PatientDto;
import com.example.smarthealthcareappointmentsystem.entites.Patient;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper {
    public PatientDto toDto(Patient patient) {
        if (patient == null) return null;

        return PatientDto.builder()
                .id(patient.getId())
                .firstName(patient.getFirstName())
                .lastName(patient.getLastName())
                .email(patient.getEmail())
                .phone(patient.getPhone())
                .dateOfBirth(patient.getDateOfBirth())
                .medicalHistory(patient.getMedicalHistory())
                .build();
    }

    public Patient toEntity(PatientDto dto) {
        if (dto == null) return null;

        return Patient.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .dateOfBirth(dto.getDateOfBirth())
                .medicalHistory(dto.getMedicalHistory())
                .build();
    }
}
