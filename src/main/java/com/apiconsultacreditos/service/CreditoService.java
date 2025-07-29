package com.apiconsultacreditos.service;

import com.apiconsultacreditos.model.Credito;
import com.apiconsultacreditos.repository.CreditoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CreditoService {
    private final CreditoRepository repository;

    public CreditoService(CreditoRepository repository) {
        this.repository = repository;
    }

    public List<Credito> listarTodos() {
        return repository.findAll();
    }

    public List<Credito> consultarPorNfse(String numeroNfse) {
        return repository.findByNumeroNfse(numeroNfse);
    }

    public Optional<Credito> consultarPorNumeroCredito(String numeroCredito) {
        return repository.findByNumeroCredito(numeroCredito);
    }
}