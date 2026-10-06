package br.com.clinica.odontologica.repository;

import br.com.clinica.odontologica.domain.Dentist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentistRepository extends JpaRepository<Dentist, Long> {
    Page<Dentist> findByNameContainingIgnoreCaseOrCroContainingIgnoreCase(String name, String cro, Pageable pageable);
}
