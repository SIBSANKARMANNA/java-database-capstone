package com.project.back_end.controllers;

import com.project.back_end.models.Appointment;
import com.project.back_end.services.AppointmentService;
import com.project.back_end.services.AuthService;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("${api.path}appointments")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private AuthService authService;

    /* =========================
       GET APPOINTMENTS (DOCTOR)
    ========================= */
    @GetMapping("/{date}/{patientName}/{token}")
    public ResponseEntity<Map<String, Object>> getAppointments(
            @PathVariable LocalDate date,
            @PathVariable String patientName,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> auth =
                authService.validateToken(token, "doctor");

        if (!auth.getBody().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(
                appointmentService.getAppointment(patientName, date, token)
        );
    }

    /* =========================
       BOOK APPOINTMENT
    ========================= */
    @PostMapping("/{token}")
    public ResponseEntity<Map<String, String>> book(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> auth =
                authService.validateToken(token, "patient");

        if (!auth.getBody().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        int valid = authService.validateAppointment(appointment);
        if (valid != 1) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", "Invalid appointment"));
        }

        appointmentService.bookAppointment(appointment);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Appointment booked"));
    }

    /* =========================
       UPDATE APPOINTMENT
    ========================= */
    @PutMapping("/{token}")
    public ResponseEntity<Map<String, String>> update(
            @RequestBody Appointment appointment,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> auth =
                authService.validateToken(token, "patient");

        if (!auth.getBody().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return appointmentService.updateAppointment(appointment);
    }

    /* =========================
       CANCEL APPOINTMENT
    ========================= */
    @DeleteMapping("/{id}/{token}")
    public ResponseEntity<Map<String, String>> cancel(
            @PathVariable long id,
            @PathVariable String token) {

        ResponseEntity<Map<String, String>> auth =
                authService.validateToken(token, "patient");

        if (!auth.getBody().isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return appointmentService.cancelAppointment(id, token);
    }
}
