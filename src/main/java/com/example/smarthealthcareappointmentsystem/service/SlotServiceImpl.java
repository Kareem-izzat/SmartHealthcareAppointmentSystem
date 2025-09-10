package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.SlotDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestSlotDto;
import com.example.smarthealthcareappointmentsystem.entites.Appointment;
import com.example.smarthealthcareappointmentsystem.entites.AppointmentStatus;
import com.example.smarthealthcareappointmentsystem.entites.Doctor;
import com.example.smarthealthcareappointmentsystem.entites.Slot;
import com.example.smarthealthcareappointmentsystem.exception.BadRequestException;
import com.example.smarthealthcareappointmentsystem.exception.ResourceNotFoundException;
import com.example.smarthealthcareappointmentsystem.exception.UnauthorizedException;
import com.example.smarthealthcareappointmentsystem.mapper.SlotMapper;
import com.example.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import com.example.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.example.smarthealthcareappointmentsystem.repository.SlotRepository;
import com.example.smarthealthcareappointmentsystem.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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
        checkAuth(doctor);

        // check overlapping
        if (requestSlotDto.getStartTime().isAfter(requestSlotDto.getEndTime()) ||
                requestSlotDto.getStartTime().isBefore(LocalDateTime.now())) {
            throw new BadRequestException("Invalid slot time.");
        }
        List<Slot> overlappingSlots=slotRepository.findOverlappingSlots( doctorId,
                requestSlotDto.getStartTime(),
                requestSlotDto.getEndTime());
        if (!overlappingSlots.isEmpty()) {
            throw new BadRequestException("Slot overlaps with an existing slot.");
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
    public Page<SlotDto> getAllSlots(Long doctorId, Pageable pageable) {


        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id "+ doctorId));

        checkAuth(doctor);
        return slotRepository.findByDoctorId(doctorId, pageable)
                .map(slotMapper::toSlotDto);
    }
    @Override
    public SlotDto updateSlot(Long slotId, RequestSlotDto requestSlotDto) {


        Slot slot = slotRepository.findById(slotId)
                .orElseThrow(() -> new ResourceNotFoundException("Slot not found with id: " + slotId));
        Doctor doctor = doctorRepository.findById(slot.getDoctor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id "+ slot.getDoctor().getId()));
        checkAuth(doctor);
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
        Doctor doctor = doctorRepository.findById(slot.getDoctor().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id "+ slot.getDoctor().getId()));
        checkAuth(doctor);

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
    public Page<SlotDto> getAvailableSlots(Long doctorId, Pageable pageable) {
         doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor not found with id "+ doctorId));
        return slotRepository.findByDoctorIdAndAvailableTrue(doctorId, pageable)
                .map(slotMapper::toSlotDto);
    }
    private void checkAuth(Doctor doctor ) {
        CustomUserDetails currentUser =
                (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();


        if (!doctor.getEmail().equals(currentUser.getEmail())) {
            throw new UnauthorizedException("You are not authorized to manage this slot.");
        }
    }

}
