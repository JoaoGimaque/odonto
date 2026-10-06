package br.com.clinica.odontologica.controller;

import br.com.clinica.odontologica.dto.DashboardResponse;
import br.com.clinica.odontologica.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {
    private final DashboardService service;
    public DashboardController(DashboardService service) { this.service=service; }
    @GetMapping public DashboardResponse summary() { return service.summary(); }
}
