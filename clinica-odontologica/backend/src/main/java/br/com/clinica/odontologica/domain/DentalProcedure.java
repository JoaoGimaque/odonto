package br.com.clinica.odontologica.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "dental_procedure")
public class DentalProcedure {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true, length = 120) private String name;
    @Column(columnDefinition = "TEXT") private String description;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal price;
    @Column(name = "duration_minutes", nullable = false) private int durationMinutes;
    @Column(nullable = false) private boolean active = true;
    protected DentalProcedure() {}
    public DentalProcedure(String name, String description, BigDecimal price, int durationMinutes) { this.name=name; this.description=description; this.price=price; this.durationMinutes=durationMinutes; }
    public void update(String name, String description, BigDecimal price, int durationMinutes, boolean active) { this.name=name; this.description=description; this.price=price; this.durationMinutes=durationMinutes; this.active=active; }
    public Long getId() { return id; } public String getName() { return name; } public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; } public int getDurationMinutes() { return durationMinutes; } public boolean isActive() { return active; }
}
