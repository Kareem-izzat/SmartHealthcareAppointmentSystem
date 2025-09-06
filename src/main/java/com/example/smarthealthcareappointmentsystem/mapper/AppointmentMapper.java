package com.example.smarthealthcareappointmentsystem.mapper;

import com.example.smarthealthcareappointmentsystem.DTO.AppointmentDto;
import com.example.smarthealthcareappointmentsystem.entites.Appointment;
import com.example.smarthealthcareappointmentsystem.entites.Patient;
import com.example.smarthealthcareappointmentsystem.entites.Slot;
import org.springframework.stereotype.Component;

// mapper between dto and entity
@Component
public class AppointmentMapper {

    public AppointmentDto toDto(Appointment appointment) {
        if (appointment == null) return null;

        return AppointmentDto.builder()
                .id(appointment.getId())
                .slotId(appointment.getSlot().getId())
                .doctorId(appointment.getSlot().getDoctor().getId())
                .doctorName(appointment.getSlot().getDoctor().getFirstName() + " " +
                        appointment.getSlot().getDoctor().getLastName())
                .patientId(appointment.getPatient().getId())
                .patientName(appointment.getPatient().getFirstName() + " " +
                        appointment.getPatient().getLastName())
                .startTime(appointment.getSlot().getStartTime())
                .endTime(appointment.getSlot().getEndTime())
                .status(appointment.getStatus())
                .build();

    }
    public Appointment toEntity(AppointmentDto dto, Slot slot, Patient patient) {
        if (dto == null) return null;

        return Appointment.builder()
                .id(dto.getId())
                .slot(slot)
                .patient(patient)
                .status(dto.getStatus())
                .build();
    }

}
