package com.example.smarthealthcareappointmentsystem.mapper.mongo;
import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;
import com.example.smarthealthcareappointmentsystem.entites.mongo.Prescription;
import org.springframework.stereotype.Component;
// mapper between dto and entity
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


    public Prescription toEntity(RequestPrescriptionDto dto) {
        if (dto == null) return null;

        return Prescription.builder()
                .patientId(dto.getPatientId())
                .doctorId(dto.getDoctorId())
                .appointmentId(dto.getAppointmentId())
                .medicines(dto.getMedicines())
                .notes(dto.getNotes())
                .build();
    }

}