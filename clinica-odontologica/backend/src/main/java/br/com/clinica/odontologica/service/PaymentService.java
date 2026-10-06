package br.com.clinica.odontologica.service;

import br.com.clinica.odontologica.domain.Payment;
import br.com.clinica.odontologica.dto.PaymentRequest;
import br.com.clinica.odontologica.dto.PaymentResponse;
import br.com.clinica.odontologica.exception.ResourceNotFoundException;
import br.com.clinica.odontologica.repository.DentalProcedureRepository;
import br.com.clinica.odontologica.repository.PatientRepository;
import br.com.clinica.odontologica.repository.PaymentRepository;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @Transactional
public class PaymentService {
    private static final Set<String> METHODS=Set.of("PIX","DINHEIRO","CARTAO_CREDITO","CARTAO_DEBITO","BOLETO");
    private static final Set<String> STATUSES=Set.of("PENDENTE","PAGO","CANCELADO");
    private final PaymentRepository payments; private final PatientRepository patients; private final DentalProcedureRepository procedures;
    public PaymentService(PaymentRepository payments, PatientRepository patients, DentalProcedureRepository procedures) { this.payments=payments; this.patients=patients; this.procedures=procedures; }
    @Transactional(readOnly=true) public List<PaymentResponse> list() { return payments.findAll().stream().map(PaymentResponse::from).toList(); }
    public PaymentResponse create(PaymentRequest r) {
        if (!METHODS.contains(r.method())) throw new IllegalArgumentException("Forma de pagamento inválida.");
        if (!STATUSES.contains(r.status())) throw new IllegalArgumentException("Status de pagamento inválido.");
        var patient=patients.findById(r.patientId()).orElseThrow(() -> new ResourceNotFoundException("Paciente",r.patientId()));
        var procedure=r.procedureId()==null?null:procedures.findById(r.procedureId()).orElseThrow(() -> new ResourceNotFoundException("Procedimento",r.procedureId()));
        return PaymentResponse.from(payments.save(new Payment(patient,procedure,r.amount(),r.paidAt(),r.method(),r.status(),r.description())));
    }
}
