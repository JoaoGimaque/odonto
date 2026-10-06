package br.com.clinica.odontologica.service;

import br.com.clinica.odontologica.domain.Appointment;
import br.com.clinica.odontologica.domain.AppointmentStatus;
import br.com.clinica.odontologica.dto.AppointmentRequest;
import br.com.clinica.odontologica.dto.AppointmentResponse;
import br.com.clinica.odontologica.exception.ResourceNotFoundException;
import br.com.clinica.odontologica.repository.AppointmentRepository;
import br.com.clinica.odontologica.repository.DentistRepository;
import br.com.clinica.odontologica.repository.PatientRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AppointmentService {
    private final AppointmentRepository appointments;
    private final PatientRepository patients;
    private final DentistRepository dentists;
    public AppointmentService(AppointmentRepository appointments, PatientRepository patients, DentistRepository dentists) {
        this.appointments=appointments; this.patients=patients; this.dentists=dentists;
    }
    @Transactional(readOnly = true)
    public List<AppointmentResponse> list(LocalDate from, LocalDate to) {
        LocalDateTime start = (from == null ? LocalDate.now() : from).atStartOfDay();
        LocalDateTime end = (to == null ? start.toLocalDate().plusDays(7) : to.plusDays(1)).atStartOfDay();
        return appointments.findByStartsAtBetweenOrderByStartsAt(start, end).stream().map(AppointmentResponse::from).toList();
    }
    public AppointmentResponse create(AppointmentRequest request) {
        validateRange(request);
        var patient = patients.findById(request.patientId()).orElseThrow(() -> new ResourceNotFoundException("Paciente", request.patientId()));
        var dentist = dentists.findById(request.dentistId()).orElseThrow(() -> new ResourceNotFoundException("Dentista", request.dentistId()));
        if (!patient.isActive() || !dentist.isActive()) throw new IllegalArgumentException("Paciente e dentista precisam estar ativos.");
        checkConflict(dentist.getId(), request.startsAt(), request.endsAt(), null);
        var status = request.status() == null ? AppointmentStatus.AGENDADA : request.status();
        return AppointmentResponse.from(appointments.save(new Appointment(patient, dentist, request.startsAt(), request.endsAt(), request.type(), request.notes(), status)));
    }
    public AppointmentResponse update(Long id, AppointmentRequest request) {
        validateRange(request);
        Appointment appointment = appointments.findById(id).orElseThrow(() -> new ResourceNotFoundException("Consulta", id));
        var patient = patients.findById(request.patientId()).orElseThrow(() -> new ResourceNotFoundException("Paciente", request.patientId()));
        var dentist = dentists.findById(request.dentistId()).orElseThrow(() -> new ResourceNotFoundException("Dentista", request.dentistId()));
        checkConflict(dentist.getId(), request.startsAt(), request.endsAt(), id);
        appointment.update(patient, dentist, request.startsAt(), request.endsAt(), request.type(), request.notes(), request.status() == null ? appointment.getStatus() : request.status());
        return AppointmentResponse.from(appointment);
    }
    public void cancel(Long id) {
        Appointment appointment = appointments.findById(id).orElseThrow(() -> new ResourceNotFoundException("Consulta", id));
        appointment.update(appointment.getPatient(), appointment.getDentist(), appointment.getStartsAt(), appointment.getEndsAt(), appointment.getType(), appointment.getNotes(), AppointmentStatus.CANCELADA);
    }
    private void validateRange(AppointmentRequest request) {
        if (!request.endsAt().isAfter(request.startsAt())) throw new IllegalArgumentException("O fim da consulta deve ser posterior ao início.");
    }
    private void checkConflict(Long dentistId, LocalDateTime startsAt, LocalDateTime endsAt, Long ignoredId) {
        boolean conflict = ignoredId == null
                ? appointments.existsByDentistIdAndStartsAtLessThanAndEndsAtGreaterThan(dentistId, endsAt, startsAt)
                : appointments.existsByDentistIdAndStartsAtLessThanAndEndsAtGreaterThanAndIdNot(dentistId, endsAt, startsAt, ignoredId);
        if (conflict) throw new IllegalArgumentException("O dentista já possui uma consulta nesse intervalo.");
    }
}
