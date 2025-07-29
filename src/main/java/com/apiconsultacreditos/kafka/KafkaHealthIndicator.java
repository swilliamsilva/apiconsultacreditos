package com.apiconsultacreditos.kafka;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component("kafka")
public class KafkaHealthIndicator implements HealthIndicator {

    private final KafkaTemplate<String, String> kafkaTemplate;
    
    public KafkaHealthIndicator(
        @Qualifier("stringKafkaTemplate") // Especifica o bean correto
        KafkaTemplate<String, String> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public Health health() {
        try {
            kafkaTemplate.send("health-check-topic", "ping").get(10, TimeUnit.SECONDS);
            return Health.up().build();
        } catch (Exception e) {
            return Health.down()
                   .withDetail("error", e.getMessage())
                   .build();
        }
    }
}