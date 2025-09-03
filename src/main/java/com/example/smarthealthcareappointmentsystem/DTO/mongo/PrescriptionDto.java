package com.example.smarthealthcareappointmentsystem.DTO.mongo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PrescriptionDto {
    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;

    private Long appointmentId;
    private LocalDateTime date;

    private List<String> medicines;
    private String notes;
}
