package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.LoginResponseDto;

import com.example.smarthealthcareappointmentsystem.DTO.request.LoginRequestDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPatientDto;

public interface AuthService {
    LoginResponseDto login(LoginRequestDto loginRequestDto);
    String registerPatient(RequestPatientDto patientDto);
    String registerDoctor(RequestDoctorDto doctorDto);
}
