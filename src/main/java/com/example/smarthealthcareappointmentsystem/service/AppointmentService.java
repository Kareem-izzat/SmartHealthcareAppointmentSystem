package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.AppointmentDto;

import java.util.List;

public interface AppointmentService {
    AppointmentDto getAppointmentById(Long doctorId, Long appointmentId);
    List<AppointmentDto> getAppointments(Long doctorId);
    AppointmentDto markAppointmentAsCompleted(Long doctorId, Long appointmentId);
    AppointmentDto bookAppointment(Long patientId,  Long slotId);
    List<AppointmentDto> getPatientAppointments(Long patientId);
    AppointmentDto cancelAppointment(Long patientId, Long slotId);


}
