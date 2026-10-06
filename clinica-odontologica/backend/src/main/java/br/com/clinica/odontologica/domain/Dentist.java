package br.com.clinica.odontologica.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "dentist")
public class Dentist {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 120)
    private String name;
    @Column(nullable = false, unique = true, length = 40)
    private String cro;
    @Column(nullable = false, length = 100)
    private String specialty;
    @Column(length = 30)
    private String phone;
    @Column(nullable = false, unique = true, length = 180)
    private String email;
    @Column(nullable = false)
    private boolean active = true;

    protected Dentist() {}
    public Dentist(String name, String cro, String specialty, String phone, String email) {
        this.name = name; this.cro = cro; this.specialty = specialty; this.phone = phone; this.email = email;
    }
    public void update(String name, String cro, String specialty, String phone, String email, boolean active) {
        this.name = name; this.cro = cro; this.specialty = specialty; this.phone = phone; this.email = email; this.active = active;
    }
    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCro() { return cro; }
    public String getSpecialty() { return specialty; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public boolean isActive() { return active; }
}
