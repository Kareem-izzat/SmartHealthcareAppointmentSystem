package com.example.smarthealthcareappointmentsystem.controller;

import com.example.smarthealthcareappointmentsystem.DTO.*;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestAppointmentDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPatientDto;
import com.example.smarthealthcareappointmentsystem.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/patient")
@RequiredArgsConstructor
// patient can update himself and book a slot for an appointment or cancel it and view his records
public class patientController {
    final private PatientService patientService;
    final private DoctorService doctorService;
    final private SlotService slotService;
    final private AppointmentService appointmentService;
    final private PrescriptionService prescriptionService;
    final private MedicalRecordService medicalRecordService;


    @GetMapping("/{id}")
    public ResponseEntity<PatientDto> viewProfile(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }
    @PutMapping("/{patientId}")
    public ResponseEntity<Void> updateProfile(
            @PathVariable Long patientId,
            @Valid @RequestBody RequestPatientDto updateDto) {
        patientService.updatePatient(patientId, updateDto);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/doctors")
    public ResponseEntity<Page<DoctorDto>> getDoctorsBySpecialty(
            @Valid @RequestParam String specialty,
            @PageableDefault(sort = "lastName") Pageable pageable) {

        return ResponseEntity.ok(doctorService.searchDoctorsBySpecialty(specialty, pageable));
    }
    @GetMapping("/doctors/{doctorId}/slots")
    public ResponseEntity<List<SlotDto>> getAvailableSlots(@PathVariable Long doctorId) {

        return ResponseEntity.ok(slotService.getAvailableSlots(doctorId));
    }
    @PostMapping("/{patientId}/appointments")
    public ResponseEntity<AppointmentDto> bookAppointment(
            @PathVariable Long patientId,
            @Valid @RequestBody RequestAppointmentDto requestAppointmentDto) {

        AppointmentDto appointment = appointmentService.bookAppointment(
                patientId,
                requestAppointmentDto.getSlotId()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);

    }

    @GetMapping("/{patientId}/appointments")
    public ResponseEntity<Page<AppointmentDto>> getAppointmentsByPatient(
            @PathVariable Long patientId,
            Pageable pageable
    ) {
        Page<AppointmentDto> appointments = appointmentService.getPatientAppointments(patientId, pageable);
        return ResponseEntity.ok(appointments);
    }

    @DeleteMapping("/{patientId}/appointments/{appointmentId}")
    public ResponseEntity<AppointmentDto> cancelAppointment(
            @PathVariable Long patientId,
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentService.cancelAppointment(patientId, appointmentId));
    }
    @GetMapping("/{patientId}/prescriptions")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptions(
            @PathVariable Long patientId) {
        List<PrescriptionDto> prescriptions = prescriptionService.getPrescriptionsByPatient(patientId);
        return ResponseEntity.ok(prescriptions);
    }
    @GetMapping("/{patientId}/medical-record")
    public ResponseEntity<MedicalRecordDto> getMedicalRecord(@PathVariable Long patientId) {
        MedicalRecordDto record = medicalRecordService.getOrCreateRecord(patientId);
        return ResponseEntity.ok(record);
    }




}
