package br.com.clinica.odontologica.service;

import br.com.clinica.odontologica.domain.Quote;
import br.com.clinica.odontologica.domain.QuoteItem;
import br.com.clinica.odontologica.dto.QuoteRequest;
import br.com.clinica.odontologica.dto.QuoteResponse;
import br.com.clinica.odontologica.exception.ResourceNotFoundException;
import br.com.clinica.odontologica.repository.DentalProcedureRepository;
import br.com.clinica.odontologica.repository.PatientRepository;
import br.com.clinica.odontologica.repository.QuoteRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class QuoteService {
    private static final Set<String> STATUSES=Set.of("PENDENTE","APROVADO","RECUSADO","CONCLUIDO");
    private final QuoteRepository quotes; private final PatientRepository patients; private final DentalProcedureRepository procedures;
    public QuoteService(QuoteRepository quotes,PatientRepository patients,DentalProcedureRepository procedures){this.quotes=quotes;this.patients=patients;this.procedures=procedures;}
    @Transactional(readOnly=true) public List<QuoteResponse> list(){return quotes.findAll().stream().map(QuoteResponse::from).toList();}
    public QuoteResponse create(QuoteRequest request){
        String status=request.status()==null?"PENDENTE":request.status(); if(!STATUSES.contains(status)) throw new IllegalArgumentException("Status de orçamento inválido.");
        if(request.discount().signum()<0) throw new IllegalArgumentException("Desconto não pode ser negativo.");
        var patient=patients.findById(request.patientId()).orElseThrow(()->new ResourceNotFoundException("Paciente",request.patientId()));
        var quote=new Quote(patient,request.discount(),BigDecimal.ZERO,status,request.notes()); BigDecimal subtotal=BigDecimal.ZERO;
        for(var item:request.items()){
            var procedure=procedures.findById(item.procedureId()).orElseThrow(()->new ResourceNotFoundException("Procedimento",item.procedureId()));
            if(!procedure.isActive()) throw new IllegalArgumentException("Não é possível orçar um procedimento inativo.");
            quote.addItem(new QuoteItem(procedure,item.quantity(),procedure.getPrice()));
            subtotal=subtotal.add(procedure.getPrice().multiply(BigDecimal.valueOf(item.quantity())));
        }
        BigDecimal total=subtotal.subtract(request.discount()); if(total.signum()<0) throw new IllegalArgumentException("Desconto maior que o subtotal.");
        quote.calculateTotal(subtotal);
        return QuoteResponse.from(quotes.save(quote));
    }
    public QuoteResponse updateStatus(Long id,String status){
        if(!STATUSES.contains(status)) throw new IllegalArgumentException("Status de orçamento inválido.");
        var quote=quotes.findById(id).orElseThrow(()->new ResourceNotFoundException("Orçamento",id));
        quote.changeStatus(status);
        return QuoteResponse.from(quote);
    }
}
