package com.example.smarthealthcareappointmentsystem;

import com.example.smarthealthcareappointmentsystem.DTO.request.RequestPatientDto;
import com.example.smarthealthcareappointmentsystem.entites.Patient;
import com.example.smarthealthcareappointmentsystem.entites.Doctor;
import com.example.smarthealthcareappointmentsystem.entites.Slot;
import com.example.smarthealthcareappointmentsystem.entites.Role;
import com.example.smarthealthcareappointmentsystem.repository.PatientRepository;
import com.example.smarthealthcareappointmentsystem.repository.DoctorRepository;
import com.example.smarthealthcareappointmentsystem.repository.SlotRepository;
import com.example.smarthealthcareappointmentsystem.repository.AppointmentRepository;
import com.example.smarthealthcareappointmentsystem.repository.PrescriptionRepository;
import com.example.smarthealthcareappointmentsystem.security.JwtUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase//(replace = AutoConfigureTestDatabase.Replace.ANY) // Use H2
class PatientControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PrescriptionRepository prescriptionRepository;

    @Autowired
    private JwtUtils jwtUtils;

    private Patient patient;
    private Doctor doctor;
    private Slot slot;
    private String patientToken;

    @BeforeEach
    void setUp() {


        appointmentRepository.deleteAll();
        slotRepository.deleteAll();
        doctorRepository.deleteAll();
        patientRepository.deleteAll();
        prescriptionRepository.deleteAll();


        patient = Patient.builder()
                .firstName("Kareem")
                .lastName("Qutob")
                .email("kareem@example.com")
                .password("password123")
                .phone("123456789")
                .role(Role.PATIENT)
                .dateOfBirth(LocalDate.of(1995, 5, 20))
                .medicalHistory("No known conditions")
                .build();
        patientRepository.save(patient);

        // Generate JWT token for patient
        patientToken = jwtUtils.generateJwtToken(patient.getEmail(), patient.getRole());

        doctor = Doctor.builder()
                .firstName("Alice")
                .lastName("Smith")
                .email("drsmith@example.com")
                .password("password123")
                .phone("987654321")
                .role(Role.DOCTOR)
                .specialty("Cardiology")
                .yearsOfExperience(10)
                .build();
        doctorRepository.save(doctor);

        slot = Slot.builder()
                .doctor(doctor)
                .startTime(LocalDateTime.now().plusHours(1))
                .endTime(LocalDateTime.now().plusHours(2))
                .available(true)
                .build();
        slotRepository.save(slot);
    }

    @Test
    void testViewProfile() throws Exception {
        mockMvc.perform(get("/api/patient/{id}", patient.getId())
                        .header("Authorization", "Bearer " + patientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Kareem"))
                .andExpect(jsonPath("$.email").value("kareem@example.com"));
    }

    @Test
    void testUpdateProfile() throws Exception {
        RequestPatientDto updateDto = RequestPatientDto.builder()
                .firstName("KareemUpdated")
                .lastName("QutobUpdated")
                .email("kareem@example.com")
                .password("password123") // Required
                .phone("987654321")
                .dateOfBirth(LocalDate.of(1995, 5, 20)) // Required
                .medicalHistory("Updated history")
                .build();

        mockMvc.perform(put("/api/patient/{id}", patient.getId())
                        .header("Authorization", "Bearer " + patientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isNoContent());

        // Verify in DB
        Patient updated = patientRepository.findById(patient.getId()).orElseThrow();
        assert updated.getFirstName().equals("KareemUpdated");
        assert updated.getLastName().equals("QutobUpdated");
        assert updated.getPhone().equals("987654321");
        assert updated.getMedicalHistory().equals("Updated history");
    }

    @Test
    void testGetDoctorsBySpecialty() throws Exception {
        mockMvc.perform(get("/api/patient/doctors")
                        .param("specialty", "Cardiology")
                        .header("Authorization", "Bearer " + patientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].firstName").value("Alice"))
                .andExpect(jsonPath("$.content[0].lastName").value("Smith"));
    }

    @Test
    void testGetAvailableSlots() throws Exception {
        mockMvc.perform(get("/api/patient/doctors/{doctorId}/slots", doctor.getId())
                        .header("Authorization", "Bearer " + patientToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].available").value(true));
    }
}
