package br.com.clinica.odontologica.controller;

import br.com.clinica.odontologica.dto.PaymentRequest;
import br.com.clinica.odontologica.dto.PaymentResponse;
import br.com.clinica.odontologica.service.PaymentService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController @RequestMapping("/api/pagamentos")
@PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
public class PaymentController {
    private final PaymentService service;
    public PaymentController(PaymentService service) { this.service=service; }
    @GetMapping public List<PaymentResponse> list() { return service.list(); }
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public PaymentResponse create(@Valid @RequestBody PaymentRequest request) { return service.create(request); }
}
