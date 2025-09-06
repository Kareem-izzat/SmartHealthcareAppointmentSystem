package com.example.smarthealthcareappointmentsystem.mapper;

import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;
import com.example.smarthealthcareappointmentsystem.entites.Doctor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DoctorMapper {
    private final SlotMapper slotMapper;
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
