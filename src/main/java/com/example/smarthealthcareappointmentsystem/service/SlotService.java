package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.SlotDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestSlotDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface SlotService {
    SlotDto createSlot(Long doctorId, RequestSlotDto requestSlotDto);
    Page<SlotDto> getAllSlots(Long doctorId, Pageable pageable);
    SlotDto updateSlot(Long slotId, RequestSlotDto requestSlotDto);
    void deleteSlot(Long slotId);
    Page<SlotDto> getAvailableSlots(Long doctorId, Pageable pageable);
}
