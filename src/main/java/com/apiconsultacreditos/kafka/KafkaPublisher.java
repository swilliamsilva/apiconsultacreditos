package com.apiconsultacreditos.kafka;

import com.apiconsultacreditos.model.Credito;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Profile("!test")
public class KafkaPublisher {

    private static final Logger logger = LoggerFactory.getLogger(KafkaPublisher.class);

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.kafka.topic.name}")
    private String topic;

    public KafkaPublisher(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public static class KafkaPublishingException extends RuntimeException {
        public KafkaPublishingException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public void publishCredito(Credito credito) {
        if (credito == null) {
            throw new IllegalArgumentException("Credito não pode ser nulo");
        }

        try {
            String json = objectMapper.writeValueAsString(credito);
            kafkaTemplate.send(topic, json)
                .thenAccept(result -> logger.info("Mensagem publicada com sucesso no tópico {}", topic))
                .exceptionally(ex -> {
                    logger.error("Erro ao publicar no tópico {}", topic, ex);
                    return null;
                });
        } catch (JsonProcessingException e) {
            logger.error("Erro ao converter credito em JSON", e);
            throw new KafkaPublishingException("Erro ao converter credito em JSON", e);
        }
    }
}