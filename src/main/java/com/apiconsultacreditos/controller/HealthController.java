HealthController.java

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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/actuator")
public class HealthController {
    
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", Instant.now().toString());
        return ResponseEntity.ok(status);
    }
    
    @GetMapping("/health/db")
    public ResponseEntity<Map<String, String>> healthDb() {
        // Verificação de conectividade com banco
    }
    
    @GetMapping("/health/kafka")
    public ResponseEntity<Map<String, String>> healthKafka() {
        // Verificação de conectividade com Kafka
    }
}