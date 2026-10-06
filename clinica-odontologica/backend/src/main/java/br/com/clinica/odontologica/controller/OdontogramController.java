package br.com.clinica.odontologica.controller;

import br.com.clinica.odontologica.dto.OdontogramRequest;
import br.com.clinica.odontologica.dto.OdontogramResponse;
import br.com.clinica.odontologica.service.OdontogramService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController @RequestMapping("/api/odontograma")
@PreAuthorize("hasAnyRole('ADMIN','DENTISTA')")
public class OdontogramController {
    private final OdontogramService service;
    public OdontogramController(OdontogramService service) { this.service=service; }
    @GetMapping("/paciente/{patientId}") public List<OdontogramResponse> list(@PathVariable Long patientId) { return service.list(patientId); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public OdontogramResponse save(@Valid @RequestBody OdontogramRequest request) { return service.save(request); }
}
