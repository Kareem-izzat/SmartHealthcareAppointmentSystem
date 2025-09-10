package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.SlotDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestSlotDto;
import com.example.smarthealthcareappointmentsystem.entites.*;
import com.example.smarthealthcareappointmentsystem.exception.BadRequestException;
import com.example.smarthealthcareappointmentsystem.repository.*;
import com.example.smarthealthcareappointmentsystem.mapper.SlotMapper;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.security.CustomUserDetails;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SlotServiceTests {

    @Mock
    private SlotMapper slotMapper;
    @Mock
    private SlotRepository slotRepository;
    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private AppointmentRepository appointmentRepository;

    @InjectMocks
    private SlotServiceImpl slotService;

    private Doctor doctor;
    private Slot slot;
    private RequestSlotDto requestSlotDto;
    private SlotDto slotDto;

    private MockedStatic<SecurityContextHolder> mockedSecurityContextHolder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doctor = Doctor.builder()
                .id(1L)
                .firstName("Dr. John")
                .email("dr.john@example.com")  // add email!
                .build();


        requestSlotDto = RequestSlotDto.builder()
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .Available(true)
                .build();

        slot = Slot.builder()
                .id(10L)
                .doctor(doctor)
                .startTime(requestSlotDto.getStartTime())
                .endTime(requestSlotDto.getEndTime())
                .available(true)
                .build();

        slotDto = SlotDto.builder()
                .id(10L)
                .doctorId(doctor.getId())
                .startTime(requestSlotDto.getStartTime())
                .endTime(requestSlotDto.getEndTime())
                .available(true)
                .build();

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
    void createSlot_success() {
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(slotRepository.findOverlappingSlots(anyLong(), any(), any())).thenReturn(List.of());
        when(slotRepository.save(any(Slot.class))).thenReturn(slot);
        when(slotMapper.toSlotDto(any(Slot.class))).thenReturn(slotDto);

        SlotDto result = slotService.createSlot(doctor.getId(), requestSlotDto);

        assertThat(result).isNotNull();
        assertThat(result.getDoctorId()).isEqualTo(doctor.getId());
        verify(slotRepository).save(any(Slot.class));
    }

    @Test
    void createSlot_doctorNotFound() {
        when(doctorRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> slotService.createSlot(99L, requestSlotDto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void createSlot_overlapping() {
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(slotRepository.findOverlappingSlots(anyLong(), any(), any())).thenReturn(List.of(slot));

        assertThatThrownBy(() -> slotService.createSlot(doctor.getId(), requestSlotDto))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Slot overlaps");

    }

    @Test
    void getAllSlots_success() {
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        Page<Slot> slots = new PageImpl<>(List.of(slot));
        when(slotRepository.findByDoctorId(eq(doctor.getId()), any(Pageable.class))).thenReturn(slots);
        when(slotMapper.toSlotDto(any(Slot.class))).thenReturn(slotDto);

        Page<SlotDto> result = slotService.getAllSlots(doctor.getId(), Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getDoctorId()).isEqualTo(doctor.getId());
    }

    @Test
    void updateSlot_success() {
        when(slotRepository.findById(slot.getId())).thenReturn(Optional.of(slot));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        when(slotRepository.findOverlappingSlots(anyLong(), any(), any())).thenReturn(List.of());
        when(slotRepository.save(any(Slot.class))).thenReturn(slot);
        when(slotMapper.toSlotDto(any(Slot.class))).thenReturn(slotDto);

        SlotDto result = slotService.updateSlot(slot.getId(), requestSlotDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(slot.getId());
    }

    @Test
    void deleteSlot_withAppointment() {
        when(slotRepository.findById(slot.getId())).thenReturn(Optional.of(slot));
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));

        Appointment appointment = Appointment.builder()
                .id(1L)
                .slot(slot)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(appointmentRepository.findBySlotId(slot.getId())).thenReturn(Optional.of(appointment));

        slotService.deleteSlot(slot.getId());

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
        verify(slotRepository).delete(slot);
    }

    @Test
    void getAvailableSlots_success() {
        when(doctorRepository.findById(doctor.getId())).thenReturn(Optional.of(doctor));
        Page<Slot> slots = new PageImpl<>(List.of(slot));
        when(slotRepository.findByDoctorIdAndAvailableTrue(eq(doctor.getId()), any(Pageable.class))).thenReturn(slots);
        when(slotMapper.toSlotDto(any(Slot.class))).thenReturn(slotDto);

        Page<SlotDto> result = slotService.getAvailableSlots(doctor.getId(), Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).isAvailable()).isTrue();
    }
}
