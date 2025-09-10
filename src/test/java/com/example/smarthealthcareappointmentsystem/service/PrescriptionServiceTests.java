package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;
import com.example.smarthealthcareappointmentsystem.entites.*;
import com.example.smarthealthcareappointmentsystem.entites.mongo.Prescription;
import com.example.smarthealthcareappointmentsystem.exception.BadRequestException;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.mongo.PrescriptionMapper;
import com.example.smarthealthcareappointmentsystem.repository.*;
import com.example.smarthealthcareappointmentsystem.security.CustomUserDetails;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PrescriptionServiceTests {

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

    private MockedStatic<SecurityContextHolder> mockedSecurityContextHolder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create patient
        patient = Patient.builder()
                .id(1L)
                .firstName("Kareem")
                .lastName("Qutob")
                .email("kareem@example.com")
                .phone("123456789")
                .password("password123")
                .role(Role.PATIENT)
                .dateOfBirth(LocalDate.of(1995, 5, 20))
                .build();

        // Create doctor
        doctor = Doctor.builder()
                .id(2L)
                .firstName("Dr")
                .lastName("Smith")
                .email("drsmith@example.com")
                .phone("987654321")
                .password("password123")
                .role(Role.DOCTOR)
                .specialty("Cardiology")
                .yearsOfExperience(10)
                .build();

        // Create slot
        Slot slot = Slot.builder()
                .id(10L)
                .doctor(doctor)
                .startTime(LocalDateTime.now().plusDays(1))
                .endTime(LocalDateTime.now().plusDays(1).plusHours(1))
                .available(true)
                .build();

        // Create appointment
        appointment = Appointment.builder()
                .id(3L)
                .patient(patient)
                .slot(slot)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        // Request DTO
        requestDto = RequestPrescriptionDto.builder()
                .patientId(patient.getId())
                .doctorId(doctor.getId())
                .appointmentId(appointment.getId())
                .medicines(List.of("Med1", "Med2"))
                .notes("Take twice daily")
                .build();

        // Prescription entity and DTO
        prescriptionEntity = new Prescription();
        prescriptionDto = new PrescriptionDto();
        prescriptionDto.setPatientId(patient.getId());
        prescriptionDto.setDoctorId(doctor.getId());
        prescriptionDto.setAppointmentId(appointment.getId());
        prescriptionDto.setNotes("Take twice daily");

        // Mock SecurityContextHolder
        CustomUserDetails currentUser = new CustomUserDetails(doctor.getEmail(),doctor.getPassword(),doctor.getRole());
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(currentUser);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(auth);

        mockedSecurityContextHolder = mockStatic(SecurityContextHolder.class);
        mockedSecurityContextHolder.when(SecurityContextHolder::getContext).thenReturn(securityContext);
    }

    @AfterEach
    void tearDown() {
        mockedSecurityContextHolder.close();
    }

    @Test
    void addPrescription_success() {
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByIdAndSlot_DoctorId(appointment.getId(), doctor.getId()))
                .thenReturn(Optional.of(appointment));
        when(prescriptionMapper.toEntity(requestDto)).thenReturn(prescriptionEntity);
        when(prescriptionRepository.save(any(Prescription.class))).thenReturn(prescriptionEntity);
        when(prescriptionMapper.toDto(prescriptionEntity)).thenReturn(prescriptionDto);

        PrescriptionDto result = prescriptionService.addPrescription(requestDto);

        assertNotNull(result);
        assertEquals(patient.getId(), result.getPatientId());
        assertEquals(doctor.getId(), result.getDoctorId());
        assertEquals(appointment.getId(), result.getAppointmentId());
        assertEquals("Take twice daily", result.getNotes());

        verify(medicalRecordService).addPrescriptionToRecord(patient.getId(), prescriptionEntity);
        verify(prescriptionRepository).save(any(Prescription.class));
    }

    @Test
    void addPrescription_patientNotFound() {
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> prescriptionService.addPrescription(requestDto));
    }

    @Test
    void addPrescription_doctorNotFound() {
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> prescriptionService.addPrescription(requestDto));
    }

    @Test
    void addPrescription_appointmentNotFound() {
        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByIdAndSlot_DoctorId(appointment.getId(), doctor.getId()))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> prescriptionService.addPrescription(requestDto));
    }

    @Test
    void addPrescription_appointmentCancelled() {
        appointment.setStatus(AppointmentStatus.CANCELLED);

        when(patientRepository.findById(patient.getId())).thenReturn(Optional.of(patient));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(appointmentRepository.findByIdAndSlot_DoctorId(appointment.getId(), doctor.getId()))
                .thenReturn(Optional.of(appointment));

        assertThrows(BadRequestException.class,
                () -> prescriptionService.addPrescription(requestDto));
    }
}
