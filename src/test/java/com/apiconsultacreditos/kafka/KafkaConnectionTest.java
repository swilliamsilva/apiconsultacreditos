package com.apiconsultacreditos.kafka;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.concurrent.TimeUnit;

@Component
public class KafkaConnectionTest {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaConnectionTest(
        @Qualifier("stringKafkaTemplate") // Especifica o bean correto
        KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void testKafkaConnection() {
        try {
            System.out.println("=== TESTANDO CONEXÃO COM KAFKA ===");
            kafkaTemplate.send("test-connection-topic", "ping").get(10, TimeUnit.SECONDS);
            System.out.println("=== CONEXÃO COM KAFKA OK ===");
        } catch (Exception e) {
            System.err.println("=== ERRO NA CONEXÃO COM KAFKA ===");
            e.printStackTrace();
        }
    }
}