package br.com.clinica.odontologica.dto;

import br.com.clinica.odontologica.domain.ClinicalRecord;
import java.time.LocalDateTime;

public record ClinicalRecordResponse(Long id, Long patientId, String patientName, String chiefComplaint, String anamnesis,
        String diagnosis, String observations, String procedures, String medications, String allergies, String dentalHistory, LocalDateTime updatedAt) {
    public static ClinicalRecordResponse from(ClinicalRecord r) { return new ClinicalRecordResponse(r.getId(), r.getPatient().getId(), r.getPatient().getName(), r.getChiefComplaint(), r.getAnamnesis(), r.getDiagnosis(), r.getObservations(), r.getProcedures(), r.getMedications(), r.getAllergies(), r.getDentalHistory(), r.getUpdatedAt()); }
}
