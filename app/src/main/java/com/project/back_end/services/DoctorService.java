package com.project.back_end.services;

import com.project.back_end.models.Doctor;
import com.project.back_end.models.Appointment;
import com.project.back_end.models.Login;

import com.project.back_end.repo.jpa.DoctorRepository;
import com.project.back_end.repo.jpa.AppointmentRepository;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import java.util.List;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private TokenService tokenService;

    /* =========================
       DOCTOR AVAILABILITY
    ========================= */
    public List<String> getDoctorAvailability(Long doctorId, LocalDate date) {

        Doctor doctor = doctorRepository.findById(doctorId).orElse(null);
        if (doctor == null) return List.of();

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.atTime(LocalTime.MAX);

        List<Appointment> appointments =
                appointmentRepository.findByDoctorIdAndAppointmentTimeBetween(
                        doctorId, start, end);

        Set<String> booked =
                appointments.stream()
                        .map(a -> a.getAppointmentTime()
                                .toLocalTime()
                                .toString()
                                .substring(0, 5))
                        .collect(Collectors.toSet());

        return doctor.getAvailableTimes().stream()
                .filter(t -> !booked.contains(t))
                .toList();
    }

    /* =========================
       SAVE DOCTOR
    ========================= */
    public int saveDoctor(Doctor doctor) {
        if (doctorRepository.findByEmail(doctor.getEmail()) != null) return -1;

        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /* =========================
       UPDATE DOCTOR
    ========================= */
    public int updateDoctor(Doctor doctor) {
        if (!doctorRepository.existsById(doctor.getId())) return -1;

        try {
            doctorRepository.save(doctor);
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    /* =========================
       GET ALL DOCTORS
    ========================= */
    public List<Doctor> getDoctors() {
        return doctorRepository.findAll();
    }

    /* =========================
       DELETE DOCTOR
    ========================= */
    public int deleteDoctor(long id) {
        if (!doctorRepository.existsById(id)) return -1;

        appointmentRepository.deleteAllByDoctorId(id);
        doctorRepository.deleteById(id);
        return 1;
    }

    /* =========================
       DOCTOR LOGIN
    ========================= */
    public ResponseEntity<Map<String, String>> validateDoctor(Login login) {

        Doctor doctor = doctorRepository.findByEmail(login.getIdentifier());

        if (doctor == null ||
                !doctor.getPassword().equals(login.getPassword())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Invalid credentials"));
        }

        String token = tokenService.generateToken(doctor.getEmail());
        return ResponseEntity.ok(Map.of("token", token));
    }

    /* =========================
       FILTER DOCTORS (REQUIRED)
    ========================= */
    public Map<String, Object> filterDoctors(
            String name, String specialty, String time) {

        List<Doctor> doctors = doctorRepository.findAll();

        if (!"null".equals(name)) {
            doctors = doctors.stream()
                    .filter(d -> d.getName()
                            .toLowerCase()
                            .contains(name.toLowerCase()))
                    .toList();
        }

        if (!"null".equals(specialty)) {
            doctors = doctors.stream()
                    .filter(d -> d.getSpecialty()
                            .equalsIgnoreCase(specialty))
                    .toList();
        }

        if (!"null".equals(time)) {
            doctors = filterDoctorByTime(doctors, time);
        }

        return Map.of("doctors", doctors);
    }

    /* =========================
       FILTER BY TIME
    ========================= */
    public List<Doctor> filterDoctorByTime(List<Doctor> doctors, String amOrPm) {
        return doctors.stream()
                .filter(d ->
                        d.getAvailableTimes().stream()
                                .anyMatch(t -> t.toLowerCase().contains(amOrPm.toLowerCase())))
                .toList();
    }
}
