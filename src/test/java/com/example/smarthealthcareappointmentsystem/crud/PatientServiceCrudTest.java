package com.example.smarthealthcareappointmentsystem.crud;


import com.example.smarthealthcareappointmentsystem.DTO.PatientDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPatientDto;
import com.example.smarthealthcareappointmentsystem.entites.Patient;
import com.example.smarthealthcareappointmentsystem.entites.Role;
import com.example.smarthealthcareappointmentsystem.exception.BadRequestException;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.PatientMapper;
import com.example.smarthealthcareappointmentsystem.repository.PatientRepository;
import com.example.smarthealthcareappointmentsystem.service.PatientServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class PatientServiceCrudTest {
    @Mock
    private PatientRepository patientRepository;

    @Mock
    private PatientMapper patientMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PatientServiceImpl patientService;

    private Patient patient;
    private PatientDto patientDto;
    private RequestPatientDto requestPatientDto;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);


        patient = Patient.builder()
                .id(1L)
                .firstName("kareem")
                .lastName("qutob")
                .email("kareem@example.com")
                .phone("123456789")
                .password("encodedPassword")
                .dateOfBirth(LocalDate.parse("2025-09-07"))
                .medicalHistory("no previous history")
                .role(Role.PATIENT)
                .build();


        patientDto = PatientDto.builder()
                .id(1L)
                .firstName("kareem")
                .lastName("qutob")
                .email("kareem@example.com")
                .phone("123456789")
                .dateOfBirth(LocalDate.parse("2025-09-07"))
                .medicalHistory("no previous history")
                .build();


        requestPatientDto = RequestPatientDto.builder()
                .firstName("kareem")
                .lastName("qutob")
                .email("kareem@example.com")
                .phone("123456789")
                .password("rawPassword")
                .dateOfBirth(LocalDate.parse("2025-09-07"))
                .medicalHistory("no previous history")
                .build();
    }

    @Test
    void testAddPatient() {
        when(patientRepository.existsByEmail(requestPatientDto.getEmail())).thenReturn(false);
        when(patientMapper.toEntity(requestPatientDto)).thenReturn(patient);
        when(passwordEncoder.encode(requestPatientDto.getPassword())).thenReturn("encodedPassword");
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(patientMapper.toDto(patient)).thenReturn(patientDto);

        PatientDto result = patientService.addPatient(requestPatientDto);

        assertNotNull(result);
        assertEquals("kareem", result.getFirstName());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testAddPatientEmailAlreadyExists() {
        when(patientRepository.existsByEmail(requestPatientDto.getEmail())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> patientService.addPatient(requestPatientDto));
    }


    @Test
    void testGetPatientById() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientMapper.toDto(patient)).thenReturn(patientDto);

        PatientDto result = patientService.getPatientById(1L);

        assertEquals("kareem", result.getFirstName());
        verify(patientRepository).findById(1L);
    }

    @Test
    void testGetPatientByIdThrowsResourceNotFoundException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> patientService.getPatientById(1L));
    }


    @Test
    void testUpdatePatientById() {
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);
        when(patientMapper.toDto(patient)).thenReturn(patientDto);

        PatientDto result = patientService.updatePatient(1L, requestPatientDto);

        assertEquals("kareem", result.getFirstName());
        verify(passwordEncoder, times(1)).encode("rawPassword");
        verify(patientRepository).save(patient);
    }

    @Test
    void testUpdatePatientByIdThrowsResourceNotFoundException() {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> patientService.updatePatient(1L, requestPatientDto));
    }


    @Test
    void testRemovePatientById() {
        when(patientRepository.existsById(1L)).thenReturn(true);

        patientService.removePatient(1L);

        verify(patientRepository).deleteById(1L);
    }

    @Test
    void testRemovePatientByIdThrowsResourceNotFoundException() {
        when(patientRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> patientService.removePatient(1L));
    }



}
