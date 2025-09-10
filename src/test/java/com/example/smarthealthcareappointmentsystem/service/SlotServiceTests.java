package com.example.smarthealthcareappointmentsystem.service;


import com.example.smarthealthcareappointmentsystem.DTO.SlotDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestSlotDto;
import com.example.smarthealthcareappointmentsystem.entites.*;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.SlotMapper;
import com.example.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import com.example.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.example.smarthealthcareappointmentsystem.repository.SlotRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        doctor = Doctor.builder().id(1L).firstName("Dr. John").build();

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
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void getAllSlots_success() {
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
        when(slotRepository.findOverlappingSlots(anyLong(), any(), any())).thenReturn(List.of());
        when(slotRepository.save(any(Slot.class))).thenReturn(slot);
        when(slotMapper.toSlotDto(any(Slot.class))).thenReturn(slotDto);

        SlotDto result = slotService.updateSlot(slot.getId(), requestSlotDto);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(slot.getId());
    }

    @Test
    void updateSlot_notFound() {
        when(slotRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> slotService.updateSlot(99L, requestSlotDto))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteSlot_withAppointment() {
        Appointment appointment = Appointment.builder()
                .id(1L)
                .slot(slot)
                .status(AppointmentStatus.SCHEDULED)
                .build();

        when(slotRepository.findById(slot.getId())).thenReturn(Optional.of(slot));
        when(appointmentRepository.findBySlotId(slot.getId())).thenReturn(Optional.of(appointment));

        slotService.deleteSlot(slot.getId());

        assertThat(appointment.getStatus()).isEqualTo(AppointmentStatus.CANCELLED);
        verify(slotRepository).delete(slot);
    }

    @Test
    void deleteSlot_notFound() {
        when(slotRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> slotService.deleteSlot(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void getAvailableSlots_success() {
        Page<Slot> slots = new PageImpl<>(List.of(slot));
        when(slotRepository.findByDoctorIdAndAvailableTrue(eq(doctor.getId()), any(Pageable.class))).thenReturn(slots);
        when(slotMapper.toSlotDto(any(Slot.class))).thenReturn(slotDto);

        Page<SlotDto> result = slotService.getAvailableSlots(doctor.getId(), Pageable.unpaged());

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).isAvailable()).isTrue();
    }
}
