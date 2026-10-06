package br.com.clinica.odontologica.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clinical_record")
public class ClinicalRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "patient_id", unique = true)
    private Patient patient;
    @Column(name = "chief_complaint", columnDefinition = "TEXT") private String chiefComplaint;
    @Column(columnDefinition = "TEXT") private String anamnesis;
    @Column(columnDefinition = "TEXT") private String diagnosis;
    @Column(columnDefinition = "TEXT") private String observations;
    @Column(columnDefinition = "TEXT") private String procedures;
    @Column(columnDefinition = "TEXT") private String medications;
    @Column(columnDefinition = "TEXT") private String allergies;
    @Column(name = "dental_history", columnDefinition = "TEXT") private String dentalHistory;
    @Column(name = "updated_at", insertable = false, updatable = false) private LocalDateTime updatedAt;

    protected ClinicalRecord() {}
    public ClinicalRecord(Patient patient) { this.patient = patient; }
    public void update(String chiefComplaint, String anamnesis, String diagnosis, String observations, String procedures, String medications, String allergies, String dentalHistory) {
        this.chiefComplaint = chiefComplaint; this.anamnesis = anamnesis; this.diagnosis = diagnosis; this.observations = observations;
        this.procedures = procedures; this.medications = medications; this.allergies = allergies; this.dentalHistory = dentalHistory;
    }
    public Long getId() { return id; }
    public Patient getPatient() { return patient; }
    public String getChiefComplaint() { return chiefComplaint; }
    public String getAnamnesis() { return anamnesis; }
    public String getDiagnosis() { return diagnosis; }
    public String getObservations() { return observations; }
    public String getProcedures() { return procedures; }
    public String getMedications() { return medications; }
    public String getAllergies() { return allergies; }
    public String getDentalHistory() { return dentalHistory; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}
