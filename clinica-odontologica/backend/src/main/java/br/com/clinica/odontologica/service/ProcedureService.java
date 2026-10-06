package br.com.clinica.odontologica.service;

import br.com.clinica.odontologica.domain.DentalProcedure;
import br.com.clinica.odontologica.dto.ProcedureRequest;
import br.com.clinica.odontologica.dto.ProcedureResponse;
import br.com.clinica.odontologica.exception.ResourceNotFoundException;
import br.com.clinica.odontologica.repository.DentalProcedureRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class ProcedureService {
    private final DentalProcedureRepository repository;
    public ProcedureService(DentalProcedureRepository repository) { this.repository=repository; }
    @Transactional(readOnly=true) public List<ProcedureResponse> list() { return repository.findAll().stream().map(ProcedureResponse::from).toList(); }
    public ProcedureResponse create(ProcedureRequest r) { return ProcedureResponse.from(repository.save(new DentalProcedure(r.name().trim(),r.description(),r.price(),r.durationMinutes()))); }
    public ProcedureResponse update(Long id, ProcedureRequest r) {
        DentalProcedure p=repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Procedimento",id));
        p.update(r.name().trim(),r.description(),r.price(),r.durationMinutes(),r.active()==null || r.active()); return ProcedureResponse.from(p);
    }
    public void deactivate(Long id) { DentalProcedure p=repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Procedimento",id)); p.update(p.getName(),p.getDescription(),p.getPrice(),p.getDurationMinutes(),false); }
}
