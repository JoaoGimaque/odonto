package br.com.clinica.odontologica.repository;

import br.com.clinica.odontologica.domain.ClinicalRecord;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClinicalRecordRepository extends JpaRepository<ClinicalRecord, Long> {
    Optional<ClinicalRecord> findByPatientId(Long patientId);
}
