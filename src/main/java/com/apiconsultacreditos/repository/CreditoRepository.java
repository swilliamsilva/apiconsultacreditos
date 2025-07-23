package com.apiconsultacreditos.repository;

import com.apiconsultacreditos.model.Credito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CreditoRepository extends JpaRepository<Credito, Long> {

    List<Credito> findByNumeroNfse(String numeroNfse);
    Optional<Credito> findByNumeroCredito(String numeroCredito);
 
}

