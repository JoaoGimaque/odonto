package br.com.clinica.odontologica.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "patient_id") private Patient patient;
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "procedure_id") private DentalProcedure procedure;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal amount;
    @Column(name = "paid_at", nullable = false) private LocalDateTime paidAt;
    @Column(nullable = false, length = 24) private String method;
    @Column(nullable = false, length = 24) private String status;
    @Column(length = 255) private String description;
    protected Payment() {}
    public Payment(Patient patient, DentalProcedure procedure, BigDecimal amount, LocalDateTime paidAt, String method, String status, String description) {
      this.patient=patient; this.procedure=procedure; this.amount=amount; this.paidAt=paidAt; this.method=method; this.status=status; this.description=description;
    }
    public Long getId(){return id;} public Patient getPatient(){return patient;} public DentalProcedure getProcedure(){return procedure;}
    public BigDecimal getAmount(){return amount;} public LocalDateTime getPaidAt(){return paidAt;} public String getMethod(){return method;}
    public String getStatus(){return status;} public String getDescription(){return description;}
}
