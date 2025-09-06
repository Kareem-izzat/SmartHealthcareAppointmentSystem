package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;
import com.example.smarthealthcareappointmentsystem.entites.Appointment;
import com.example.smarthealthcareappointmentsystem.entites.Doctor;
import com.example.smarthealthcareappointmentsystem.entites.Patient;
import com.example.smarthealthcareappointmentsystem.entites.mongo.Prescription;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.mongo.PrescriptionMapper;
import com.example.smarthealthcareappointmentsystem.repository.*;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
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
    public List<PrescriptionDto> getPrescriptionsByDoctor(Long doctorId) {
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(()-> new ResourceNotFoundException("Doctor not found with id" + doctorId));
        return prescriptionRepository.findByDoctorId(doctorId).stream()
                .map(prescriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionDto> getPrescriptionsByPatient(Long patientId) {
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id " + patientId));

        return prescriptionRepository.findByPatientId(patientId).stream()
                .map(prescriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public PrescriptionDto addPrescription(RequestPrescriptionDto dto) {
        Patient patient = patientRepository.findById(dto.getPatientId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Patient not found with id: " + dto.getPatientId()));

        Doctor doctor = doctorRepository.findById(dto.getDoctorId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Doctor not found with id: " + dto.getDoctorId()));


        Appointment appointment = appointmentRepository.findByIdAndSlot_Doctor_Id(dto.getAppointmentId(), doctor.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Appointment not found with id: " + dto.getAppointmentId() + " for doctorId: " + doctor.getId()
                ));


        Prescription prescription = prescriptionMapper.toEntity(dto);
        prescription.setDoctorId(doctor.getId());
        prescription.setDoctorName(doctor.getFirstName() + " " + doctor.getLastName());
        prescription.setPatientId(patient.getId());
        prescription.setPatientName(patient.getFirstName() + " " + patient.getLastName());
        prescription.setDate(LocalDateTime.now());
        prescription.setAppointmentId(appointment.getId());


        Prescription savedPrescription = prescriptionRepository.save(prescription);


        medicalRecordService.addPrescriptionToRecord(patient.getId(), savedPrescription);


        return prescriptionMapper.toDto(savedPrescription);
    }
}
