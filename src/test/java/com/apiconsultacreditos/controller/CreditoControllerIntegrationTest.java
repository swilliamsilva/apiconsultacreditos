package com.apiconsultacreditos.controller;

import com.apiconsultacreditos.config.TestDatabaseManager;
import com.apiconsultacreditos.config.TestKafkaConfig;
import com.apiconsultacreditos.model.Credito;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@Import(TestKafkaConfig.class)
class CreditoControllerIntegrationTest {

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private TestDatabaseManager db;
    
    // Mock para o KafkaTemplate
    @MockBean
    private KafkaTemplate<String, Credito> kafkaTemplate;

   
    @BeforeEach
    void setup() {
        db.limparBanco();
        restTemplate.postForEntity(
            "/api/creditos/test-seed",
            Credito.builder()
                       
                .numeroCredito("123456")
                .numeroNfse("7891011")
                .dataConstituicao(LocalDate.now())
                .valorIssqn(new BigDecimal("1500.00"))
                .tipoCredito("ISSQN")
                .simplesNacional(true)
                .aliquota(new BigDecimal("5.0"))
                .valorFaturado(new BigDecimal("30000.00"))
                .valorDeducao(new BigDecimal("5000.00"))
                .baseCalculo(new BigDecimal("25000.00"))
                .build(),
            Void.class
        );
    }

    @Test
    void testBuscarPorNumero() {
        ResponseEntity<String> res = restTemplate.getForEntity("/api/creditos/numero/123456", String.class);
        assertThat(res.getStatusCode()).isEqualTo(OK);
        assertThat(res.getBody()).contains("123456");
    }

    @Test
    void testBuscarPorNfse() {
        ResponseEntity<String> res = restTemplate.getForEntity("/api/creditos/nfse/7891011", String.class);
        assertThat(res.getStatusCode()).isEqualTo(OK);
        assertThat(res.getBody()).contains("7891011");
    }
}
