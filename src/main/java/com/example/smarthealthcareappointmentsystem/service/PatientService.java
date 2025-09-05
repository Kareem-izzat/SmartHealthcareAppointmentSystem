package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.MedicalRecordDto;
import com.example.smarthealthcareappointmentsystem.DTO.PatientDto;
import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPatientDto;

import java.util.List;

public interface PatientService {

    PatientDto getProfile(Long patientId);

    PatientDto updateProfile(Long patientId, RequestPatientDto requestPatientDto);


    MedicalRecordDto viewMedicalRecord(Long patientId);

    List<PrescriptionDto> viewPrescriptions(Long patientId);

    void bookAppointment(Long patientId, Long doctorId, Long appointmentId);

    void cancelAppointment(Long patientId, Long appointmentId);
}
