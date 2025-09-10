package com.example.smarthealthcareappointmentsystem.controller;

import com.example.smarthealthcareappointmentsystem.DTO.DoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.PatientDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestDoctorDto;
import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPatientDto;
import com.example.smarthealthcareappointmentsystem.service.DoctorService;
import com.example.smarthealthcareappointmentsystem.service.PatientService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
// admin will create and edit patients and drs
public class AdminController {
    private final DoctorService doctorService;
    private final PatientService patientService;

    @GetMapping("/doctors")
    public ResponseEntity<Page<DoctorDto>> getDoctors(
            @PageableDefault(sort = "lastName") Pageable pageable) {
        return ResponseEntity.ok(doctorService.getAllDoctors(pageable));
    }
    @GetMapping("/doctors/{id}")
    public ResponseEntity<DoctorDto> getDoctorById(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }
    @PostMapping("/doctors")
    public ResponseEntity<DoctorDto> addDoctor(@Valid @RequestBody RequestDoctorDto doctorDto) {
        return ResponseEntity.ok(doctorService.addDoctor(doctorDto));
    }
    @PutMapping("/doctors/{id}")
    public ResponseEntity<DoctorDto> updateDoctor(@PathVariable Long id, @Valid @RequestBody RequestDoctorDto doctorDto) {
        return ResponseEntity.ok(doctorService.updateDoctorById(id, doctorDto));
    }
    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> removeDoctor(@PathVariable Long id) {
        doctorService.RemoveDoctorById(id);
        return ResponseEntity.noContent().build();
    }

    //---------------------------------------------------- patient
    @GetMapping("/patients")
    public ResponseEntity<Page<PatientDto>> getPatients(
            @PageableDefault( sort = "lastName") Pageable pageable) {
        return ResponseEntity.ok(patientService.getAllPatients(pageable));
    }
    @GetMapping("/patients/{id}")
    public ResponseEntity<PatientDto> getPatientById(@PathVariable Long id) {
        return ResponseEntity.ok(patientService.getPatientById(id));
    }
    @PostMapping("/patients")
    public ResponseEntity<PatientDto> addPatient(@Valid @RequestBody RequestPatientDto patientDto) {
        return ResponseEntity.ok(patientService.addPatient(patientDto));
    }
    @PutMapping("/patients/{id}")
    public ResponseEntity<PatientDto> updatePatient(@PathVariable Long id, @Valid @RequestBody RequestPatientDto patientDto) {
        return ResponseEntity.ok(patientService.updatePatient(id, patientDto));
    }
    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> removePatient(@PathVariable Long id) {
        patientService.removePatient(id);
        return ResponseEntity.noContent().build();
    }
}
