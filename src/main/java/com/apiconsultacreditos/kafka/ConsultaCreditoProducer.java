package com.apiconsultacreditos.kafka;

import com.apiconsultacreditos.event.ConsultaCreditoEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class ConsultaCreditoProducer {

    private static final Logger logger = LoggerFactory.getLogger(ConsultaCreditoProducer.class);
    
    private final KafkaTemplate<String, ConsultaCreditoEvent> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    public ConsultaCreditoProducer(KafkaTemplate<String, ConsultaCreditoEvent> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendEvent(ConsultaCreditoEvent event) {
        if(event == null || event.getNumeroCredito() == null) {
            logger.error("Tentativa de enviar evento inválido: {}", event);
            return;
        }
        
        CompletableFuture<SendResult<String, ConsultaCreditoEvent>> future = 
            kafkaTemplate.send(topicName, event);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                logger.info("Evento enviado com sucesso: {} | Offset: {}",
                    event.getNumeroCredito(), 
                    result.getRecordMetadata().offset());
            } else {
                logger.error("Falha ao enviar evento: {}", event.getNumeroCredito(), ex);
            }
        });
    }

    public void publicarConsulta(String numeroCredito) {
        if(numeroCredito == null || numeroCredito.isBlank()) {
            logger.warn("Número de crédito inválido: {}", numeroCredito);
            return;
        }
        
        ConsultaCreditoEvent event = new ConsultaCreditoEvent.Builder()
            .numeroCredito(numeroCredito)
            // Adicione outros campos necessários aqui
            .build();
            
        sendEvent(event);
    }
}