package br.com.clinica.odontologica.dto;

import br.com.clinica.odontologica.domain.Dentist;

public record DentistResponse(Long id, String name, String cro, String specialty, String phone, String email, boolean active) {
    public static DentistResponse from(Dentist dentist) {
        return new DentistResponse(dentist.getId(), dentist.getName(), dentist.getCro(), dentist.getSpecialty(), dentist.getPhone(), dentist.getEmail(), dentist.isActive());
    }
}
