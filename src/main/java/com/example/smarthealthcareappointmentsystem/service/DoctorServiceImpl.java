package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.AppointmentDto;
import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.SlotDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestSlotDto;
import com.example.smarthealthcareappointmentsystem.entites.*;
import com.example.smarthealthcareappointmentsystem.entites.mongo.Prescription;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.AppointmentMapper;
import com.example.smarthealthcareappointmentsystem.mapper.SlotMapper;
import com.example.smarthealthcareappointmentsystem.mapper.mongo.PrescriptionMapper;
import com.example.smarthealthcareappointmentsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DoctorServiceImpl implements DoctorService {
    private final AppointmentRepository appointmentRepository;
    private final PrescriptionRepository prescriptionRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final PrescriptionMapper prescriptionMapper;
    private final AppointmentMapper appointmentMapper;
    private final MedicalRecordService medicalRecordService;
    private final SlotRepository slotRepository;
    private final SlotMapper slotMapper;

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

    @Override
    public List<PrescriptionDto> getPrescriptionsByDoctor(Long doctorId) {
        return prescriptionRepository.findByDoctorId(doctorId).stream()
                .map(prescriptionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<PrescriptionDto> getPrescriptionsByPatient(Long patientId) {
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


        Appointment appointment = appointmentRepository.findByIdAndDoctorId(dto.getAppointmentId(), doctor.getId())
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
    @Override
    public SlotDto createSlot(Long doctorId, RequestSlotDto requestSlotDto) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id "+ doctorId));

        Slot slot = Slot.builder()
                .doctor(doctor)
                .startTime(requestSlotDto.getStartTime())
                .endTime(requestSlotDto.getEndTime())
                .available(true)
                .build();

        slot = slotRepository.save(slot);
        return slotMapper.toSlotDto(slot);
    }
    @Override
    public List<SlotDto> getAllSlots(Long doctorId) {
        return slotRepository.findByDoctorId(doctorId)
                .stream()
                .map(slotMapper::toSlotDto)
                .collect(Collectors.toList());
    }
    @Override
    public SlotDto updateSlot(Long slotId, RequestSlotDto requestSlotDto) {
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + slotId));

        slot.setStartTime(requestSlotDto.getStartTime());
        slot.setEndTime(requestSlotDto.getEndTime());
        slot.setAvailable(requestSlotDto.isAvailable());

        slotRepository.save(slot);

        return slotMapper.toSlotDto(slot);
    }

    @Override
    public void deleteSlot(Long slotId) {
        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + slotId));
        slotRepository.delete(slot);
    }
}
