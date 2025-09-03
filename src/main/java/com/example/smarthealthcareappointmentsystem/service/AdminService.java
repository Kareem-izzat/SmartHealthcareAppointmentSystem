package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.PatientDto;

import java.util.List;

public interface AdminService {
    // for doctor managment
    List<DoctorDto> getAllDoctors();
    DoctorDto getDoctorById(Long id);
    void RemoveDoctorById(Long id);
    DoctorDto updateDoctorById(Long id, DoctorDto doctorDto);
    DoctorDto addDoctor(DoctorDto doctorDto);

    // for patient managment
    List<PatientDto> getAllPatients();
    PatientDto getPatientById(Long patientId);
    PatientDto addPatient(PatientDto patientDto);
    PatientDto updatePatient(Long patientId, PatientDto patientDto);
    void removePatient(Long patientId);

}
