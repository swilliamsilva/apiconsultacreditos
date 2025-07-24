package com.apiconsultacreditos.repository;

import com.apiconsultacreditos.model.Credito;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CreditoRepository extends JpaRepository<Credito, Long> {

    // Mantido conforme necessidade de múltiplos créditos por NFSe
    List<Credito> findByNumeroNfse(String numeroNfse);
    
    // Alterado para tratar unicidade do número do crédito
    Optional<Credito> findByNumeroCredito(String numeroCredito);
}