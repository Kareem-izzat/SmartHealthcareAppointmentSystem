package com.example.smarthealthcareappointmentsystem.mapper;

import com.example.smarthealthcareappointmentsystem.DTO.SlotDto;
import com.example.smarthealthcareappointmentsystem.entites.Slot;
import org.springframework.stereotype.Component;

@Component
public class SlotMapper {
    public SlotDto toSlotDto(Slot slot) {
        return SlotDto.builder()
                .id(slot.getId())
                .startTime(slot.getStartTime())
                .endTime(slot.getEndTime())
                .build();
    }

    public Slot toSlotEntity(SlotDto dto) {
        return Slot.builder()
                .id(dto.getId())
                .startTime(dto.getStartTime())
                .endTime(dto.getEndTime())
                .build();
    }
}
