package br.com.clinica.odontologica.repository;

import br.com.clinica.odontologica.domain.OdontogramEntry;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OdontogramEntryRepository extends JpaRepository<OdontogramEntry, Long> {
    List<OdontogramEntry> findByPatientIdOrderByToothNumber(Long patientId);
    Optional<OdontogramEntry> findByPatientIdAndToothNumber(Long patientId, String toothNumber);
}
