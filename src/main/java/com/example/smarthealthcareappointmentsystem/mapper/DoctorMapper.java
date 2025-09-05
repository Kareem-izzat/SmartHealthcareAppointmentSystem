package com.example.smarthealthcareappointmentsystem.mapper;

import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;
import com.example.smarthealthcareappointmentsystem.entites.Doctor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DoctorMapper {
    private SlotMapper slotMapper;
    public DoctorDto toDto(Doctor doctor) {
        if (doctor == null) return null;

        return DoctorDto.builder()
                .id(doctor.getId())
                .firstName(doctor.getFirstName())
                .lastName(doctor.getLastName())
                .email(doctor.getEmail())
                .phone(doctor.getPhone())
                .specialty(doctor.getSpecialty())
                .yearsOfExperience(doctor.getYearsOfExperience())
                .slots(doctor.getSlots() != null ?
                        doctor.getSlots().stream()
                                .map(slotMapper::toSlotDto)
                                .collect(Collectors.toList()) : null)
                .build();
    }

    public Doctor toEntity(DoctorDto dto) {
        if (dto == null) return null;

        Doctor doctor = Doctor.builder()
                .id(dto.getId())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .specialty(dto.getSpecialty())
                .yearsOfExperience(dto.getYearsOfExperience())
                .build();

        if (dto.getSlots() != null) {
            doctor.setSlots(dto.getSlots().stream()
                    .map(slotMapper::toSlotEntity)
                    .collect(Collectors.toList()));
        }
        return doctor;
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
