package com.example.smarthealthcareappointmentsystem.service;


import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;

import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;

import com.example.smarthealthcareappointmentsystem.entites.*;

import com.example.smarthealthcareappointmentsystem.exception.BadRequestException;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;

import com.example.smarthealthcareappointmentsystem.mapper.DoctorMapper;

import com.example.smarthealthcareappointmentsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final AppointmentRepository appointmentRepository;


    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "doctors")
    public Page<DoctorDto> getAllDoctors(Pageable pageable) {
        return doctorRepository.findAll(pageable)
                .map(doctorMapper::toDto);
    }

    @Override
    @CacheEvict(value = {"doctors", "doctorsBySpecialty"}, allEntries = true)
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
    @Transactional(readOnly = true)
    @Cacheable(value = "doctors", key = "#doctorId")
    public DoctorDto getDoctorById(Long doctorId) {
        Doctor doctor=doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + doctorId));
        return doctorMapper.toDto(doctor);
    }
    @Override
    @CacheEvict(value = {"doctors", "doctorsBySpecialty"}, allEntries = true)
    public DoctorDto addDoctor(RequestDoctorDto doctorDto) {
        if (doctorRepository.existsByEmail(doctorDto.getEmail())) {
            throw new BadRequestException("Email is already in use");
        }
        Doctor doctor = doctorMapper.toEntity(doctorDto);
        doctor.setPassword(passwordEncoder.encode(doctorDto.getPassword()));
        doctor.setRole(Role.DOCTOR);
        Doctor savedDoctor = doctorRepository.save(doctor);
        return doctorMapper.toDto(savedDoctor);
    }
    @Override
    @CacheEvict(value = {"doctors", "doctorsBySpecialty"}, allEntries = true)
    public void RemoveDoctorById(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResourceNotFoundException("Doctor not found with id " + doctorId);
        }
        List<Appointment> futureAppointments = appointmentRepository.findBySlot_Doctor_IdAndSlot_StartTimeAfter(
                doctorId, LocalDateTime.now()
        );
        // cancel any future appointment for doctor
        for (Appointment appointment : futureAppointments) {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);

        }
        doctorRepository.deleteById(doctorId);
    }
    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "doctorsBySpecialty", key = "#specialty")
    public Page<DoctorDto> searchDoctorsBySpecialty(String specialty, Pageable pageable) {
        return doctorRepository.findBySpecialtyIgnoreCase(specialty, pageable)
                .map(doctorMapper::toDto);
    }


}
