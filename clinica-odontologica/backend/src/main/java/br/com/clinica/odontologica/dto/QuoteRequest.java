package br.com.clinica.odontologica.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record QuoteRequest(@NotNull Long patientId, @NotEmpty List<@Valid Item> items,
        @NotNull BigDecimal discount, String status, String notes) {
    public record Item(@NotNull Long procedureId, @Min(1) int quantity) {}
}
