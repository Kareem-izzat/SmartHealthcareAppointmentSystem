package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.AppointmentDto;
import com.example.smarthealthcareappointmentsystem.entites.Appointment;
import com.example.smarthealthcareappointmentsystem.entites.AppointmentStatus;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.AppointmentMapper;
import com.example.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;

    @Override
    public List<AppointmentDto> getAppointments(Long doctorId) {
        return appointmentRepository.findByDoctorId(doctorId).stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public AppointmentDto getAppointmentById(Long doctorId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndDoctorId(appointmentId, doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found for doctorId: " + doctorId));
        return appointmentMapper.toDto(appointment);
    }

    @Override
    public AppointmentDto markAppointmentAsCompleted(Long doctorId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndDoctorId(appointmentId, doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found for doctorId: " + doctorId));
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }
}
