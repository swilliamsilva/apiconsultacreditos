// ============================
// CreditoController.java (Atualizado)
// ============================
package com.apiconsultacreditos.controller;

import com.apiconsultacreditos.dto.CreditoDTO;
import com.apiconsultacreditos.kafka.ConsultaCreditoProducer;
import com.apiconsultacreditos.mapper.CreditoMapper;
import com.apiconsultacreditos.model.Credito;
import com.apiconsultacreditos.service.CreditoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/creditos")
public class CreditoController {

    private final CreditoService creditoService;
    private final ConsultaCreditoProducer producer;
    private final CreditoMapper mapper;

    public CreditoController(CreditoService creditoService, ConsultaCreditoProducer producer, CreditoMapper mapper) {
        this.creditoService = creditoService;
        this.producer = producer;
        this.mapper = mapper;
    }

    @GetMapping("/nfse/{numeroNfse}")
    public List<CreditoDTO> getCreditoPorNfse(@PathVariable String numeroNfse) {
        List<Credito> creditos = creditoService.consultarPorNfse(numeroNfse);
        creditos.forEach(producer::sendCredito);
        return mapper.toDtoList(creditos);
    }

    @GetMapping("/numero/{numeroCredito}")
    public CreditoDTO getCreditoPorNumeroCredito(@PathVariable String numeroCredito) {
        Credito credito = creditoService.consultarPorNumeroCredito(numeroCredito);
        if (credito != null) producer.sendCredito(credito);
        return mapper.toDto(credito);
    }
}
