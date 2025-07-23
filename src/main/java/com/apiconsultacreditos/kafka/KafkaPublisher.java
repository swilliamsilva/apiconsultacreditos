package com.apiconsultacreditos.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.apiconsultacreditos.model.Credito;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class KafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;
    
    @Value("${spring.kafka.topic.name}")
    private String topic;

    public KafkaPublisher(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = new ObjectMapper();
    }

    // Exceção personalizada para erros de publicação
    public static class KafkaPublishingException extends RuntimeException {
        public KafkaPublishingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public void publishCredito(Credito credito) {
        try {
            String json = objectMapper.writeValueAsString(credito);
            kafkaTemplate.send(topic, json);
        } catch (JsonProcessingException e) {
            // Lança exceção dedicada com a causa original
            throw new KafkaPublishingException("Erro ao converter credito em JSON", e);
        }
    }
}