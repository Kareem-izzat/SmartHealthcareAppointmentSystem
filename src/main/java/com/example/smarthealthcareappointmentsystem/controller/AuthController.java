package com.example.smarthealthcareappointmentsystem.controller;

import com.example.smarthealthcareappointmentsystem.DTO.LoginResponseDto;

import com.example.smarthealthcareappointmentsystem.DTO.request.LoginRequestDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPatientDto;

import com.example.smarthealthcareappointmentsystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {

        return ResponseEntity.ok(authService.login(loginRequestDto));
    }
    // only admin can register
    @PostMapping("/register/patient")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerPatient(@RequestBody RequestPatientDto patientDto) {
        return ResponseEntity.ok(authService.registerPatient(patientDto));
    }
    @PostMapping("/register/doctor")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> registerDoctor(@RequestBody RequestDoctorDto doctorDto) {
        return ResponseEntity.ok(authService.registerDoctor(doctorDto));
    }
}
