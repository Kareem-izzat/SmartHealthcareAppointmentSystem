package com.example.smarthealthcareappointmentsystem.booking;

import com.example.smarthealthcareappointmentsystem.DTO.AppointmentDto;
import com.example.smarthealthcareappointmentsystem.entites.Appointment;
import com.example.smarthealthcareappointmentsystem.entites.AppointmentStatus;
import com.example.smarthealthcareappointmentsystem.entites.Patient;
import com.example.smarthealthcareappointmentsystem.entites.Slot;
import com.example.smarthealthcareappointmentsystem.exception.BadRequestException;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.AppointmentMapper;
import com.example.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import com.example.smarthealthcareappointmentsystem.repository.PatientRepository;
import com.example.smarthealthcareappointmentsystem.repository.SlotRepository;
import com.example.smarthealthcareappointmentsystem.service.AppointmentServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AppointmentServiceTests {

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private AppointmentMapper appointmentMapper;

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private SlotRepository slotRepository;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    private Patient patient;
    private Slot slot;
    private Appointment appointment;
    private AppointmentDto appointmentDto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        patient = new Patient();
        patient.setId(1L);
        patient.setFirstName("Kareem");

        slot = new Slot();
        slot.setId(10L);
        slot.setAvailable(true);
        slot.setStartTime(LocalDateTime.now().plusHours(1));
        slot.setEndTime(LocalDateTime.now().plusHours(2));

        appointment = new Appointment();
        appointment.setId(100L);
        appointment.setPatient(patient);
        appointment.setSlot(slot);
        appointment.setStatus(AppointmentStatus.SCHEDULED);

        appointmentDto = new AppointmentDto();
        appointmentDto.setId(100L);
    }

    @Test
    void testBookAppointmentSuccess() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(slotRepository.findById(10L)).thenReturn(Optional.of(slot));
        when(appointmentRepository.save(any(Appointment.class))).thenReturn(appointment);
        when(appointmentMapper.toDto(any(Appointment.class))).thenReturn(appointmentDto);

        AppointmentDto result = appointmentService.bookAppointment(1L, 10L);

        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertFalse(slot.isAvailable()); // slot should be marked unavailable
        verify(slotRepository, times(1)).save(slot);
        verify(appointmentRepository, times(1)).save(any(Appointment.class));
    }

    @Test
    void testBookAppointmentPatientNotFoundThrowsResourceNotFoundException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.bookAppointment(1L, 10L));

        verifyNoInteractions(slotRepository);
        verifyNoInteractions(appointmentRepository);
    }

    @Test
    void testBookAppointmentSlotNotFoundThrowsResourceNotFoundException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(slotRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> appointmentService.bookAppointment(1L, 10L));

        verifyNoInteractions(appointmentRepository);
        verifyNoInteractions(slotRepository);
    }

    @Test
    void testBookAppointmentSlotAlreadyBooked() {
        slot.setAvailable(false); // already booked
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(slotRepository.findById(10L)).thenReturn(Optional.of(slot));

        assertThrows(BadRequestException.class,
                () -> appointmentService.bookAppointment(1L, 10L));

        verify(appointmentRepository, never()).save(any());
        verify(slotRepository, never()).save(any());
    }
}
