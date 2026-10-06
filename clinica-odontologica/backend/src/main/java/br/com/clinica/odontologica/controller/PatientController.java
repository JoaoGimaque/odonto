package br.com.clinica.odontologica.controller;

import br.com.clinica.odontologica.dto.PatientRequest;
import br.com.clinica.odontologica.dto.PatientResponse;
import br.com.clinica.odontologica.service.PatientService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/pacientes")
@PreAuthorize("hasAnyRole('ADMIN','DENTISTA','RECEPCIONISTA')")
public class PatientController {
    private final PatientService service;
    public PatientController(PatientService service) { this.service = service; }
    @GetMapping public Page<PatientResponse> list(@RequestParam(defaultValue="") String q,
            @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="20") int size) {
        return service.list(q, PageRequest.of(Math.max(page,0), Math.min(Math.max(size,1),100), Sort.by("name").ascending()));
    }
    @GetMapping("/{id}") public PatientResponse get(@PathVariable Long id) { return service.get(id); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public PatientResponse create(@Valid @RequestBody PatientRequest request) { return service.create(request); }
    @PutMapping("/{id}") public PatientResponse update(@PathVariable Long id, @Valid @RequestBody PatientRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.delete(id); }
}
