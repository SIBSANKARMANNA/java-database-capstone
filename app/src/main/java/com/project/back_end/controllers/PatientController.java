package com.project.back_end.controllers;

import com.project.back_end.models.Patient;
import com.project.back_end.models.Login;
import com.project.back_end.services.PatientService;
import com.project.back_end.services.AuthService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@RestController
@RequestMapping("${api.path}patient")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private AuthService authService;

    /* =========================
       PATIENT SIGNUP
    ========================= */
    @PostMapping
    public ResponseEntity<Map<String, String>> signup(
            @RequestBody Patient patient) {

        if (!authService.validatePatient(patient)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "message",
                            "Patient with email or phone already exists"
                    ));
        }

        int result = patientService.createPatient(patient);

        return result == 1
                ? ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Signup successful"))
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Internal server error"));
    }

    /* =========================
       PATIENT LOGIN
    ========================= */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(
            @RequestBody Login login) {

        return authService.validatePatientLogin(login);
    }

    /* =========================
       PATIENT PROFILE
    ========================= */
    @GetMapping("/{token}")
    public ResponseEntity<Map<String, Object>> getProfile(
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> auth =
                authService.validateToken(token, "patient");

        if (!auth.getBody().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return patientService.getPatientDetails(token);
    }

    /* =========================
       PATIENT APPOINTMENTS
    ========================= */
    @GetMapping("/{id}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(
            @PathVariable Long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> auth =
                authService.validateToken(token, "patient");

        if (!auth.getBody().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return patientService.getPatientAppointment(id, token);
    }

    /* =========================
       FILTER APPOINTMENTS
    ========================= */
    @GetMapping("/filter/{condition}/{name}/{token}")
    public ResponseEntity<Map<String, Object>> filterAppointments(
            @PathVariable String condition,
            @PathVariable String name,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> auth =
                authService.validateToken(token, "patient");

        if (!auth.getBody().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return authService.filterPatient(condition, name, token);
    }
}
