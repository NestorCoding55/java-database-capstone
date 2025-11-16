package com.project.back_end.services;

import com.project.back_end.models.Admin;
import com.project.back_end.models.Doctor;
import com.project.back_end.models.Patient;
import com.project.back_end.models.Appointment;
import com.project.back_end.DTO.Login;
import com.project.back_end.repo.AdminRepository;
import com.project.back_end.repo.DoctorRepository;
import com.project.back_end.repo.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class AppService {   // <<< FIXED NAME

    private final TokenService tokenService;
    private final AdminRepository adminRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public AppService(TokenService tokenService,
                      AdminRepository adminRepository,
                      DoctorRepository doctorRepository,
                      PatientRepository patientRepository,
                      DoctorService doctorService,
                      PatientService patientService) {

        this.tokenService = tokenService;
        this.adminRepository = adminRepository;
        this.doctorRepository = doctorRepository;
        this.patientRepository = patientRepository;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    public ResponseEntity<Map<String, String>> validateToken(String token, String user) {
        Map<String, String> response = new HashMap<>();
        boolean isValid = tokenService.validateToken(token, user);

        if (!isValid) {
            response.put("message", "Invalid or expired token");
            return ResponseEntity.status(401).body(response);
        }

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Map<String, String>> validateAdmin(Admin receivedAdmin) {
        Map<String, String> response = new HashMap<>();

        try {
            Admin admin = adminRepository.findByUsername(receivedAdmin.getUsername());

            if (admin == null) {
                response.put("message", "Admin not found");
                return ResponseEntity.status(401).body(response);
            }

            if (!admin.getPassword().equals(receivedAdmin.getPassword())) {
                response.put("message", "Invalid password");
                return ResponseEntity.status(401).body(response);
            }

            String token = tokenService.generateToken(admin.getUsername(), "admin");
            response.put("token", token);
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Internal server error");
            return ResponseEntity.status(500).body(response);
        }
    }

    public Map<String, Object> filterDoctor(String name, String specialty, String time) {
        Map<String, Object> response = new HashMap<>();

        if ((name == null || name.trim().isEmpty()) &&
                (specialty == null || specialty.trim().isEmpty()) &&
                (time == null || time.trim().isEmpty())) {

            List<Doctor> allDoctors = doctorService.getDoctors();
            response.put("doctors", allDoctors);
            return response;
        }

        if (name != null && !name.trim().isEmpty() &&
                specialty != null && !specialty.trim().isEmpty() &&
                time != null && !time.trim().isEmpty()) {
            return doctorService.filterDoctorsByNameSpecilityandTime(name, specialty, time);

        } else if (name != null && !name.trim().isEmpty() &&
                specialty != null && !specialty.trim().isEmpty()) {
            return doctorService.filterDoctorByNameAndSpecility(name, specialty);

        } else if (name != null && !name.trim().isEmpty() &&
                time != null && !time.trim().isEmpty()) {
            return doctorService.filterDoctorByNameAndTime(name, time);

        } else if (specialty != null && !specialty.trim().isEmpty() &&
                time != null && !time.trim().isEmpty()) {
            return doctorService.filterDoctorByTimeAndSpecility(specialty, time);

        } else if (name != null && !name.trim().isEmpty()) {
            return doctorService.findDoctorByName(name);

        } else if (specialty != null && !specialty.trim().isEmpty()) {
            return doctorService.filterDoctorBySpecility(specialty);

        } else if (time != null && !time.trim().isEmpty()) {
            return doctorService.filterDoctorsByTime(time);
        }

        response.put("doctors", List.of());
        return response;
    }

    public int validateAppointment(Appointment appointment) {

        if (appointment.getDoctor() == null || appointment.getDoctor().getId() == null)
            return -1;

        Optional<Doctor> doctorOpt = doctorRepository.findById(appointment.getDoctor().getId());
        if (doctorOpt.isEmpty())
            return -1;

        if (appointment.getPatient() == null || appointment.getPatient().getId() == null)
            return -1;

        if (appointment.getAppointmentTime() == null)
            return 0;

        if (appointment.getAppointmentTime().isBefore(java.time.LocalDateTime.now()))
            return 0;

        return 1;
    }

    public boolean validatePatient(Patient patient) {
        Patient existingPatient = patientRepository.findByEmailOrPhone(patient.getEmail(), patient.getPhone());
        return existingPatient == null;
    }

    public ResponseEntity<Map<String, String>> validatePatientLogin(Login login) {
        Map<String, String> response = new HashMap<>();

        try {
            Patient patient = patientRepository.findByEmail(login.getIdentifier());

            if (patient == null) {
                response.put("message", "Patient not found");
                return ResponseEntity.status(401).body(response);
            }

            if (!patient.getPassword().equals(login.getPassword())) {
                response.put("message", "Invalid password");
                return ResponseEntity.status(401).body(response);
            }

            String token = tokenService.generateToken(patient.getEmail(), "patient");
            response.put("token", token);
            response.put("message", "Login successful");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("message", "Internal server error");
            return ResponseEntity.status(500).body(response);
        }
    }

    public ResponseEntity<Map<String, Object>> filterPatient(String condition, String name, String token) {
        try {
            String email = tokenService.extractEmail(token);
            Patient patient = patientRepository.findByEmail(email);

            if (patient == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Patient not found");
                return ResponseEntity.status(404).body(response);
            }

            if (condition != null && !condition.trim().isEmpty() &&
                    name != null && !name.trim().isEmpty()) {
                return patientService.filterByDoctorAndCondition(condition, name, patient.getId());

            } else if (condition != null && !condition.trim().isEmpty()) {
                return patientService.filterByCondition(condition, patient.getId());

            } else if (name != null && !name.trim().isEmpty()) {
                return patientService.filterByDoctor(name, patient.getId());

            } else {
                return patientService.getPatientAppointment(patient.getId(), token);
            }

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Error filtering patient data");
            return ResponseEntity.status(500).body(response);
        }
    }
}
