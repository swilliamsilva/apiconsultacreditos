package com.apiconsultacreditos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

//ApiConsultaCreditosApplicationTests.java
@SpringBootTest(properties = {
 "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
})
@ActiveProfiles("test")
class ApiConsultaCreditosApplicationTests {
 @Test
 void contextLoads() {
     // Teste vazio apenas para verificar carga do contexto
 }
}