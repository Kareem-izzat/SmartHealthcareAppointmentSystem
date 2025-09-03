package com.example.smarthealthcareappointmentsystem.DTO;

import com.example.smarthealthcareappointmentsystem.entites.AppointmentStatus;
import lombok.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {
    private Long id;
    private Long doctorId;
    private String doctorName;
    private Long patientId;
    private String patientName;
    private LocalDateTime appointmentTime;
    private AppointmentStatus status;
}