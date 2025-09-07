package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;

import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;

import java.util.List;

public interface DoctorService {

    List<DoctorDto> getAllDoctors();
    DoctorDto getDoctorById(Long id);
    void RemoveDoctorById(Long id);
    DoctorDto updateDoctorById(Long id, RequestDoctorDto doctorDto);
    DoctorDto addDoctor(RequestDoctorDto doctorDto);
    List<DoctorDto> searchDoctorsBySpecialty(String specialty);

}
