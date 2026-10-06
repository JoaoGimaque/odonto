package br.com.clinica.odontologica.repository;

import br.com.clinica.odontologica.domain.Patient;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientRepository extends JpaRepository<Patient, Long> {
    Page<Patient> findByActiveTrueAndNameContainingIgnoreCaseOrActiveTrueAndCpfContaining(String name, String cpf, Pageable pageable);
    boolean existsByCpf(String cpf);
}
