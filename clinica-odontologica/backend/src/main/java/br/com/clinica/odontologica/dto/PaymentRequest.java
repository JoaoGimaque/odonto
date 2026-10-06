package br.com.clinica.odontologica.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentRequest(@NotNull Long patientId, Long procedureId, @NotNull @DecimalMin("0.01") BigDecimal amount,
        @NotNull LocalDateTime paidAt, @NotBlank String method, @NotBlank String status, String description) {}
