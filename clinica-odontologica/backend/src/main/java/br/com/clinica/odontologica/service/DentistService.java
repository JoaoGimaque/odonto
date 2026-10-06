package br.com.clinica.odontologica.service;

import br.com.clinica.odontologica.domain.Dentist;
import br.com.clinica.odontologica.dto.DentistRequest;
import br.com.clinica.odontologica.dto.DentistResponse;
import br.com.clinica.odontologica.exception.ResourceNotFoundException;
import br.com.clinica.odontologica.repository.DentistRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DentistService {
    private final DentistRepository repository;
    public DentistService(DentistRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public Page<DentistResponse> list(String query, Pageable pageable) {
        String term = query == null ? "" : query.trim();
        return repository.findByNameContainingIgnoreCaseOrCroContainingIgnoreCase(term, term, pageable).map(DentistResponse::from);
    }
    public DentistResponse create(DentistRequest request) {
        return DentistResponse.from(repository.save(new Dentist(request.name().trim(), request.cro().trim(), request.specialty().trim(), request.phone(), request.email().trim().toLowerCase())));
    }
    public DentistResponse update(Long id, DentistRequest request) {
        Dentist dentist = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dentista", id));
        dentist.update(request.name().trim(), request.cro().trim(), request.specialty().trim(), request.phone(), request.email().trim().toLowerCase(), request.active() == null || request.active());
        return DentistResponse.from(dentist);
    }
    public void deactivate(Long id) {
        Dentist dentist = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dentista", id));
        dentist.update(dentist.getName(), dentist.getCro(), dentist.getSpecialty(), dentist.getPhone(), dentist.getEmail(), false);
    }
    public Dentist require(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Dentista", id)); }
}
