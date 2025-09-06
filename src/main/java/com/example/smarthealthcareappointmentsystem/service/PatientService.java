package com.example.smarthealthcareappointmentsystem.service;


import com.example.smarthealthcareappointmentsystem.DTO.*;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPatientDto;

import java.util.List;

public interface PatientService {

    List<PatientDto> getAllPatients();
    PatientDto getPatientById(Long patientId);
    PatientDto addPatient(RequestPatientDto patientDto);
    PatientDto updatePatient(Long patientId, RequestPatientDto patientDto);
    void removePatient(Long patientId);

}
