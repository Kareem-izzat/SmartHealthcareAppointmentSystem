package com.example.smarthealthcareappointmentsystem.DTO.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestAppointmentDto {
    @NotNull(message = "Slot ID is required")
    private Long slotId;

    @NotNull(message = "Patient ID is required")
    private Long patientId;
}