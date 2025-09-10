package com.example.smarthealthcareappointmentsystem.crud;

import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;
import com.example.smarthealthcareappointmentsystem.entites.Doctor;
import com.example.smarthealthcareappointmentsystem.entites.Role;
import com.example.smarthealthcareappointmentsystem.exception.BadRequestException;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.DoctorMapper;
import com.example.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import com.example.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.example.smarthealthcareappointmentsystem.service.DoctorServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class DoctorServiceCrudTests {

    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DoctorMapper doctorMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;
    private DoctorDto doctorDto;
    private RequestDoctorDto requestDoctorDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doctor = Doctor.builder()
                .id(1L)
                .firstName("kareem")
                .lastName("qutob")
                .email("kareem@example.com")
                .phone("123456789")
                .password("encodedPassword")
                .specialty("Cardiology")
                .yearsOfExperience(10)
                .role(Role.DOCTOR)
                .build();

        doctorDto = DoctorDto.builder()
                .id(1L)
                .firstName("kareem")
                .lastName("qutob")
                .email("kareem@example.com")
                .phone("123456789")
                .specialty("Cardiology")
                .yearsOfExperience(10)
                .build();

        requestDoctorDto = RequestDoctorDto.builder()
                .firstName("kareem")
                .lastName("qutob")
                .email("kareem@example.com")
                .phone("123456789")
                .password("rawPassword")
                .specialty("Cardiology")
                .yearsOfExperience(10)
                .build();
    }

    @Test
    void testAddDoctor() {
        when(doctorRepository.existsByEmail(requestDoctorDto.getEmail())).thenReturn(false);
        when(doctorMapper.toEntity(requestDoctorDto)).thenReturn(doctor);
        when(passwordEncoder.encode(requestDoctorDto.getPassword())).thenReturn("encodedPassword");
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        DoctorDto result = doctorService.addDoctor(requestDoctorDto);

        assertNotNull(result);
        assertEquals("kareem", result.getFirstName());
        verify(doctorRepository, times(1)).save(any(Doctor.class));
    }

    @Test
    void testAddDoctorEmailAlreadyExists() {
        when(doctorRepository.existsByEmail(requestDoctorDto.getEmail())).thenReturn(true);
        assertThrows(BadRequestException.class, () -> doctorService.addDoctor(requestDoctorDto));
    }

    @Test
    void testGetDoctorById() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        DoctorDto result = doctorService.getDoctorById(1L);

        assertEquals("kareem", result.getFirstName());
        verify(doctorRepository).findById(1L);
    }

    @Test
    void testGetDoctorByIdThrowsResourceNotFoundException() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> doctorService.getDoctorById(1L));
    }

    @Test
    void testUpdateDoctorById() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(doctor));
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(doctorRepository.save(any(Doctor.class))).thenReturn(doctor);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        DoctorDto result = doctorService.updateDoctorById(1L, requestDoctorDto);

        assertEquals("kareem", result.getFirstName());
        verify(passwordEncoder, times(1)).encode("rawPassword");
        verify(doctorRepository).save(doctor);
    }

    @Test
    void testUpdateDoctorByIdThrowsResourceNotFoundException() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> doctorService.updateDoctorById(1L, requestDoctorDto));
    }

    @Test
    void testRemoveDoctorById() {
        when(doctorRepository.existsById(1L)).thenReturn(true);
        when(appointmentRepository.findBySlot_Doctor_IdAndSlot_StartTimeAfter(eq(1L), any()))
                .thenReturn(List.of()); // no appointments

        doctorService.RemoveDoctorById(1L);

        verify(doctorRepository).deleteById(1L);
    }


    @Test
    void testRemoveDoctorByIdThrowsResourceNotFoundException() {
        when(doctorRepository.existsById(1L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> doctorService.RemoveDoctorById(1L));
    }

    @Test
    void testSearchDoctorsBySpecialtyPaged() {
        Pageable pageable = PageRequest.of(0, 5);
        Page<Doctor> doctorPage = new PageImpl<>(List.of(doctor), pageable, 1);

        when(doctorRepository.findBySpecialtyIgnoreCase(anyString(), any(Pageable.class)))
                .thenReturn(doctorPage);
        when(doctorMapper.toDto(doctor)).thenReturn(doctorDto);

        Page<DoctorDto> result = doctorService.searchDoctorsBySpecialty("Cardiology", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Cardiology", result.getContent().get(0).getSpecialty());
    }
}
