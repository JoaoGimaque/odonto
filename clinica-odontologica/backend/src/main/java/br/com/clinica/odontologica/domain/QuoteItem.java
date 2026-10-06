package br.com.clinica.odontologica.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity @Table(name="quote_item")
public class QuoteItem {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="quote_id") private Quote quote;
    @ManyToOne(optional=false,fetch=FetchType.LAZY) @JoinColumn(name="procedure_id") private DentalProcedure procedure;
    @Column(nullable=false) private int quantity;
    @Column(name="unit_price",nullable=false,precision=12,scale=2) private BigDecimal unitPrice;
    protected QuoteItem() {}
    public QuoteItem(DentalProcedure procedure,int quantity,BigDecimal unitPrice){this.procedure=procedure;this.quantity=quantity;this.unitPrice=unitPrice;}
    void setQuote(Quote quote){this.quote=quote;}
    public Long getId(){return id;} public DentalProcedure getProcedure(){return procedure;} public int getQuantity(){return quantity;} public BigDecimal getUnitPrice(){return unitPrice;}
}
