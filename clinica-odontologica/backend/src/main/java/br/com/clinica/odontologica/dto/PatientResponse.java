package br.com.clinica.odontologica.dto;

import br.com.clinica.odontologica.domain.Patient;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record PatientResponse(Long id, String name, String cpf, LocalDate birthDate, String gender, String phone,
        String email, String address, String notes, boolean active, LocalDateTime createdAt) {
    public static PatientResponse from(Patient patient) {
        return new PatientResponse(patient.getId(), patient.getName(), patient.getCpf(), patient.getBirthDate(), patient.getGender(),
                patient.getPhone(), patient.getEmail(), patient.getAddress(), patient.getNotes(), patient.isActive(), patient.getCreatedAt());
    }
}
