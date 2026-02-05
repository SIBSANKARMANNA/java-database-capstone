package com.project.back_end.services;

import com.project.back_end.repo.jpa.AdminRepository;
import com.project.back_end.repo.jpa.DoctorRepository;
import com.project.back_end.repo.jpa.PatientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class TokenService {

    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private AdminRepository adminRepo;

    @Autowired
    private DoctorRepository doctorRepo;

    @Autowired
    private PatientRepository patientRepo;

    /* =========================
       GENERATE JWT TOKEN
    ========================= */
    public String generateToken(String identifier) {
        return Jwts.builder()
                .setSubject(identifier)
                .setIssuedAt(new Date())
                .setExpiration(
                        Date.from(Instant.now().plus(7, ChronoUnit.DAYS))
                )
                .signWith(getSigningKey())
                .compact();
    }

    /* =========================
       EXTRACT USER IDENTIFIER
    ========================= */
    public String extractIdentifier(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    /* =========================
       VALIDATE TOKEN BY ROLE
    ========================= */
    public boolean validateToken(String token, String user) {
        try {
            String identifier = extractIdentifier(token);

            return switch (user) {
                case "admin" -> adminRepo.findByUsername(identifier) != null;
                case "doctor" -> doctorRepo.findByEmail(identifier) != null;
                case "patient" -> patientRepo.findByEmail(identifier) != null;
                default -> false;
            };
        } catch (Exception e) {
            return false;
        }
    }

    /* =========================
       SIGNING KEY
    ========================= */
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(
                secret.getBytes(StandardCharsets.UTF_8)
        );
    }
}
