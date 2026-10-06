package br.com.clinica.odontologica.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment")
public class Appointment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "patient_id")
    private Patient patient;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "dentist_id")
    private Dentist dentist;
    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;
    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;
    @Column(name = "appointment_type", nullable = false, length = 120)
    private String type;
    @Column(columnDefinition = "TEXT")
    private String notes;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 24)
    private AppointmentStatus status = AppointmentStatus.AGENDADA;

    protected Appointment() {}
    public Appointment(Patient patient, Dentist dentist, LocalDateTime startsAt, LocalDateTime endsAt, String type, String notes, AppointmentStatus status) {
        this.patient = patient; this.dentist = dentist; this.startsAt = startsAt; this.endsAt = endsAt; this.type = type; this.notes = notes; this.status = status;
    }
    public void update(Patient patient, Dentist dentist, LocalDateTime startsAt, LocalDateTime endsAt, String type, String notes, AppointmentStatus status) {
        this.patient = patient; this.dentist = dentist; this.startsAt = startsAt; this.endsAt = endsAt; this.type = type; this.notes = notes; this.status = status;
    }
    public Long getId() { return id; }
    public Patient getPatient() { return patient; }
    public Dentist getDentist() { return dentist; }
    public LocalDateTime getStartsAt() { return startsAt; }
    public LocalDateTime getEndsAt() { return endsAt; }
    public String getType() { return type; }
    public String getNotes() { return notes; }
    public AppointmentStatus getStatus() { return status; }
}
