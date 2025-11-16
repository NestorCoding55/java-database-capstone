package com.project.back_end.services;

import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class TokenService {

    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Autowired
    public TokenService(AdminRepository adminRepository,
                       DoctorRepository doctorRepository,
                       PatientRepository patientRepository) {
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
    }

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }

    public String generateToken(String identifier, String role) {
        return Jwts.builder()
                .subject(identifier)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 7 days
                .signWith(getSigningKey())
                .compact();
    }

    public String extractIdentifier(String token) {
        try {
            return Jwts.parser()
                    .verifyWith(getSigningKey())
                    .build()
                    .parseSignedClaims(token)
                    .getPayload()
                    .getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    public boolean validateToken(String token, String user) {
        try {
            String identifier = extractIdentifier(token);
            if (identifier == null) {
                return false;
            }

            switch (user.toLowerCase()) {
                case "admin":
                    return adminRepository.findByUsername(identifier) != null;
                case "doctor":
                    return doctorRepository.findByEmail(identifier) != null;
                case "patient":
                    return patientRepository.findByEmail(identifier) != null;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    // Additional helper methods for extracting specific user IDs
    public Long extractAdminId(String token) {
        String username = extractIdentifier(token);
        if (username != null) {
            var admin = adminRepository.findByUsername(username);
            return admin != null ? admin.getId() : null;
        }
        return null;
    }

    public Long extractDoctorId(String token) {
        String email = extractIdentifier(token);
        if (email != null) {
            var doctor = doctorRepository.findByEmail(email);
            return doctor != null ? doctor.getId() : null;
        }
        return null;
    }

    public Long extractPatientId(String token) {
        String email = extractIdentifier(token);
        if (email != null) {
            var patient = patientRepository.findByEmail(email);
            return patient != null ? patient.getId() : null;
        }
        return null;
    }

    public String extractEmail(String token) {
        return extractIdentifier(token);
    }
}