package br.com.clinica.odontologica.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import br.com.clinica.odontologica.domain.DentalProcedure;
import br.com.clinica.odontologica.domain.Patient;
import br.com.clinica.odontologica.dto.QuoteRequest;
import br.com.clinica.odontologica.repository.DentalProcedureRepository;
import br.com.clinica.odontologica.repository.PatientRepository;
import br.com.clinica.odontologica.repository.QuoteRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class QuoteServiceTest {
    private QuoteRepository quotes;
    private PatientRepository patients;
    private DentalProcedureRepository procedures;
    private QuoteService service;

    @BeforeEach void setUp() {
        quotes=mock(QuoteRepository.class); patients=mock(PatientRepository.class); procedures=mock(DentalProcedureRepository.class);
        service=new QuoteService(quotes,patients,procedures);
    }

    @Test void rejectsDiscountAboveSubtotalBeforePersisting() {
        when(patients.findById(1L)).thenReturn(Optional.of(new Patient("Paciente","12345678901",null,null,null,null,null,null)));
        when(procedures.findById(1L)).thenReturn(Optional.of(new DentalProcedure("Limpeza",null,new BigDecimal("100.00"),30)));
        var request=new QuoteRequest(1L,List.of(new QuoteRequest.Item(1L,1)),new BigDecimal("100.01"),null,null);
        assertThrows(IllegalArgumentException.class,()->service.create(request));
        verify(quotes,never()).save(any());
    }

    @Test void calculatesAndPersistsDiscountedTotal() {
        when(patients.findById(1L)).thenReturn(Optional.of(new Patient("Paciente","12345678901",null,null,null,null,null,null)));
        when(procedures.findById(1L)).thenReturn(Optional.of(new DentalProcedure("Limpeza",null,new BigDecimal("100.00"),30)));
        when(quotes.save(any())).thenAnswer(invocation->invocation.getArgument(0));
        var request=new QuoteRequest(1L,List.of(new QuoteRequest.Item(1L,2)),new BigDecimal("25.00"),null,null);
        var response=service.create(request);
        assertEquals(new BigDecimal("175.00"),response.total());
    }
}
