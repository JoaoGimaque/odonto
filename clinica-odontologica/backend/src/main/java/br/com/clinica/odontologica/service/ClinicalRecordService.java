package br.com.clinica.odontologica.service;

import br.com.clinica.odontologica.domain.ClinicalRecord;
import br.com.clinica.odontologica.dto.ClinicalRecordRequest;
import br.com.clinica.odontologica.dto.ClinicalRecordResponse;
import br.com.clinica.odontologica.exception.ResourceNotFoundException;
import br.com.clinica.odontologica.repository.ClinicalRecordRepository;
import br.com.clinica.odontologica.repository.PatientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class ClinicalRecordService {
    private final ClinicalRecordRepository records; private final PatientRepository patients;
    public ClinicalRecordService(ClinicalRecordRepository records, PatientRepository patients) { this.records=records; this.patients=patients; }
    @Transactional(readOnly=true) public ClinicalRecordResponse get(Long patientId) { return ClinicalRecordResponse.from(records.findByPatientId(patientId).orElseThrow(() -> new ResourceNotFoundException("Prontuário do paciente", patientId))); }
    public ClinicalRecordResponse save(ClinicalRecordRequest request) {
        var patient=patients.findById(request.patientId()).orElseThrow(() -> new ResourceNotFoundException("Paciente", request.patientId()));
        var record=records.findByPatientId(request.patientId()).orElseGet(() -> new ClinicalRecord(patient));
        record.update(request.chiefComplaint(), request.anamnesis(), request.diagnosis(), request.observations(), request.procedures(), request.medications(), request.allergies(), request.dentalHistory());
        return ClinicalRecordResponse.from(records.save(record));
    }
}
