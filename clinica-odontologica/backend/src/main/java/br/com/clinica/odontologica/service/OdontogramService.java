package br.com.clinica.odontologica.service;

import br.com.clinica.odontologica.domain.OdontogramEntry;
import br.com.clinica.odontologica.dto.OdontogramRequest;
import br.com.clinica.odontologica.dto.OdontogramResponse;
import br.com.clinica.odontologica.exception.ResourceNotFoundException;
import br.com.clinica.odontologica.repository.OdontogramEntryRepository;
import br.com.clinica.odontologica.repository.PatientRepository;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class OdontogramService {
    private static final Set<String> CONDITIONS = Set.of("CARIE", "RESTAURACAO", "EXTRACAO", "CANAL", "IMPLANTE", "AUSENTE", "OBSERVACAO");
    private final OdontogramEntryRepository entries; private final PatientRepository patients;
    public OdontogramService(OdontogramEntryRepository entries, PatientRepository patients) { this.entries=entries; this.patients=patients; }
    @Transactional(readOnly=true) public List<OdontogramResponse> list(Long patientId) { return entries.findByPatientIdOrderByToothNumber(patientId).stream().map(OdontogramResponse::from).toList(); }
    public OdontogramResponse save(OdontogramRequest request) {
        if (!CONDITIONS.contains(request.condition())) throw new IllegalArgumentException("Condição odontológica inválida.");
        var patient=patients.findById(request.patientId()).orElseThrow(() -> new ResourceNotFoundException("Paciente", request.patientId()));
        var entry=entries.findByPatientIdAndToothNumber(request.patientId(), request.toothNumber()).orElseGet(() -> new OdontogramEntry(patient, request.toothNumber(), request.condition(), request.observation()));
        entry.update(request.condition(), request.observation());
        return OdontogramResponse.from(entries.save(entry));
    }
}
