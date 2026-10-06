package br.com.clinica.odontologica.repository;

import br.com.clinica.odontologica.domain.Payment;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    @Query("select coalesce(sum(p.amount), 0) from Payment p where p.status = 'PAGO' and p.paidAt between :from and :to")
    BigDecimal sumPaidBetween(LocalDateTime from, LocalDateTime to);
}
