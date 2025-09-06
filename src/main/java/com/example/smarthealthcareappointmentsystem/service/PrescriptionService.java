package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;

import java.util.List;

public interface PrescriptionService {
    PrescriptionDto addPrescription(RequestPrescriptionDto prescriptionRequestDto);

    List<PrescriptionDto> getPrescriptionsByDoctor(Long doctorId);

    List<PrescriptionDto> getPrescriptionsByPatient(Long patientId);
}
