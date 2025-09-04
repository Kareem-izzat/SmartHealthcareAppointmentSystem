package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.PatientDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPatientDto;

import java.util.List;

public interface AdminService {
    // for doctor managment
    List<DoctorDto> getAllDoctors();
    DoctorDto getDoctorById(Long id);
    void RemoveDoctorById(Long id);
    DoctorDto updateDoctorById(Long id, RequestDoctorDto doctorDto);
    DoctorDto addDoctor(RequestDoctorDto doctorDto);

    // for patient managment
    List<PatientDto> getAllPatients();
    PatientDto getPatientById(Long patientId);
    PatientDto addPatient(RequestPatientDto patientDto);
    PatientDto updatePatient(Long patientId, RequestPatientDto patientDto);
    void removePatient(Long patientId);

}
