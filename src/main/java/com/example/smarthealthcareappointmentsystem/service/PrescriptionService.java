package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



public interface PrescriptionService {
    PrescriptionDto addPrescription(RequestPrescriptionDto prescriptionRequestDto);

    Page<PrescriptionDto> getPrescriptionsByDoctor(Long doctorId, Pageable pageable);

    Page<PrescriptionDto> getPrescriptionsByPatient(Long patientId, Pageable pageable);
}
