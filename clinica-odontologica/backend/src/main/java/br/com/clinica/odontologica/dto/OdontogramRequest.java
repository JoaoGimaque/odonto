package br.com.clinica.odontologica.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record OdontogramRequest(@NotNull Long patientId, @NotBlank @Pattern(regexp="[1-8][1-8]") String toothNumber,
        @NotBlank String condition, String observation) {}
