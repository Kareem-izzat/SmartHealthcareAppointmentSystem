package com.example.smarthealthcareappointmentsystem.mapper.mongo;
import com.example.smarthealthcareappointmentsystem.DTO.mongo.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.entites.mongo.Prescription;
import org.springframework.stereotype.Component;

@Component
public class PrescriptionMapper {

    public PrescriptionDto toDto(Prescription prescription) {
        if (prescription == null) return null;

        return PrescriptionDto.builder()
                .patientId(prescription.getPatientId())
                .patientName(prescription.getPatientName())
                .doctorId(prescription.getDoctorId())
                .doctorName(prescription.getDoctorName())
                .appointmentId(prescription.getAppointmentId())
                .date(prescription.getDate())
                .medicines(prescription.getMedicines())
                .notes(prescription.getNotes())
                .build();
    }

    public Prescription toEntity(PrescriptionDto dto) {
        if (dto == null) return null;

        return Prescription.builder()
                .patientId(dto.getPatientId())
                .patientName(dto.getPatientName())
                .doctorId(dto.getDoctorId())
                .doctorName(dto.getDoctorName())
                .appointmentId(dto.getAppointmentId())
                .date(dto.getDate())
                .medicines(dto.getMedicines())
                .notes(dto.getNotes())
                .build();
    }
}