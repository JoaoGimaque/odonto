package br.com.clinica.odontologica.dto;

import br.com.clinica.odontologica.domain.DentalProcedure;
import java.math.BigDecimal;

public record ProcedureResponse(Long id, String name, String description, BigDecimal price, int durationMinutes, boolean active) {
    public static ProcedureResponse from(DentalProcedure p) { return new ProcedureResponse(p.getId(),p.getName(),p.getDescription(),p.getPrice(),p.getDurationMinutes(),p.isActive()); }
}
