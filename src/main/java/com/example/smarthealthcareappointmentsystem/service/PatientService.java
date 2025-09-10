package com.example.smarthealthcareappointmentsystem.service;


import com.example.smarthealthcareappointmentsystem.DTO.*;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPatientDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface PatientService {
    Page<PatientDto> getAllPatients(Pageable pageable);
    PatientDto getPatientById(Long patientId);
    PatientDto addPatient(RequestPatientDto patientDto);
    PatientDto updatePatient(Long patientId, RequestPatientDto patientDto);
    void removePatient(Long patientId);

}
