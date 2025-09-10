package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;
import com.example.smarthealthcareappointmentsystem.entites.*;
import com.example.smarthealthcareappointmentsystem.entites.mongo.Prescription;
import com.example.smarthealthcareappointmentsystem.exception.BadRequestException;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.exception.UnauthorizedException;
import com.example.smarthealthcareappointmentsystem.mapper.mongo.PrescriptionMapper;
import com.example.smarthealthcareappointmentsystem.repository.*;

import com.example.smarthealthcareappointmentsystem.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class PrescriptionServiceImpl implements PrescriptionService {
    private final  PrescriptionRepository prescriptionRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final PatientRepository patientRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalRecordService medicalRecordService;

    @Override
    @Transactional(readOnly = true)
    public Page<PrescriptionDto> getPrescriptionsByDoctor(Long doctorId, Pageable pageable) {
        Doctor doctor =doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id " + doctorId));
        CustomUserDetails currentUser =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if (!doctor.getEmail().equals(currentUser.getEmail())) {
            throw new UnauthorizedException("You are not authorized to view this information");
        }

        return prescriptionRepository.findByDoctorId(doctorId, pageable)
                .map(prescriptionMapper::toDto);
    }


    @Override
    @Transactional(readOnly = true)
    public Page<PrescriptionDto> getPrescriptionsByPatient(Long patientId, Pageable pageable) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + patientId));
        CustomUserDetails currentUser =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // Check authorization
        if (currentUser.getRole() == Role.DOCTOR ||
                (!currentUser.getEmail().equals(patient.getEmail()) && currentUser.getRole() == Role.PATIENT)) {
            throw new UnauthorizedException("You are not authorized to view prescriptions for this patient.");
        }
        return prescriptionRepository.findByPatientId(patientId, pageable)
                .map(prescriptionMapper::toDto);
    }


    @Override
    public PrescriptionDto addPrescription(RequestPrescriptionDto dto) {
        // check existance for params
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + dto.getPatientId()));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + dto.getDoctorId()));
        CustomUserDetails currentUser =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if (!doctor.getEmail().equals(currentUser.getEmail())) {
            throw new UnauthorizedException("You are not authorized to do this operation");
        }



        Appointment appointment = appointmentRepository.findByIdAndSlot_DoctorId(dto.getAppointmentId(), doctor.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + dto.getAppointmentId() + " for doctorId: " + doctor.getId()
                ));
        if (!doctor.getId().equals(appointment.getSlot().getDoctor().getId())) {
            throw new BadRequestException("You cannot add a prescription for an appointment you do not own.");
        }



        if (appointment.getStatus() == AppointmentStatus.CANCELLED) {
            throw new BadRequestException("Cannot add prescription to a canceled appointment");
        }



        Prescription prescription = prescriptionMapper.toEntity(dto);
        prescription.setDoctorId(doctor.getId());
        prescription.setDoctorName(doctor.getFirstName() + " " + doctor.getLastName());
        prescription.setPatientId(patient.getId());
        prescription.setPatientName(patient.getFirstName() + " " + patient.getLastName());
        prescription.setDate(LocalDateTime.now());
        prescription.setAppointmentId(appointment.getId());


        Prescription savedPrescription = prescriptionRepository.save(prescription);

        // append prescriprion to record
        medicalRecordService.addPrescriptionToRecord(patient.getId(), savedPrescription);


        return prescriptionMapper.toDto(savedPrescription);
    }
}
