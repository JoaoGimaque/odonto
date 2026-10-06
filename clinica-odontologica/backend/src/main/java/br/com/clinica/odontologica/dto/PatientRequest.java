package br.com.clinica.odontologica.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record PatientRequest(@NotBlank @Size(max=140) String name, @NotBlank @Size(max=14) String cpf,
        LocalDate birthDate, String gender, String phone, @Email String email, String address, String notes) {}
