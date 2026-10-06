package br.com.clinica.odontologica.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "odontogram_entry", uniqueConstraints = @UniqueConstraint(name = "uk_odontogram_patient_tooth", columnNames = {"patient_id", "tooth_number"}))
public class OdontogramEntry {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "patient_id") private Patient patient;
    @Column(name = "tooth_number", nullable = false, length = 3) private String toothNumber;
    @Column(name = "condition_type", nullable = false, length = 24) private String condition;
    @Column(columnDefinition = "TEXT") private String observation;
    @Column(name = "updated_at", insertable = false, updatable = false) private LocalDateTime updatedAt;
    protected OdontogramEntry() {}
    public OdontogramEntry(Patient patient, String toothNumber, String condition, String observation) { this.patient=patient; this.toothNumber=toothNumber; this.condition=condition; this.observation=observation; }
    public void update(String condition, String observation) { this.condition=condition; this.observation=observation; }
    public Long getId(){return id;} public Patient getPatient(){return patient;} public String getToothNumber(){return toothNumber;}
    public String getCondition(){return condition;} public String getObservation(){return observation;} public LocalDateTime getUpdatedAt(){return updatedAt;}
}
