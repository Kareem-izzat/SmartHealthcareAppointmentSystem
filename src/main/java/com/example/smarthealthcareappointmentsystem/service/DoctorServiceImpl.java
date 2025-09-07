package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.AppointmentDto;
import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.SlotDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestSlotDto;
import com.example.smarthealthcareappointmentsystem.entites.*;
import com.example.smarthealthcareappointmentsystem.entites.mongo.Prescription;
import com.example.smarthealthcareappointmentsystem.exception.BadRequestException;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.AppointmentMapper;
import com.example.smarthealthcareappointmentsystem.mapper.DoctorMapper;
import com.example.smarthealthcareappointmentsystem.mapper.SlotMapper;
import com.example.smarthealthcareappointmentsystem.mapper.mongo.PrescriptionMapper;
import com.example.smarthealthcareappointmentsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {

    private final DoctorMapper doctorMapper;
    private final DoctorRepository doctorRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public List<DoctorDto> getAllDoctors() {
        return doctorRepository.findAll().stream()
                .map(doctorMapper::toDto)
                .collect(Collectors.toList());
    }
    @Override
    public DoctorDto updateDoctorById(Long doctorId, RequestDoctorDto doctorDto){
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + doctorId));
        doctor.setFirstName(doctorDto.getFirstName());
        doctor.setLastName(doctorDto.getLastName());
        doctor.setEmail(doctorDto.getEmail());
        doctor.setPhone(doctorDto.getPhone());
        doctor.setSpecialty(doctorDto.getSpecialty());
        doctor.setYearsOfExperience(doctorDto.getYearsOfExperience());
        // only encode when provided
        if (doctorDto.getPassword() != null && !doctorDto.getPassword().isBlank()) {
            doctor.setPassword(passwordEncoder.encode(doctorDto.getPassword()));
        }
        return doctorMapper.toDto(doctorRepository.save(doctor));


    }
    @Override
    public DoctorDto getDoctorById(Long doctorId) {
        Doctor doctor=doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + doctorId));
        return doctorMapper.toDto(doctor);
    }
    @Override
    public DoctorDto addDoctor(RequestDoctorDto doctorDto) {
        Doctor doctor = doctorMapper.toEntity(doctorDto);
        doctor.setPassword(passwordEncoder.encode(doctorDto.getPassword()));
        doctor.setRole(Role.DOCTOR);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(savedDoctor);
    }
    @Override
    public void RemoveDoctorById(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id " + doctorId);
        }
        doctorRepository.deleteById(doctorId);
    }
    @Override
    public List<DoctorDto> searchDoctorsBySpecialty(String specialty) {
        return doctorRepository.findBySpecialty(specialty).stream()
                .map(doctorMapper::toDto)
                .collect(Collectors.toList());
    }


}
