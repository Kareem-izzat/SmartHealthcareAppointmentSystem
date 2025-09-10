package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.SlotDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestSlotDto;
import com.example.smarthealthcareappointmentsystem.entites.Appointment;
import com.example.smarthealthcareappointmentsystem.entites.AppointmentStatus;
import com.example.smarthealthcareappointmentsystem.entites.Doctor;
import com.example.smarthealthcareappointmentsystem.entites.Slot;
import com.example.smarthealthcareappointmentsystem.exception.BadRequestException;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.mapper.SlotMapper;
import com.example.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import com.example.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.example.smarthealthcareappointmentsystem.repository.SlotRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Service
@Transactional
@RequiredArgsConstructor
public class SlotServiceImpl implements  SlotService {

    private final SlotMapper slotMapper;
    private final SlotRepository slotRepository;
    private final DoctorRepository doctorRepository;
    private final AppointmentRepository appointmentRepository;

    @Override
    public SlotDto createSlot(Long doctorId, RequestSlotDto requestSlotDto) {
        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id "+ doctorId));
        // check overlapping
        if (requestSlotDto.getStartTime().isAfter(requestSlotDto.getEndTime()) ||
                requestSlotDto.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Invalid slot time.");
        }
        List<Slot> overlappingSlots=slotRepository.findOverlappingSlots( doctorId,
                requestSlotDto.getStartTime(),
                requestSlotDto.getEndTime());
        if (!overlappingSlots.isEmpty()) {
            throw new IllegalArgumentException("Slot overlaps with an existing slot.");
        }
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
    @Transactional(readOnly = true)
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
        if (requestSlotDto.getStartTime().isAfter(requestSlotDto.getEndTime()) ||
                requestSlotDto.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Invalid slot time.");
        }
        // check overlapping excluding the updated slot
        List<Slot> overlappingSlots = slotRepository.findOverlappingSlots(
                        slot.getDoctor().getId(),
                        requestSlotDto.getStartTime(),
                        requestSlotDto.getEndTime()
                ).stream()
                .filter(s -> !s.getId().equals(slotId))
                .toList();
        if (!overlappingSlots.isEmpty()) {
            throw new BadRequestException("Updated slot overlaps with an existing slot.");
        }

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

        Optional<Appointment> bookedAppointmentOpt = appointmentRepository.findBySlotId(slotId);
        // cancel the appointment if exist
        bookedAppointmentOpt.ifPresent(appointment -> {
            appointment.setStatus(AppointmentStatus.CANCELLED);
            appointmentRepository.save(appointment);
        });
        slotRepository.delete(slot);
    }
    @Override
    @Transactional(readOnly = true)
    public List<SlotDto> getAvailableSlots(Long doctorId) {
        return slotRepository.findByDoctorId(doctorId).stream()
                .filter(Slot::isAvailable)
                .map(slotMapper::toSlotDto)
                .toList();
    }
}
