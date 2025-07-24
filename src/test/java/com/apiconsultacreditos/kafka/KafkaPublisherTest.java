package com.apiconsultacreditos.kafka;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.concurrent.CompletableFuture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.concurrent.SettableListenableFuture;

import com.apiconsultacreditos.model.Credito;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("kafka-test")
class KafkaPublisherTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private KafkaPublisher kafkaPublisher;

    @Test
    void testPublishCredito() throws JsonProcessingException {
        ReflectionTestUtils.setField(kafkaPublisher, "topic", "consulta-creditos-topic");
        
        Credito credito = new Credito();
        credito.setNumeroCredito("123456");
        credito.setTipoCredito("ISSQN");
        credito.setValorIssqn(new BigDecimal("1500.75"));

        // Configurar comportamento do mock
        when(objectMapper.writeValueAsString(credito)).thenReturn("{}");
        
        // Criar um CompletableFuture válido
        CompletableFuture<SendResult<String, String>> future = new CompletableFuture<>();
        future.complete(new SendResult<>(null, null));
        
        when(kafkaTemplate.send(eq("consulta-creditos-topic"), anyString()))
             .thenReturn(future);
        
        assertDoesNotThrow(() -> kafkaPublisher.publishCredito(credito));
        verify(kafkaTemplate, times(1)).send(eq("consulta-creditos-topic"), anyString());
    }
}