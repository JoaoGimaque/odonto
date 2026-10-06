package br.com.clinica.odontologica.repository;

import br.com.clinica.odontologica.domain.Quote;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuoteRepository extends JpaRepository<Quote, Long> {}
