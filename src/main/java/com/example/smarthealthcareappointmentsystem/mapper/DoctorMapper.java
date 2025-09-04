package com.example.smarthealthcareappointmentsystem.mapper;

import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;
import com.example.smarthealthcareappointmentsystem.entites.Doctor;
import org.springframework.stereotype.Component;

@Component
public class DoctorMapper {

    public   DoctorDto toDto(Doctor doctor) {
        if (doctor == null) return null;
        return DoctorDto.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .specialty(doctor.getSpecialty())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .build();
    }

    public   Doctor toEntity(DoctorDto dto) {
        if (dto == null) return null;
        return Doctor.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .specialty(dto.getSpecialty())
                .yearsOfExperience(dto.getYearsOfExperience())
                .build();
    }
    public Doctor toEntity(RequestDoctorDto requestDto) {
        if (requestDto == null) return null;
        return Doctor.builder()
                .firstName(requestDto.getFirstName())
                .lastName(requestDto.getLastName())
                .email(requestDto.getEmail())
                .phone(requestDto.getPhone())
                .specialty(requestDto.getSpecialty())
                .yearsOfExperience(requestDto.getYearsOfExperience())
                .password(requestDto.getPassword())
                .build();
    }

}
