package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.LoginResponseDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.LoginRequestDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPatientDto;
import com.example.smarthealthcareappointmentsystem.entites.Doctor;
import com.example.smarthealthcareappointmentsystem.entites.Patient;
import com.example.smarthealthcareappointmentsystem.entites.Role;
import com.example.smarthealthcareappointmentsystem.mapper.DoctorMapper;
import com.example.smarthealthcareappointmentsystem.mapper.PatientMapper;
import com.example.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.example.smarthealthcareappointmentsystem.repository.PatientRepository;
import com.example.smarthealthcareappointmentsystem.security.CustomUserDetails;
import com.example.smarthealthcareappointmentsystem.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final DoctorRepository doctorRepository;
    private final DoctorMapper doctorMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public LoginResponseDto login(LoginRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Role role = userDetails.getRole();

        String token = jwtUtils.generateJwtToken(userDetails.getUsername(), role);
        return new LoginResponseDto(token, role.name());
    }
    @Override
    public String registerPatient(RequestPatientDto dto){
        if (patientRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use by another patient.");
        }
        Patient patient = patientMapper.toEntity(dto);

        patient.setPassword(passwordEncoder.encode(dto.getPassword()));

        patient.setRole(Role.PATIENT);

        patientRepository.save(patient);

        return "Patient registered successfully!";
    }
    @Override
    public String registerDoctor(RequestDoctorDto dto){
        if (doctorRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email is already in use by another doctor.");
        }

        Doctor doctor = doctorMapper.toEntity(dto);


        doctor.setPassword(passwordEncoder.encode(dto.getPassword()));

        doctor.setRole(Role.DOCTOR);

        doctorRepository.save(doctor);

        return "Doctor registered successfully!";
    }
}
