package com.apiconsultacreditos.controller;

import com.apiconsultacreditos.dto.CreditoResponse;
import com.apiconsultacreditos.event.ConsultaCreditoEvent;
import com.apiconsultacreditos.kafka.ConsultaCreditoProducer;
import com.apiconsultacreditos.mapper.CreditoMapper;
import com.apiconsultacreditos.mapper.EventoMapper;
import com.apiconsultacreditos.model.Credito;
import com.apiconsultacreditos.service.CreditoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/creditos")
public class CreditoController {

    private static final Logger logger = LoggerFactory.getLogger(CreditoController.class);

    private final CreditoService creditoService;
    private final ConsultaCreditoProducer producer;
    private final CreditoMapper creditoMapper;
    private final EventoMapper eventoMapper;

    public CreditoController(CreditoService creditoService,
                             ConsultaCreditoProducer producer,
                             CreditoMapper creditoMapper,
                             EventoMapper eventoMapper) {
        this.creditoService = creditoService;
        this.producer = producer;
        this.creditoMapper = creditoMapper;
        this.eventoMapper = eventoMapper;
    }

    @GetMapping
    public ResponseEntity<List<CreditoResponse>> listarTodos() {
        List<Credito> creditos = creditoService.listarTodos();
        enviarParaKafkaAsync(creditos);
        List<CreditoResponse> responses = creditos.stream()
                .map(creditoMapper::toResponse)
                .toList();
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/nfse/{numeroNfse}")
    public ResponseEntity<List<CreditoResponse>> buscarPorNumeroNfse(@PathVariable String numeroNfse) {
        List<Credito> creditos = creditoService.consultarPorNfse(numeroNfse);

        if (creditos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        enviarParaKafkaAsync(creditos);
        List<CreditoResponse> responses = creditos.stream()
                .map(creditoMapper::toResponse)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/numero/{numeroCredito}")
    public ResponseEntity<CreditoResponse> buscarPorNumeroCredito(@PathVariable String numeroCredito) {
        Optional<Credito> creditoOpt = creditoService.consultarPorNumeroCredito(numeroCredito);
        if (creditoOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        Credito credito = creditoOpt.get();
        enviarParaKafkaAsync(credito);
        return ResponseEntity.ok(creditoMapper.toResponse(credito));
    }

    private void enviarParaKafkaAsync(List<Credito> creditos) {
        creditos.forEach(this::enviarParaKafkaAsync);
    }

    private void enviarParaKafkaAsync(Credito credito) {
        CompletableFuture.runAsync(() -> {
            try {
                ConsultaCreditoEvent evento = eventoMapper.toEvent(credito);
                producer.sendEvent(evento);
                logger.info("Evento enviado para Kafka: {}", evento.getNumeroCredito());
            } catch (Exception e) {
                logger.error("Erro ao enviar crédito para Kafka: {}", credito.getNumeroCredito(), e);
            }
        });
    }
}