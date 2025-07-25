package com.apiconsultacreditos.controller;

import com.apiconsultacreditos.model.Credito;
import com.apiconsultacreditos.repository.CreditoRepository;
import com.apiconsultacreditos.kafka.ConsultaCreditoProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.support.TestPropertySourceUtils;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Testcontainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = CreditoControllerIntegrationTest.DataSourceInitializer.class)
class CreditoControllerIntegrationTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");

    // Inicializador para forçar o uso do driver PostgreSQL
    static class DataSourceInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            TestPropertySourceUtils.addInlinedPropertiesToEnvironment(
                applicationContext,
                "spring.datasource.driver-class-name=org.postgresql.Driver",
                "spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect",
                "spring.sql.init.mode=never",
                "spring.jpa.hibernate.ddl-auto=update",
                "spring.jpa.defer-datasource-initialization=false",
                "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
            );
        }
    }

    @DynamicPropertySource
    static void registerPgProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CreditoRepository creditoRepository;

    @MockBean
    private ConsultaCreditoProducer consultaCreditoProducer;

    @BeforeEach
    void setup() {
        // Configura o mock para não fazer nada quando enviar mensagens
        doNothing().when(consultaCreditoProducer).publicarConsulta(any(Credito.class));
        
        creditoRepository.deleteAll();
        
        // Criar créditos de teste
        Credito credito1 = new Credito();
        credito1.setNumeroCredito("CRD-001");
        credito1.setNumeroNfse("NFSE-001");
        credito1.setDataConstituicao(LocalDate.now());
        credito1.setValorIssqn(new BigDecimal("1500.00"));
        credito1.setTipoCredito("ISSQN");
        credito1.setSimplesNacional(true);
        credito1.setAliquota(new BigDecimal("5.0"));
        credito1.setValorFaturado(new BigDecimal("30000.00"));
        credito1.setValorDeducao(new BigDecimal("5000.00"));
        credito1.setBaseCalculo(new BigDecimal("25000.00"));
        
        Credito credito2 = new Credito();
        credito2.setNumeroCredito("CRD-002");
        credito2.setNumeroNfse("NFSE-001");
        credito2.setDataConstituicao(LocalDate.now());
        credito2.setValorIssqn(new BigDecimal("1500.00"));
        credito2.setTipoCredito("ISSQN");
        credito2.setSimplesNacional(true);
        credito2.setAliquota(new BigDecimal("5.0"));
        credito2.setValorFaturado(new BigDecimal("30000.00"));
        credito2.setValorDeducao(new BigDecimal("5000.00"));
        credito2.setBaseCalculo(new BigDecimal("25000.00"));
        
        creditoRepository.saveAll(List.of(credito1, credito2));
    }

    @Test
    void buscarPorNumeroCredito_DeveRetornarCredito() {
        var response = restTemplate.getForEntity(
            "/api/creditos/numero/CRD-001", 
            Credito.class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getNumeroCredito()).isEqualTo("CRD-001");
    }

    @Test
    void buscarPorNumeroNfse_DeveRetornarCreditos() {
        var response = restTemplate.getForEntity(
            "/api/creditos/nfse/NFSE-001", 
            Credito[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()[0].getNumeroNfse()).isEqualTo("NFSE-001");
        assertThat(response.getBody()[1].getNumeroNfse()).isEqualTo("NFSE-001");
    }

    @Test
    void listarTodos_DeveRetornarTodosCreditos() {
        var response = restTemplate.getForEntity(
            "/api/creditos", 
            Credito[].class
        );
        
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }
}