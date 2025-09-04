package com.example.smarthealthcareappointmentsystem.service;

import com.example.smarthealthcareappointmentsystem.DTO.AppointmentDto;
import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;

import java.util.List;

public interface DoctorService {


    List<AppointmentDto> getAppointments(Long doctorId);

    AppointmentDto markAppointmentAsCompleted(Long doctorId, Long appointmentId);

    PrescriptionDto addPrescription(RequestPrescriptionDto prescriptionRequestDto);

    List<PrescriptionDto> getPrescriptionsByDoctor(Long doctorId);

    List<PrescriptionDto> getPrescriptionsByPatient(Long patientId);

    AppointmentDto getAppointmentById(Long doctorId, Long appointmentId);

}
