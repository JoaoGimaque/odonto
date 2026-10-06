package br.com.clinica.odontologica.dto;

import br.com.clinica.odontologica.domain.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(Long id, Long patientId, String patientName, Long procedureId, String procedureName,
        BigDecimal amount, LocalDateTime paidAt, String method, String status, String description) {
    public static PaymentResponse from(Payment p) { return new PaymentResponse(p.getId(),p.getPatient().getId(),p.getPatient().getName(),p.getProcedure()==null?null:p.getProcedure().getId(),p.getProcedure()==null?null:p.getProcedure().getName(),p.getAmount(),p.getPaidAt(),p.getMethod(),p.getStatus(),p.getDescription()); }
}
