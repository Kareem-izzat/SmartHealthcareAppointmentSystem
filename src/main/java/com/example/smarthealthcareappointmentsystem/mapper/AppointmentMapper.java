package com.example.smarthealthcareappointmentsystem.mapper;

import com.example.smarthealthcareappointmentsystem.DTO.AppointmentDto;
import com.example.smarthealthcareappointmentsystem.entites.Appointment;
import com.example.smarthealthcareappointmentsystem.entites.Doctor;
import com.example.smarthealthcareappointmentsystem.entites.Patient;
import org.springframework.stereotype.Component;


@Component
public class AppointmentMapper {

    public AppointmentDto toDto(Appointment appointment){
        if (appointment == null) return null;
        return AppointmentDto.builder()
                .id(appointment.getId())
                .doctorId(appointment.getDoctor().getId())
                .doctorName(appointment.getDoctor().getFirstName() + " "
                        + appointment.getDoctor().getLastName())
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getFirstName() + " "
                        + appointment.getPatient().getLastName())
                .startTime(appointment.getStartTime())
                .endTime(appointment.getEndTime())
                .status(appointment.getStatus())
                .build();
    }
    public Appointment toEntity(AppointmentDto dto, Doctor doctor, Patient patient) {
        if (dto == null) return null;

        return Appointment.builder()
                .id(dto.getId())
                .doctor(doctor)
                .patient(patient)
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .status(dto.getStatus())
                .build();
    }
}
