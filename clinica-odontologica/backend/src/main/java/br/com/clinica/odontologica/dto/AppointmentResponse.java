package br.com.clinica.odontologica.dto;

import br.com.clinica.odontologica.domain.Appointment;
import br.com.clinica.odontologica.domain.AppointmentStatus;
import java.time.LocalDateTime;

public record AppointmentResponse(Long id, Long patientId, String patientName, Long dentistId, String dentistName,
        LocalDateTime startsAt, LocalDateTime endsAt, String type, String notes, AppointmentStatus status) {
    public static AppointmentResponse from(Appointment appointment) {
        return new AppointmentResponse(appointment.getId(), appointment.getPatient().getId(), appointment.getPatient().getName(),
                appointment.getDentist().getId(), appointment.getDentist().getName(), appointment.getStartsAt(), appointment.getEndsAt(),
                appointment.getType(), appointment.getNotes(), appointment.getStatus());
    }
}
