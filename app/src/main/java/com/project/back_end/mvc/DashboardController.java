package com.project.back_end.mvc;

import com.project.back_end.services.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Map;

@Controller
public class DashboardController {

    @Autowired
    private AppService appService;

    @GetMapping("/adminDashboard/{token}")
    public String adminDashboard(@PathVariable String token) {
        ResponseEntity<Map<String, String>> validationResult =
                appService.validateToken(token, "admin");

        if (validationResult.getStatusCode().isError()) {
            return "redirect:http://localhost:8080";
        }

        return "admin/adminDashboard";
    }

    @GetMapping("/doctorDashboard/{token}")
    public String doctorDashboard(@PathVariable String token) {
        ResponseEntity<Map<String, String>> validationResult =
                appService.validateToken(token, "doctor");

        if (validationResult.getStatusCode().isError()) {
            return "redirect:http://localhost:8080";
        }

        return "doctor/doctorDashboard";
    }
}
