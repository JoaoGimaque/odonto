package br.com.clinica.odontologica.controller;

import br.com.clinica.odontologica.dto.DentistRequest;
import br.com.clinica.odontologica.dto.DentistResponse;
import br.com.clinica.odontologica.service.DentistService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/dentistas")
public class DentistController {
    private final DentistService service;
    public DentistController(DentistService service) { this.service = service; }
    @GetMapping @PreAuthorize("hasAnyRole('ADMIN','DENTISTA','RECEPCIONISTA')") public Page<DentistResponse> list(@RequestParam(defaultValue="") String q,
            @RequestParam(defaultValue="0") int page, @RequestParam(defaultValue="100") int size) {
        return service.list(q, PageRequest.of(Math.max(page,0), Math.min(Math.max(size,1),100), Sort.by("name").ascending()));
    }
    @PostMapping @PreAuthorize("hasRole('ADMIN')") @ResponseStatus(HttpStatus.CREATED) public DentistResponse create(@Valid @RequestBody DentistRequest request) { return service.create(request); }
    @PutMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") public DentistResponse update(@PathVariable Long id, @Valid @RequestBody DentistRequest request) { return service.update(id, request); }
    @DeleteMapping("/{id}") @PreAuthorize("hasRole('ADMIN')") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id) { service.deactivate(id); }
}
