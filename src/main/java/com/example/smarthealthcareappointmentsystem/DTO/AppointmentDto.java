package com.example.smarthealthcareappointmentsystem.DTO;

import com.example.smarthealthcareappointmentsystem.entites.AppointmentStatus;
import lombok.*;
import java.time.LocalDateTime;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDto {
    private Long id;

    private Long slotId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private Long doctorId;
    private String doctorName;

    private Long patientId;
    private String patientName;

    private AppointmentStatus status;
}
