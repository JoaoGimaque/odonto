package br.com.clinica.odontologica.controller;

import br.com.clinica.odontologica.dto.AppointmentRequest;
import br.com.clinica.odontologica.dto.AppointmentResponse;
import br.com.clinica.odontologica.service.AppointmentService;
import jakarta.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/consultas")
public class AppointmentController {
    private final AppointmentService service;
    public AppointmentController(AppointmentService service) { this.service=service; }
    @GetMapping public List<AppointmentResponse> list(@RequestParam(required=false) LocalDate from, @RequestParam(required=false) LocalDate to) { return service.list(from,to); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public AppointmentResponse create(@Valid @RequestBody AppointmentRequest request) { return service.create(request); }
    @PutMapping("/{id}") public AppointmentResponse update(@PathVariable Long id, @Valid @RequestBody AppointmentRequest request) { return service.update(id,request); }
    @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void cancel(@PathVariable Long id) { service.cancel(id); }
}
