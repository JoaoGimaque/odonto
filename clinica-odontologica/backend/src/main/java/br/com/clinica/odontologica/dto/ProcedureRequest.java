package br.com.clinica.odontologica.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProcedureRequest(@NotBlank String name, String description, @NotNull @DecimalMin("0.00") BigDecimal price,
        @Min(1) int durationMinutes, Boolean active) {}
