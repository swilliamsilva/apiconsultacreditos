package com.apiconsultacreditos.kafka;

import com.apiconsultacreditos.event.ConsultaCreditoEvent;
import com.apiconsultacreditos.model.Credito;

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

    public void publicarConsulta(Credito credito) {
        if(credito == null || credito.getNumeroCredito() == null || credito.getNumeroCredito().isBlank()) {
            logger.warn("Número de crédito inválido: {}", credito);
            return;
        }
        
        ConsultaCreditoEvent event = new ConsultaCreditoEvent.Builder()
            .numeroCredito(credito.getNumeroCredito())
            .numeroNfse(credito.getNumeroNfse())
            .dataConstituicao(credito.getDataConstituicao())
            .valorIssqn(credito.getValorIssqn())
            .tipoCredito(credito.getTipoCredito())
            .simplesNacional(credito.isSimplesNacional())
            .aliquota(credito.getAliquota())
            .valorFaturado(credito.getValorFaturado())
            .valorDeducao(credito.getValorDeducao())
            .baseCalculo(credito.getBaseCalculo())
            .build();
            
        sendEvent(event);
    }
}