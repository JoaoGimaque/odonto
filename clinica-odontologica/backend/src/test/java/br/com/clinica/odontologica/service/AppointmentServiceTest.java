package br.com.clinica.odontologica.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import br.com.clinica.odontologica.domain.Dentist;
import br.com.clinica.odontologica.domain.Patient;
import br.com.clinica.odontologica.dto.AppointmentRequest;
import br.com.clinica.odontologica.repository.AppointmentRepository;
import br.com.clinica.odontologica.repository.DentistRepository;
import br.com.clinica.odontologica.repository.PatientRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AppointmentServiceTest {
    private AppointmentRepository appointments;
    private PatientRepository patients;
    private DentistRepository dentists;
    private AppointmentService service;

    @BeforeEach void setUp() {
        appointments=mock(AppointmentRepository.class); patients=mock(PatientRepository.class); dentists=mock(DentistRepository.class);
        service=new AppointmentService(appointments,patients,dentists);
    }

    @Test void rejectsAppointmentWhenEndIsNotAfterStart() {
        LocalDateTime start=LocalDate.now().plusDays(2).atTime(10,0);
        var request=new AppointmentRequest(1L,1L,start,start,"Avaliação",null,null);
        assertThrows(IllegalArgumentException.class,()->service.create(request));
        verifyNoInteractions(appointments,patients,dentists);
    }

    @Test void rejectsOverlappingAppointmentsForSameDentist() {
        LocalDateTime start=LocalDate.now().plusDays(2).atTime(10,0);
        when(patients.findById(1L)).thenReturn(Optional.of(new Patient("Paciente","12345678901",null,null,null,null,null,null)));
        Dentist dentist=mock(Dentist.class);
        when(dentist.getId()).thenReturn(1L);
        when(dentist.isActive()).thenReturn(true);
        when(dentists.findById(1L)).thenReturn(Optional.of(dentist));
        when(appointments.existsByDentistIdAndStartsAtLessThanAndEndsAtGreaterThan(1L,start.plusHours(1),start)).thenReturn(true);
        var request=new AppointmentRequest(1L,1L,start,start.plusHours(1),"Avaliação",null,null);
        assertThrows(IllegalArgumentException.class,()->service.create(request));
        verify(appointments,never()).save(any());
    }
}
