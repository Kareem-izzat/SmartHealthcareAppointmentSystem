package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.PatientDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPatientDto;
import com.example.smarthealthcareappointmentsystem.entites.Patient;
import com.example.smarthealthcareappointmentsystem.entites.Role;
import com.example.smarthealthcareappointmentsystem.exception.BadRequestException;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;

import com.example.smarthealthcareappointmentsystem.mapper.PatientMapper;
import com.example.smarthealthcareappointmentsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final PasswordEncoder passwordEncoder;


    @Override
    @Transactional(readOnly = true)
    public Page<PatientDto> getAllPatients(Pageable pageable) {
        return patientRepository.findAll(pageable)
                .map(patientMapper::toDto);
    }
    @Override
    public PatientDto updatePatient(Long patientId, RequestPatientDto patientDto){
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + patientId));

        patient.setFirstName(patientDto.getFirstName());
        patient.setLastName(patientDto.getLastName());
        patient.setEmail(patientDto.getEmail());
        patient.setPhone(patientDto.getPhone());
        patient.setDateOfBirth(patientDto.getDateOfBirth());
        patient.setMedicalHistory(patientDto.getMedicalHistory());
        // check if exist before encode
        if (patientDto.getPassword() != null && !patientDto.getPassword().isBlank()) {
            patient.setPassword(passwordEncoder.encode(patientDto.getPassword()));
        }

        Patient updatedPatient = patientRepository.save(patient);
        return patientMapper.toDto(updatedPatient);
    }

    @Override
    @Transactional(readOnly = true)
    public PatientDto getPatientById(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + patientId));
        return patientMapper.toDto(patient);
    }
    @Override
    public PatientDto addPatient(RequestPatientDto patientDto) {
        if (patientRepository.existsByEmail(patientDto.getEmail())) {
            throw new BadRequestException("Email is already in use by another patient.");
        }
        Patient patient = patientMapper.toEntity(patientDto);
        patient.setPassword(passwordEncoder.encode(patientDto.getPassword()));
        patient.setRole(Role.PATIENT);
        Patient saved = patientRepository.save(patient);
        return patientMapper.toDto(saved);
    }

    @Override
    public void removePatient(Long patientId) {
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient not found with id " + patientId);
        }
        patientRepository.deleteById(patientId);
    }
}

