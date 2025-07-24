package com.apiconsultacreditos.kafka;

import com.apiconsultacreditos.model.Credito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ConsultaCreditoProducer {

    private final KafkaTemplate<String, Credito> kafkaTemplate;

    @Value("${spring.kafka.topic.name}")
    private String topicName;

    public ConsultaCreditoProducer(KafkaTemplate<String, Credito> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void sendCredito(Credito credito) {
        kafkaTemplate.send(topicName, credito);
    }

    public void publicarConsulta(String numeroCredito) {
        Credito credito = new Credito();
        credito.setNumeroCredito(numeroCredito);
        sendCredito(credito);
    }
}