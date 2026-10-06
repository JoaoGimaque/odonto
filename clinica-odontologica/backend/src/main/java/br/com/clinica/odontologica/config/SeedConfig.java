package br.com.clinica.odontologica.config;

import br.com.clinica.odontologica.domain.*;
import br.com.clinica.odontologica.repository.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Profile("dev")
public class SeedConfig {
    @Bean CommandLineRunner seedData(AppUserRepository users, DentistRepository dentists, PatientRepository patients,
            DentalProcedureRepository procedures, AppointmentRepository appointments, PaymentRepository payments,
            PasswordEncoder encoder, @Value("${app.seed.enabled}") boolean enabled,
            @Value("${app.seed.admin-email}") String email, @Value("${app.seed.admin-password}") String password,
            @Value("${app.seed.dentist-email}") String dentistEmail, @Value("${app.seed.dentist-password}") String dentistPassword) {
        return args -> {
            if (!enabled) return;
            if (password != null && !password.isBlank() && users.findByEmailIgnoreCase(email).isEmpty())
                users.save(new AppUser("Administrador", email.trim().toLowerCase(), encoder.encode(password), Role.ADMIN));
            if (dentistPassword != null && !dentistPassword.isBlank() && users.findByEmailIgnoreCase(dentistEmail).isEmpty())
                users.save(new AppUser("Dra. Marina Costa", dentistEmail.trim().toLowerCase(), encoder.encode(dentistPassword), Role.DENTISTA));
            if (dentists.count() == 0) dentists.saveAll(List.of(
                    new Dentist("Dra. Marina Costa", "CRO-SP-12345", "Clínica geral", "11999990001", "marina@clinica.local"),
                    new Dentist("Dr. Rafael Lima", "CRO-SP-67890", "Endodontia", "11999990002", "rafael@clinica.local")));
            if (patients.count() == 0) patients.saveAll(List.of(
                    new Patient("Ana Souza", "12345678901", LocalDate.of(1990, 5, 12), "FEMININO", "11988880001", "ana@example.local", "Rua das Flores, 10", null),
                    new Patient("Bruno Alves", "23456789012", LocalDate.of(1985, 8, 23), "MASCULINO", "11988880002", "bruno@example.local", "Av. Central, 25", null),
                    new Patient("Carla Mendes", "34567890123", LocalDate.of(1998, 2, 4), "FEMININO", "11988880003", "carla@example.local", "Rua Azul, 88", null),
                    new Patient("Diego Rocha", "45678901234", LocalDate.of(1979, 11, 16), "MASCULINO", "11988880004", "diego@example.local", "Rua Verde, 2", null),
                    new Patient("Elisa Nunes", "56789012345", LocalDate.of(2001, 7, 30), "FEMININO", "11988880005", "elisa@example.local", "Rua Sul, 41", null)));
            if (procedures.count() == 0) procedures.saveAll(List.of(
                    new DentalProcedure("Limpeza", "Profilaxia e polimento", new BigDecimal("180.00"), 45),
                    new DentalProcedure("Restauração", "Restauração em resina", new BigDecimal("320.00"), 60),
                    new DentalProcedure("Clareamento", "Clareamento supervisionado", new BigDecimal("900.00"), 90),
                    new DentalProcedure("Extração", "Exodontia simples", new BigDecimal("420.00"), 60),
                    new DentalProcedure("Canal", "Tratamento endodôntico", new BigDecimal("1200.00"), 90)));
            if (appointments.count() == 0 && patients.count() > 0 && dentists.count() > 0) {
                var patientList = patients.findAll(); var dentistList = dentists.findAll();
                LocalDateTime tomorrow = LocalDate.now().plusDays(1).atTime(9, 0);
                appointments.saveAll(List.of(
                        new Appointment(patientList.get(0), dentistList.get(0), tomorrow, tomorrow.plusMinutes(45), "Limpeza", null, AppointmentStatus.CONFIRMADA),
                        new Appointment(patientList.get(1), dentistList.get(1), tomorrow.plusHours(1), tomorrow.plusHours(2), "Avaliação", null, AppointmentStatus.AGENDADA)));
            }
            if (payments.count() == 0 && patients.count() > 0 && procedures.count() > 0)
                payments.save(new Payment(patients.findAll().get(0), procedures.findAll().get(0), new BigDecimal("180.00"), LocalDateTime.now(), "PIX", "PAGO", "Profilaxia"));
        };
    }
}
