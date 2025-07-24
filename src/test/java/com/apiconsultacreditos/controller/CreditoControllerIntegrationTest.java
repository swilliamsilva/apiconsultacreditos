package com.apiconsultacreditos.controller;

import com.apiconsultacreditos.model.Credito;
import com.apiconsultacreditos.repository.CreditoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration",
        "spring.jpa.hibernate.ddl-auto=create-drop"
    }
)
@Testcontainers
public class CreditoControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    @DynamicPropertySource
    static void registerPgProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", postgres::getDriverClassName);
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CreditoRepository creditoRepository;

    @MockBean
    private KafkaTemplate<String, String> stringKafkaTemplate;
    
    @MockBean
    private KafkaTemplate<String, Credito> creditoKafkaTemplate;

    @BeforeEach
    void setup() {
        // Limpar qualquer dado existente
        creditoRepository.deleteAll();
        
        // Criar e persistir entidade
        Credito credito = new Credito();
        credito.setNumeroCredito("123456");
        credito.setNumeroNfse("7891011");
        credito.setDataConstituicao(LocalDate.now());
        credito.setValorIssqn(new BigDecimal("1500.00"));
        credito.setTipoCredito("ISSQN");
        credito.setSimplesNacional(true);
        credito.setAliquota(new BigDecimal("5.0"));
        credito.setValorFaturado(new BigDecimal("30000.00"));
        credito.setValorDeducao(new BigDecimal("5000.00"));
        credito.setBaseCalculo(new BigDecimal("25000.00"));
        
        creditoRepository.save(credito);
        creditoRepository.flush(); // Forçar sincronização com o banco
    }

    @Test
    void testBuscarPorNumero() {
        // Verificar dados no banco antes da requisição
        System.out.println("Dados no banco: " + creditoRepository.findAll());
        
        ResponseEntity<String> res = restTemplate.getForEntity(
            "/api/creditos/numero/123456", 
            String.class
        );
        
        // Debug detalhado
        System.out.println("Status: " + res.getStatusCode());
        System.out.println("Corpo: " + res.getBody());
        System.out.println("Headers: " + res.getHeaders());
        
        assertThat(res.getStatusCode()).isEqualTo(OK);
        assertThat(res.getBody()).contains("123456");
    }

    @Test
    void testBuscarPorNfse() {
        // Verificar dados no banco antes da requisição
        System.out.println("Dados no banco: " + creditoRepository.findAll());
        
        ResponseEntity<String> res = restTemplate.getForEntity(
            "/api/creditos/nfse/7891011", 
            String.class
        );
        
        // Debug detalhado
        System.out.println("Status: " + res.getStatusCode());
        System.out.println("Corpo: " + res.getBody());
        System.out.println("Headers: " + res.getHeaders());
        
        assertThat(res.getStatusCode()).isEqualTo(OK);
        assertThat(res.getBody()).contains("7891011");
    }
}