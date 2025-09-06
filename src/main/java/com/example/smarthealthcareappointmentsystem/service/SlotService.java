package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.SlotDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestSlotDto;

import java.util.List;

public interface SlotService {
    SlotDto createSlot(Long doctorId, RequestSlotDto requestSlotDto);
    List<SlotDto> getAllSlots(Long doctorId);
    SlotDto updateSlot(Long slotId, RequestSlotDto requestSlotDto);
    void deleteSlot(Long slotId);
}
