package com.apiconsultacreditos;

import com.apiconsultacreditos.model.Credito;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(properties = {
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
})
@ActiveProfiles("test")
class ApiConsultaCreditosApplicationTests {

    // Adiciona um mock do KafkaTemplate para resolver a dependência
    @MockBean
    private KafkaTemplate<String, Credito> kafkaTemplate;

    @Test
    void contextLoads() {
        // Teste vazio apenas para verificar carga do contexto
    }
}