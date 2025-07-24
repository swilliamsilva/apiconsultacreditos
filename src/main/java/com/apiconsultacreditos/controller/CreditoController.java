// ==========================================
// Classe: CreditoController.java
// Projeto: api-consulta-creditos
// ==========================================
package com.apiconsultacreditos.controller;

import com.apiconsultacreditos.dto.CreditoResponse;
import com.apiconsultacreditos.kafka.ConsultaCreditoProducer;
import com.apiconsultacreditos.mapper.CreditoMapper;
import com.apiconsultacreditos.model.Credito;
import com.apiconsultacreditos.service.CreditoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creditos")
public class CreditoController {

    private final CreditoService creditoService;
    private final ConsultaCreditoProducer producer;
    private final CreditoMapper mapper;

    public CreditoController(CreditoService creditoService,
                             ConsultaCreditoProducer producer,
                             CreditoMapper mapper) {
        this.creditoService = creditoService;
        this.producer = producer;
        this.mapper = mapper;
    }

    @GetMapping("/nfse/{numeroNfse}")
    public ResponseEntity<List<CreditoResponse>> buscarPorNumeroNfse(@PathVariable String numeroNfse) {
        List<Credito> creditos = creditoService.consultarPorNfse(numeroNfse);

        creditos.forEach(producer::sendCredito);

        List<CreditoResponse> responses = creditos.stream()
                .map(mapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/numero/{numeroCredito}")
    public ResponseEntity<CreditoResponse> buscarPorNumeroCredito(@PathVariable String numeroCredito) {
        Credito credito = creditoService.consultarPorNumeroCredito(numeroCredito);

        if (credito == null) {
            return ResponseEntity.notFound().build();
        }

        producer.sendCredito(credito);
        return ResponseEntity.ok(mapper.toResponse(credito));
    }
}
