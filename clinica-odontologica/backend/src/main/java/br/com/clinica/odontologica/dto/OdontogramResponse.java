package br.com.clinica.odontologica.dto;

import br.com.clinica.odontologica.domain.OdontogramEntry;
import java.time.LocalDateTime;

public record OdontogramResponse(Long id, Long patientId, String toothNumber, String condition, String observation, LocalDateTime updatedAt) {
    public static OdontogramResponse from(OdontogramEntry e) { return new OdontogramResponse(e.getId(), e.getPatient().getId(), e.getToothNumber(), e.getCondition(), e.getObservation(), e.getUpdatedAt()); }
}
