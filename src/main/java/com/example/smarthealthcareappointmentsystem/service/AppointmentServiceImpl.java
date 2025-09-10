package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.AppointmentDto;
import com.example.smarthealthcareappointmentsystem.entites.*;
import com.example.smarthealthcareappointmentsystem.exception.BadRequestException;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.AppointmentMapper;
import com.example.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import com.example.smarthealthcareappointmentsystem.repository.PatientRepository;
import com.example.smarthealthcareappointmentsystem.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final AppointmentMapper appointmentMapper;
    private final PatientRepository patientRepository;

    private final SlotRepository slotRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDto> getAppointments(Long doctorId, Pageable pageable) {
        return appointmentRepository.findBySlot_Doctor_Id(doctorId, pageable)
                .map(appointmentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentDto getAppointmentById(Long doctorId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndSlot_DoctorId(appointmentId, doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found for doctorId: " + doctorId));
        return appointmentMapper.toDto(appointment);
    }

    @Override
    public AppointmentDto markAppointmentAsCompleted(Long doctorId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findByIdAndSlot_DoctorId(appointmentId, doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found for doctorId: " + doctorId));
        appointment.setStatus(AppointmentStatus.COMPLETED);
        appointmentRepository.save(appointment);
        return appointmentMapper.toDto(appointment);
    }
    @Override
    public AppointmentDto bookAppointment(Long patientId, Long slotId) {
        // check if patient exist
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + patientId));
        // check if slot exist
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id " + slotId));
        // check if slot evailable
        if (!slot.isAvailable()) {
            throw new BadRequestException("Slot is already booked");
        }
        // check if slot is in the past
        if (slot.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Cannot book a slot in the past");
        }
        boolean hasConflict = appointmentRepository.existsByPatientIdAndSlotStartTime(patientId, slot.getStartTime());
        if (hasConflict) {
            throw new BadRequestException("Patient already has an appointment at this time");
        }




        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setSlot(slot);
        // status is scheduled and slot isnt availabe
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        slot.setAvailable(false);
        slotRepository.save(slot);

        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(saved);
    }
    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentDto> getPatientAppointments(Long patientId, Pageable pageable) {
        patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + patientId));

        return appointmentRepository.findByPatientId(patientId, pageable)
                .map(appointmentMapper::toDto);
    }

    public AppointmentDto cancelAppointment(Long patientId, Long appointmentId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id " + appointmentId));
        // check if appointment match the user
        if (!appointment.getPatient().getId().equals(patientId)) {
            throw new BadRequestException("This appointment does not belong to the patient");
        }

        appointment.setStatus(AppointmentStatus.CANCELLED);
        // reset slot free
        appointment.getSlot().setAvailable(true);
        slotRepository.save(appointment.getSlot());

        Appointment saved = appointmentRepository.save(appointment);
        return appointmentMapper.toDto(saved);
    }

}
