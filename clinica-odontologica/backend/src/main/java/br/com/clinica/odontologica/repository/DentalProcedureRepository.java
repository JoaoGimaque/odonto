package br.com.clinica.odontologica.repository;

import br.com.clinica.odontologica.domain.DentalProcedure;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DentalProcedureRepository extends JpaRepository<DentalProcedure, Long> {}
