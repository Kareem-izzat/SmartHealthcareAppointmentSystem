package com.example.smarthealthcareappointmentsystem.controller;

import com.example.smarthealthcareappointmentsystem.DTO.AppointmentDto;
import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.SlotDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestSlotDto;
import com.example.smarthealthcareappointmentsystem.service.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor

// dr can update appointments slots for himself and view his patients and appointments
public class DoctorController {


    private final AppointmentService appointmentService;
    private final PrescriptionService prescriptionService;
    private final SlotService slotService;



    @GetMapping("/{doctorId}/appointments")
    public ResponseEntity<List<AppointmentDto>> getAppointments(@PathVariable Long doctorId) {
        return ResponseEntity.ok(appointmentService.getAppointments(doctorId));
    }

    @GetMapping("/{doctorId}/appointments/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointmentById(
            @PathVariable Long doctorId,
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentService.getAppointmentById(doctorId, appointmentId));
    }

    @PutMapping("/{doctorId}/appointments/{appointmentId}/complete")
    public ResponseEntity<AppointmentDto> markAppointmentAsCompleted(
            @PathVariable Long doctorId,
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(appointmentService.markAppointmentAsCompleted(doctorId, appointmentId));
    }



    @PostMapping("/{doctorId}/prescriptions")
    public ResponseEntity<PrescriptionDto> addPrescription(
            @PathVariable Long doctorId,
            @Valid @RequestBody RequestPrescriptionDto requestPrescriptionDto) {
        requestPrescriptionDto.setDoctorId(doctorId); // ensure doctorId is set
        return ResponseEntity.ok(prescriptionService.addPrescription(requestPrescriptionDto));
    }

    @GetMapping("/{doctorId}/prescriptions")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByDoctor(doctorId));
    }

    @GetMapping("/{doctorId}/prescriptions/patient/{patientId}")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByPatient(
            @PathVariable Long doctorId,
            @PathVariable Long patientId) {
        return ResponseEntity.ok(prescriptionService.getPrescriptionsByPatient(patientId));
    }



    @PostMapping("/{doctorId}/slots")
    public ResponseEntity<SlotDto> createSlot(
            @PathVariable Long doctorId,
            @Valid @RequestBody RequestSlotDto requestSlotDto) {
        return ResponseEntity.ok(slotService.createSlot(doctorId, requestSlotDto));
    }

    @GetMapping("/{doctorId}/slots")
    public ResponseEntity<List<SlotDto>> getAllSlots(@PathVariable Long doctorId) {
        return ResponseEntity.ok(slotService.getAllSlots(doctorId));
    }

    @PutMapping("/{doctorId}/slots/{slotId}")
    public ResponseEntity<SlotDto> updateSlot(
            @PathVariable Long doctorId,
            @PathVariable Long slotId,
            @Valid @RequestBody RequestSlotDto requestSlotDto) {
        return ResponseEntity.ok(slotService.updateSlot(slotId, requestSlotDto));
    }

    @DeleteMapping("/{doctorId}/slots/{slotId}")
    public ResponseEntity<Void> deleteSlot(
            @PathVariable Long doctorId,
            @PathVariable Long slotId) {
        slotService.deleteSlot(slotId);
        return ResponseEntity.noContent().build();
    }
}
