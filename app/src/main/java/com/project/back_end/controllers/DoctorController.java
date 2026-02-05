package com.project.back_end.controllers;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Login;
import com.project.back_end.services.DoctorService;
import com.project.back_end.services.AuthService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

@RestController
@RequestMapping("${api.path}doctor")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private AuthService authService;

    /* =========================
       GET ALL DOCTORS
    ========================= */
    @GetMapping
    public Map<String, Object> getDoctors() {
        return Map.of("doctors", doctorService.getDoctors());
    }

    /* =========================
       ADD DOCTOR (ADMIN ONLY)
    ========================= */
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> addDoctor(
            @RequestBody Doctor doctor,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> auth =
                authService.validateToken(token, "admin");

        if (!auth.getBody().isEmpty()) return auth;

        int result = doctorService.saveDoctor(doctor);

        return switch (result) {
            case 1 -> ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of("message", "Doctor added to db"));
            case -1 -> ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "Doctor already exists"));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Some internal error occurred"));
        };
    }

    /* =========================
       DOCTOR LOGIN
    ========================= */
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginDoctor(
            @RequestBody Login login) {

        return doctorService.validateDoctor(login);
    }

    /* =========================
       DELETE DOCTOR (ADMIN ONLY)
    ========================= */
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> deleteDoctor(
            @PathVariable long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> auth =
                authService.validateToken(token, "admin");

        if (!auth.getBody().isEmpty()) return auth;

        int result = doctorService.deleteDoctor(id);

        return switch (result) {
            case 1 -> ResponseEntity.ok(
                    Map.of("message", "Doctor deleted successfully"));
            case -1 -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "Doctor not found with id"));
            default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Some internal error occurred"));
        };
    }

    /* =========================
       FILTER DOCTORS
    ========================= */
    @GetMapping("/filter/{name}/{time}/{speciality}")
    public Map<String, Object> filterDoctors(
            @PathVariable String name,
            @PathVariable String time,
            @PathVariable String speciality) {

        return authService.filterDoctor(name, speciality, time);
    }
}
