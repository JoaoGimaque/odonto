package br.com.clinica.odontologica.repository;

import br.com.clinica.odontologica.domain.Appointment;
import br.com.clinica.odontologica.domain.AppointmentStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    boolean existsByDentistIdAndStartsAtLessThanAndEndsAtGreaterThan(Long dentistId, LocalDateTime endsAt, LocalDateTime startsAt);
    boolean existsByDentistIdAndStartsAtLessThanAndEndsAtGreaterThanAndIdNot(Long dentistId, LocalDateTime endsAt, LocalDateTime startsAt, Long id);
    List<Appointment> findByStartsAtBetweenOrderByStartsAt(LocalDateTime from, LocalDateTime to);
    List<Appointment> findTop8ByStartsAtAfterOrderByStartsAt(LocalDateTime now);
    long countByStartsAtBetween(LocalDateTime from, LocalDateTime to);
    long countByStatusNotAndStatusNotAndStartsAtBetween(AppointmentStatus cancelled, AppointmentStatus completed, LocalDateTime from, LocalDateTime to);
}
