package com.example.smarthealthcareappointmentsystem.mapper;

import com.example.smarthealthcareappointmentsystem.DTO.SlotDto;
import com.example.smarthealthcareappointmentsystem.entites.Slot;
import org.springframework.stereotype.Component;

@Component
// mapper between dto and entity
public class SlotMapper {
    public SlotDto toSlotDto(Slot slot) {
        return SlotDto.builder()
                .id(slot.getId())
                .doctorId(slot.getDoctor() != null ? slot.getDoctor().getId() : null) // in case is null it will stay null
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .available(slot.isAvailable())
                .build();
    }

    public Slot toSlotEntity(SlotDto dto) {
        return Slot.builder()
                .id(dto.getId())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .available(dto.isAvailable())
                .build();
    }
}
