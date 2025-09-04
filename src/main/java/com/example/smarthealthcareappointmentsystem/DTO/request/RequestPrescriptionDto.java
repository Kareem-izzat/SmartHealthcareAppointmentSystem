package com.example.smarthealthcareappointmentsystem.DTO.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RequestPrescriptionDto {
    @NotNull(message = "Patient ID is required")
    private Long patientId;
    @NotNull(message = "Appointment ID is required")
    private Long appointmentId;

    @NotEmpty(message = "Medicines list cannot be empty")
    private List<String> medicines;
    @NotEmpty(message = "Notes cannot be empty")

    private String notes;
}