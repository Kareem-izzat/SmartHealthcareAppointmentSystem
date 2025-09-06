package com.example.smarthealthcareappointmentsystem.controller;

import com.example.smarthealthcareappointmentsystem.DTO.AppointmentDto;
import com.example.smarthealthcareappointmentsystem.DTO.PrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.SlotDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPrescriptionDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestSlotDto;
import com.example.smarthealthcareappointmentsystem.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/doctor")
@RequiredArgsConstructor
public class DoctorController {
    private final DoctorService doctorService;

    @GetMapping("/{doctorId}/appointments")
    public ResponseEntity<List<AppointmentDto>>
    getAppointments(@PathVariable("doctorId") Long doctorId){
        return ResponseEntity.ok(doctorService.getAppointments(doctorId));
    }
    @GetMapping("/{doctorId}/appointments/{appointmentId}")
    public ResponseEntity<AppointmentDto> getAppointmentById(
            @PathVariable Long doctorId,
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(doctorService.getAppointmentById(doctorId, appointmentId));
    }
    @PutMapping("/{doctorId}/appointments/{appointmentId}/complete")
    public ResponseEntity<AppointmentDto> markAppointmentAsCompleted(
            @PathVariable Long doctorId,
            @PathVariable Long appointmentId) {
        return ResponseEntity.ok(doctorService.markAppointmentAsCompleted(doctorId, appointmentId));
    }
    @PostMapping("/prescriptions")
    public ResponseEntity<PrescriptionDto> addPrescription(@RequestBody RequestPrescriptionDto requestPrescriptionDto) {
        return ResponseEntity.ok(doctorService.addPrescription(requestPrescriptionDto));
    }
    @GetMapping("/{doctorId}/prescriptions")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByDoctor(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorService.getPrescriptionsByDoctor(doctorId));
    }

    @GetMapping("/prescriptions/patient/{patientId}")
    public ResponseEntity<List<PrescriptionDto>> getPrescriptionsByPatient(@PathVariable Long patientId) {
        return ResponseEntity.ok(doctorService.getPrescriptionsByPatient(patientId));
    }
    @PostMapping("/{doctorId}/slots")
    public ResponseEntity<SlotDto> createSlot(
            @PathVariable Long doctorId,
            @RequestBody RequestSlotDto requestSlotDto) {
        return ResponseEntity.ok(doctorService.createSlot(doctorId, requestSlotDto));
    }

    @GetMapping("/{doctorId}/slots")
    public ResponseEntity<List<SlotDto>> getAllSlots(@PathVariable Long doctorId) {
        return ResponseEntity.ok(doctorService.getAllSlots(doctorId));
    }

    @PutMapping("/slots/{slotId}")
    public ResponseEntity<SlotDto> updateSlot(
            @PathVariable Long slotId,
            @RequestBody RequestSlotDto requestSlotDto) {
        return ResponseEntity.ok(doctorService.updateSlot(slotId, requestSlotDto));
    }

    @DeleteMapping("/slots/{slotId}")
    public ResponseEntity<Void> deleteSlot(@PathVariable Long slotId) {
        doctorService.deleteSlot(slotId);
        return ResponseEntity.noContent().build();
    }
}
