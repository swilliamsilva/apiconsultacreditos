package com.apiconsultacreditos.service;

import com.apiconsultacreditos.model.Credito;
import com.apiconsultacreditos.repository.CreditoRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CreditoService {
    private final CreditoRepository repository;

    public CreditoService(CreditoRepository repository) {
        this.repository = repository;
    }

    public List<Credito> consultarPorNfse(String numeroNfse) {
        return repository.findByNumeroNfse(numeroNfse);
    }

    public Credito consultarPorNumeroCredito(String numeroCredito) {
        return repository.findByNumeroCredito(numeroCredito)
                .orElse(null); // ou .orElseThrow() se preferir lançar exceção
    }
}
