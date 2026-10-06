package br.com.clinica.odontologica.dto;

import java.math.BigDecimal;
import java.util.List;

public record DashboardResponse(long patients, long dentists, long todayAppointments, long weekAppointments,
        long pendingAppointments, BigDecimal monthRevenue, List<AppointmentResponse> upcoming) {}
