package br.com.clinica.odontologica.service;

import br.com.clinica.odontologica.domain.Patient;
import br.com.clinica.odontologica.dto.PatientRequest;
import br.com.clinica.odontologica.dto.PatientResponse;
import br.com.clinica.odontologica.exception.ResourceNotFoundException;
import br.com.clinica.odontologica.repository.PatientRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PatientService {
    private final PatientRepository repository;
    public PatientService(PatientRepository repository) { this.repository = repository; }
    @Transactional(readOnly = true)
    public Page<PatientResponse> list(String query, Pageable pageable) {
        String term = query == null ? "" : query.trim();
        return repository.findByActiveTrueAndNameContainingIgnoreCaseOrActiveTrueAndCpfContaining(term, term, pageable).map(PatientResponse::from);
    }
    @Transactional(readOnly = true)
    public PatientResponse get(Long id) { return PatientResponse.from(require(id)); }
    public PatientResponse create(PatientRequest request) {
        Patient patient = new Patient(request.name().trim(), request.cpf().replaceAll("\\D", ""), request.birthDate(), request.gender(), request.phone(), request.email(), request.address(), request.notes());
        return PatientResponse.from(repository.save(patient));
    }
    public PatientResponse update(Long id, PatientRequest request) {
        Patient patient = require(id);
        patient.update(request.name().trim(), request.cpf().replaceAll("\\D", ""), request.birthDate(), request.gender(), request.phone(), request.email(), request.address(), request.notes());
        return PatientResponse.from(patient);
    }
    public void delete(Long id) { require(id).deactivate(); }
    public Patient require(Long id) { return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Paciente", id)); }
}
