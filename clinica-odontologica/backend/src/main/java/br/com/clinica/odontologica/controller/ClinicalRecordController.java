package br.com.clinica.odontologica.controller;

import br.com.clinica.odontologica.dto.ClinicalRecordRequest;
import br.com.clinica.odontologica.dto.ClinicalRecordResponse;
import br.com.clinica.odontologica.service.ClinicalRecordService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController @RequestMapping("/api/prontuarios")
@PreAuthorize("hasAnyRole('ADMIN','DENTISTA')")
public class ClinicalRecordController {
    private final ClinicalRecordService service;
    public ClinicalRecordController(ClinicalRecordService service) { this.service=service; }
    @GetMapping("/paciente/{patientId}") public ClinicalRecordResponse get(@PathVariable Long patientId) { return service.get(patientId); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public ClinicalRecordResponse save(@Valid @RequestBody ClinicalRecordRequest request) { return service.save(request); }
    @PutMapping @ResponseStatus(HttpStatus.OK) public ClinicalRecordResponse update(@Valid @RequestBody ClinicalRecordRequest request) { return service.save(request); }
}
