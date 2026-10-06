package br.com.clinica.odontologica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record DentistRequest(@NotBlank String name, @NotBlank String cro, @NotBlank String specialty,
        String phone, @NotBlank @Email String email, Boolean active) {}
