package br.com.clinica.odontologica.dto;

import br.com.clinica.odontologica.domain.AppointmentStatus;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

public record AppointmentRequest(@NotNull Long patientId, @NotNull Long dentistId,
        @NotNull @Future LocalDateTime startsAt, @NotNull LocalDateTime endsAt,
        @NotBlank String type, String notes, AppointmentStatus status) {}
