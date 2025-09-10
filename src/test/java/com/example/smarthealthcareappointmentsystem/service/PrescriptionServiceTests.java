package com.example.smarthealthcareappointmentsystem.service;


import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;
import com.example.smarthealthcareappointmentsystem.entites.*;
import com.example.smarthealthcareappointmentsystem.entites.mongo.Prescription;
import com.example.smarthealthcareappointmentsystem.exception.BadRequestException;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.mongo.PrescriptionMapper;
import com.example.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import com.example.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.example.smarthealthcareappointmentsystem.repository.PatientRepository;
import com.example.smarthealthcareappointmentsystem.repository.PrescriptionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrescriptionServiceImplTest {

    @Mock
    private PrescriptionRepository prescriptionRepository;
    @Mock
    private PrescriptionMapper prescriptionMapper;
    @Mock
    private PatientRepository patientRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private MedicalRecordService medicalRecordService;

    @InjectMocks
    private PrescriptionServiceImpl prescriptionService;

    private Patient patient;
    private Doctor doctor;
    private Appointment appointment;
    private RequestPrescriptionDto requestDto;
    private Prescription prescriptionEntity;
    private PrescriptionDto prescriptionDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = Patient.builder()
                .id(1L)
                .firstName("Kareem")
                .lastName("Qutob")
                .build();

        doctor = Doctor.builder()
                .id(2L)
                .firstName("Kareem")
                .lastName("Qutob")
                .build();

        Slot slot = Slot.builder().id(5L).build();
        appointment = Appointment.builder()
                .id(3L)
                .slot(slot)
                .patient(patient)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        requestDto = RequestPrescriptionDto.builder()
                .patientId(1L)
                .doctorId(2L)
                .appointmentId(3L)
                .medicines(List.of("Med1", "Med2"))
                .notes("Take twice daily")
                .build();

        prescriptionEntity = Prescription.builder()
                .id("abc123")
                .patientId(1L)
                .patientName("Kareem Qutob")
                .doctorId(2L)
                .doctorName("Kareem Qutob")
                .appointmentId(3L)
                .date(LocalDateTime.now())
                .medicines(List.of("Med1", "Med2"))
                .notes("Take twice daily")
                .build();

        prescriptionDto = PrescriptionDto.builder()
                .patientId(1L)
                .patientName("Kareem Qutob")
                .doctorId(2L)
                .doctorName("Kareem Qutob")
                .appointmentId(3L)
                .date(LocalDateTime.now())
                .medicines(List.of("Med1", "Med2"))
                .notes("Take twice daily")
                .build();
    }

    @Test
    void addPrescription_success() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByIdAndSlot_DoctorId(3L, 2L)).thenReturn(Optional.of(appointment));
        when(prescriptionMapper.toEntity(requestDto)).thenReturn(prescriptionEntity);
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescriptionEntity);
        when(prescriptionMapper.toDto(prescriptionEntity)).thenReturn(prescriptionDto);

        PrescriptionDto result = prescriptionService.addPrescription(requestDto);

        assertNotNull(result);
        assertEquals(1L, result.getPatientId());
        assertEquals(2L, result.getDoctorId());
        assertEquals(3L, result.getAppointmentId());
        assertEquals("Take twice daily", result.getNotes());

        verify(medicalRecordService).addPrescriptionToRecord(1L, prescriptionEntity);
        verify(prescriptionRepository).save(any(Prescription.class));
    }

    @Test
    void addPrescription_patientNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> prescriptionService.addPrescription(requestDto));
    }

    @Test
    void addPrescription_doctorNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> prescriptionService.addPrescription(requestDto));
    }

    @Test
    void addPrescription_appointmentNotFound() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByIdAndSlot_DoctorId(3L, 2L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> prescriptionService.addPrescription(requestDto));
    }

    @Test
    void addPrescription_appointmentCancelled() {
        appointment.setStatus(AppointmentStatus.CANCELLED);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByIdAndSlot_DoctorId(3L, 2L)).thenReturn(Optional.of(appointment));

        assertThrows(BadRequestException.class,
                () -> prescriptionService.addPrescription(requestDto));
    }

}

