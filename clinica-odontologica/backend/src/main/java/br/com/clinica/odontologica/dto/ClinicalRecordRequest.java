package br.com.clinica.odontologica.dto;

import jakarta.validation.constraints.NotNull;

public record ClinicalRecordRequest(@NotNull Long patientId, String chiefComplaint, String anamnesis, String diagnosis,
        String observations, String procedures, String medications, String allergies, String dentalHistory) {}
