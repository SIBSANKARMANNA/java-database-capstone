package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.models.Login;

import com.project.back_end.repo.jpa.AdminRepository;
import com.project.back_end.repo.jpa.DoctorRepository;
import com.project.back_end.repo.jpa.PatientRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AuthService {

    private final TokenService tokenService;
    private final AdminRepository adminRepo;
    private final DoctorRepository doctorRepo;
    private final PatientRepository patientRepo;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public AuthService(
            TokenService tokenService,
            AdminRepository adminRepo,
            DoctorRepository doctorRepo,
            PatientRepository patientRepo,
            DoctorService doctorService,
            PatientService patientService
    ) {
        this.tokenService = tokenService;
        this.adminRepo = adminRepo;
        this.doctorRepo = doctorRepo;
        this.patientRepo = patientRepo;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    /* =========================
       TOKEN VALIDATION
    ========================= */
    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        if (!tokenService.validateToken(token, user)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid or expired token"));
        }
        return ResponseEntity.ok(Map.of());
    }

    /* =========================
       ADMIN LOGIN
    ========================= */
    public ResponseEntity<Map<String, String>> validateAdmin(Admin admin) {
        Admin dbAdmin = adminRepo.findByUsername(admin.getUsername());

        if (dbAdmin != null && dbAdmin.getPassword().equals(admin.getPassword())) {
            String token = tokenService.generateToken(admin.getUsername());
            return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid credentials"));
    }

    /* =========================
       PATIENT SIGNUP CHECK
    ========================= */
    public boolean validatePatient(Patient patient) {
        return patientRepo.findByEmailOrPhone(
                patient.getEmail(), patient.getPhone()) == null;
    }

    /* =========================
       PATIENT LOGIN
    ========================= */
    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Patient patient = patientRepo.findByEmail(login.getIdentifier());

        if (patient != null && patient.getPassword().equals(login.getPassword())) {
            String token = tokenService.generateToken(patient.getEmail());
            return ResponseEntity.ok(Map.of("token", token));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", "Invalid credentials"));
    }

    /* =========================
       APPOINTMENT VALIDATION
    ========================= */
    public int validateAppointment(Appointment appointment) {
        Optional<Doctor> doctorOpt =
                doctorRepo.findById(appointment.getDoctor().getId());

        if (doctorOpt.isEmpty()) return -1;

        List<String> slots =
                doctorService.getDoctorAvailability(
                        appointment.getDoctor().getId(),
                        appointment.getAppointmentTime().toLocalDate());

        return slots.contains(
                appointment.getAppointmentTime().toLocalTime().toString()
        ) ? 1 : 0;
    }

    /* =========================
       DOCTOR FILTER (FIXED)
    ========================= */
    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        return doctorService.filterDoctors(name, specialty, time);
    }

    /* =========================
       PATIENT FILTER
    ========================= */
    public ResponseEntity<Map<String, Object>> filterPatient(
            String condition, String name, String token) {

        String email = tokenService.extractIdentifier(token);
        Patient patient = patientRepo.findByEmail(email);

        if (patient == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!"null".equals(condition) && !"null".equals(name)) {
            return patientService.filterByDoctorAndCondition(
                    condition, name, patient.getId());
        } else if (!"null".equals(condition)) {
            return patientService.filterByCondition(condition, patient.getId());
        } else {
            return patientService.filterByDoctor(name, patient.getId());
        }
    }
}
