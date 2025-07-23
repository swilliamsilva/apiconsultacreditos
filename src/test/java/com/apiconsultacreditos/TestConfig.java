package com.apiconsultacreditos;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import com.apiconsultacreditos.model.Credito;

import static org.mockito.Mockito.mock;


@TestConfiguration
public class TestConfig {
    
    @Bean
    @SuppressWarnings("unchecked")
    public KafkaTemplate<String, Credito> kafkaTemplate() {
        ProducerFactory<String, Credito> mockProducerFactory = mock(ProducerFactory.class);
        return new KafkaTemplate<>(mockProducerFactory);
    }
}