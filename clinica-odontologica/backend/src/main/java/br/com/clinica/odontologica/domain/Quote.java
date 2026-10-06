package br.com.clinica.odontologica.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity @Table(name="quote")
public class Quote {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="patient_id") private Patient patient;
    @Column(name="created_at",insertable=false,updatable=false) private LocalDateTime createdAt;
    @Column(nullable=false,precision=12,scale=2) private BigDecimal discount;
    @Column(nullable=false,precision=12,scale=2) private BigDecimal total;
    @Column(nullable=false,length=24) private String status;
    @Column(columnDefinition="TEXT") private String notes;
    @OneToMany(mappedBy="quote",cascade=CascadeType.ALL,orphanRemoval=true) private java.util.List<QuoteItem> items=new java.util.ArrayList<>();
    protected Quote() {}
    public Quote(Patient patient, BigDecimal discount, BigDecimal total, String status, String notes) { this.patient=patient; this.discount=discount; this.total=total; this.status=status; this.notes=notes; }
    public void addItem(QuoteItem item){items.add(item); item.setQuote(this);}
    public void calculateTotal(BigDecimal subtotal){this.total=subtotal.subtract(discount);}
    public void changeStatus(String status){this.status=status;}
    public Long getId(){return id;} public Patient getPatient(){return patient;} public LocalDateTime getCreatedAt(){return createdAt;} public BigDecimal getDiscount(){return discount;} public BigDecimal getTotal(){return total;} public String getStatus(){return status;} public String getNotes(){return notes;} public java.util.List<QuoteItem> getItems(){return items;}
}
