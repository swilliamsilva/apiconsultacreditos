package com.apiconsultacreditos;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ApiConsultaCreditosApplicationTests {

    @Configuration
    static class TestConfig {
        @Bean
        @Primary
        public KafkaTemplate<?, ?> kafkaTemplate() {
            return Mockito.mock(KafkaTemplate.class);
        }
    }

    @Test
    void contextLoads() {
        // Teste vazio apenas para carregar contexto
    }
}