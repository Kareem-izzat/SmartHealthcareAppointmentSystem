package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.AppointmentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AppointmentService {
    AppointmentDto getAppointmentById(Long doctorId, Long appointmentId);
    Page<AppointmentDto> getAppointments(Long doctorId, Pageable pageable);
    AppointmentDto markAppointmentAsCompleted(Long doctorId, Long appointmentId);
    AppointmentDto bookAppointment(Long patientId,  Long slotId);
    Page<AppointmentDto> getPatientAppointments(Long patientId, Pageable pageable);
    AppointmentDto cancelAppointment(Long patientId, Long slotId);


}
