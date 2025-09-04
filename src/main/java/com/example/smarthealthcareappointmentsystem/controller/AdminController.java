package com.example.smarthealthcareappointmentsystem.controller;

import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.PatientDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPatientDto;
import com.example.smarthealthcareappointmentsystem.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/doctors")
    public ResponseEntity<List<DoctorDto>> getDoctors() {

        return ResponseEntity.ok(adminService.getAllDoctors());
    }
    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getDoctorById(id));
    }
    @PostMapping("/doctors")
    public ResponseEntity<DoctorDto> addDoctor(@Valid @RequestBody RequestDoctorDto doctorDto) {
        return ResponseEntity.ok(adminService.addDoctor(doctorDto));
    }
    @PutMapping("/doctors/{id}")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable Long id, @Valid @RequestBody RequestDoctorDto doctorDto) {
        return ResponseEntity.ok(adminService.updateDoctorById(id, doctorDto));
    }
    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> removeDoctor(@PathVariable Long id) {
        adminService.RemoveDoctorById(id);
        return ResponseEntity.noContent().build();
    }

    //---------------------------------------------------- patient
    @GetMapping("/patients")
    public ResponseEntity<List<PatientDto>> getPatients() {
        return ResponseEntity.ok(adminService.getAllPatients());
    }
    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getPatientById(id));
    }
    @PostMapping("/patients")
    public ResponseEntity<PatientDto> addPatient(@Valid @RequestBody RequestPatientDto patientDto) {
        return ResponseEntity.ok(adminService.addPatient(patientDto));
    }
    @PutMapping("/patients/{id}")
    public ResponseEntity<PatientDto> updateDoctor(@PathVariable Long id, @Valid @RequestBody RequestPatientDto patientDto) {
        return ResponseEntity.ok(adminService.updatePatient(id, patientDto));
    }
    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> removePatient(@PathVariable Long id) {
        adminService.removePatient(id);
        return ResponseEntity.noContent().build();
    }
}
