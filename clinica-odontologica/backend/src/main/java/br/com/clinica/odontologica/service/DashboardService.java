package br.com.clinica.odontologica.service;

import br.com.clinica.odontologica.domain.AppointmentStatus;
import br.com.clinica.odontologica.dto.AppointmentResponse;
import br.com.clinica.odontologica.dto.DashboardResponse;
import br.com.clinica.odontologica.repository.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {
    private final PatientRepository patients; private final DentistRepository dentists; private final AppointmentRepository appointments; private final PaymentRepository payments;
    public DashboardService(PatientRepository patients, DentistRepository dentists, AppointmentRepository appointments, PaymentRepository payments) {
        this.patients=patients; this.dentists=dentists; this.appointments=appointments; this.payments=payments;
    }
    @Transactional(readOnly=true)
    public DashboardResponse summary() {
        LocalDate today=LocalDate.now(); LocalDateTime dayStart=today.atStartOfDay(); LocalDateTime dayEnd=today.plusDays(1).atStartOfDay();
        LocalDate weekEnd=today.with(java.time.DayOfWeek.SUNDAY).plusDays(1);
        LocalDateTime monthStart=today.withDayOfMonth(1).atStartOfDay();
        long todayCount=appointments.countByStartsAtBetween(dayStart,dayEnd);
        long weekCount=appointments.countByStartsAtBetween(dayStart,weekEnd.atStartOfDay());
        long pending=appointments.countByStatusNotAndStatusNotAndStartsAtBetween(AppointmentStatus.CANCELADA, AppointmentStatus.CONCLUIDA, dayStart, weekEnd.atStartOfDay());
        BigDecimal revenue=payments.sumPaidBetween(monthStart,dayEnd);
        List<AppointmentResponse> upcoming=appointments.findTop8ByStartsAtAfterOrderByStartsAt(LocalDateTime.now()).stream().map(AppointmentResponse::from).toList();
        return new DashboardResponse(patients.count(), dentists.count(), todayCount, weekCount, pending, revenue == null ? BigDecimal.ZERO : revenue, upcoming);
    }
}
