package com.example.smarthealthcareappointmentsystem.DTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestAppointmentDto {
    @NotNull(message = "Slot ID is required")
    private Long slotId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;
}