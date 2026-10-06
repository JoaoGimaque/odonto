package br.com.clinica.odontologica.domain;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient")
public class Patient {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 140)
    private String name;
    @Column(nullable = false, unique = true, length = 14)
    private String cpf;
    @Column(name = "birth_date")
    private LocalDate birthDate;
    @Column(length = 24)
    private String gender;
    @Column(length = 30)
    private String phone;
    @Column(length = 180)
    private String email;
    @Column(length = 255)
    private String address;
    @Column(columnDefinition = "TEXT")
    private String notes;
    @Column(nullable = false)
    private boolean active = true;
    @Column(name = "created_at", nullable = false, insertable = false, updatable = false)
    private LocalDateTime createdAt;

    protected Patient() {}
    public Patient(String name, String cpf, LocalDate birthDate, String gender, String phone, String email, String address, String notes) {
        this.name = name; this.cpf = cpf; this.birthDate = birthDate; this.gender = gender;
        this.phone = phone; this.email = email; this.address = address; this.notes = notes;
    }
    public void update(String name, String cpf, LocalDate birthDate, String gender, String phone, String email, String address, String notes) {
        this.name = name; this.cpf = cpf; this.birthDate = birthDate; this.gender = gender;
        this.phone = phone; this.email = email; this.address = address; this.notes = notes;
    }
    public void deactivate() { this.active = false; }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCpf() { return cpf; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getGender() { return gender; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getNotes() { return notes; }
    public boolean isActive() { return active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
