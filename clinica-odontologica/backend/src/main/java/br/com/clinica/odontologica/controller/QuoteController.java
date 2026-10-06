package br.com.clinica.odontologica.controller;

import br.com.clinica.odontologica.dto.QuoteRequest;
import br.com.clinica.odontologica.dto.QuoteResponse;
import br.com.clinica.odontologica.service.QuoteService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.Valid;

@RestController @RequestMapping("/api/orcamentos")
@PreAuthorize("hasAnyRole('ADMIN','RECEPCIONISTA')")
public class QuoteController {
    private final QuoteService service;
    public QuoteController(QuoteService service){this.service=service;}
    @GetMapping public List<QuoteResponse> list(){return service.list();}
    @PostMapping @ResponseStatus(HttpStatus.CREATED) public QuoteResponse create(@Valid @RequestBody QuoteRequest request){return service.create(request);}
    @PutMapping("/{id}/status") public QuoteResponse updateStatus(@PathVariable Long id,@Valid @RequestBody StatusRequest request){return service.updateStatus(id,request.status());}
    public record StatusRequest(@NotBlank String status) {}
}
