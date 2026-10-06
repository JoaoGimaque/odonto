package br.com.clinica.odontologica.controller;

import br.com.clinica.odontologica.dto.ProcedureRequest;
import br.com.clinica.odontologica.dto.ProcedureResponse;
import br.com.clinica.odontologica.service.ProcedureService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController @RequestMapping("/api/procedimentos")
public class ProcedureController {
    private final ProcedureService service;
    public ProcedureController(ProcedureService service) { this.service=service; }
    @GetMapping @PreAuthorize("hasAnyRole('ADMIN','DENTISTA','RECEPCIONISTA')") public List<ProcedureResponse> list() { return service.list(); }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") @ResponseStatus(HttpStatus.CREATED) public ProcedureResponse create(@Valid @RequestBody ProcedureRequest request) { return service.create(request); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public ProcedureResponse update(@PathVariable Long id, @Valid @RequestBody ProcedureRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deactivate(id); }
}
