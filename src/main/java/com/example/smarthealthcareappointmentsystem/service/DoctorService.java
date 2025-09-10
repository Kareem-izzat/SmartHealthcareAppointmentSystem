package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;

import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DoctorService {

    Page<DoctorDto> getAllDoctors(Pageable pageable);
    DoctorDto getDoctorById(Long id);
    void RemoveDoctorById(Long id);
    DoctorDto updateDoctorById(Long id, RequestDoctorDto doctorDto);
    DoctorDto addDoctor(RequestDoctorDto doctorDto);
    Page<DoctorDto> searchDoctorsBySpecialty(String specialty, Pageable pageable);

}
