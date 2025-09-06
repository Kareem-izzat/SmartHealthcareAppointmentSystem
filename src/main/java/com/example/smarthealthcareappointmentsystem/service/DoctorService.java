package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.AppointmentDto;
import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.SlotDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestSlotDto;

import java.util.List;

public interface DoctorService {

    List<DoctorDto> getAllDoctors();
    DoctorDto getDoctorById(Long id);
    void RemoveDoctorById(Long id);
    DoctorDto updateDoctorById(Long id, RequestDoctorDto doctorDto);
    DoctorDto addDoctor(RequestDoctorDto doctorDto);
    List<DoctorDto> searchDoctorsBySpecialty(String specialty);

}
