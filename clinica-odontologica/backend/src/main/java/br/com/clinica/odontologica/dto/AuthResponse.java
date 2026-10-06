package br.com.clinica.odontologica.dto;

public record AuthResponse(String token, String type, Long id, String name, String email, String role) {}
