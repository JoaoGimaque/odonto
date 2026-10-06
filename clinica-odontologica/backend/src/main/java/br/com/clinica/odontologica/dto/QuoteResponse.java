package br.com.clinica.odontologica.dto;

import br.com.clinica.odontologica.domain.Quote;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record QuoteResponse(Long id, Long patientId, String patientName, LocalDateTime createdAt, BigDecimal discount,
        BigDecimal total, String status, String notes, List<Item> items) {
    public record Item(Long procedureId, String procedureName, int quantity, BigDecimal unitPrice) {}
    public static QuoteResponse from(Quote q) { return new QuoteResponse(q.getId(),q.getPatient().getId(),q.getPatient().getName(),q.getCreatedAt(),q.getDiscount(),q.getTotal(),q.getStatus(),q.getNotes(),q.getItems().stream().map(i -> new Item(i.getProcedure().getId(),i.getProcedure().getName(),i.getQuantity(),i.getUnitPrice())).toList()); }
}
