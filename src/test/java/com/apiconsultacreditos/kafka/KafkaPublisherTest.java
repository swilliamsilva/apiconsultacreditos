package com.apiconsultacreditos.kafka;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import com.apiconsultacreditos.model.Credito;

@ExtendWith(MockitoExtension.class)
class KafkaPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @InjectMocks
    private KafkaPublisher kafkaPublisher;

    @Test
    void testPublishCredito() {
        // Configura o tópico
        ReflectionTestUtils.setField(kafkaPublisher, "topic", "consulta-creditos-topic");
        
        // Cria o objeto Credito usando construtor padrão e setters
        Credito credito = new Credito();
        credito.setNumeroCredito("123456");
        credito.setTipoCredito("ISSQN");
        credito.setValorIssqn(new BigDecimal("1500.75"));

        assertDoesNotThrow(() -> kafkaPublisher.publishCredito(credito));
        verify(kafkaTemplate, times(1)).send(eq("consulta-creditos-topic"), anyString());
    }
}