package com.apiconsultacreditos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;

@Testcontainers
@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class SchemaIntegrityTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("CreditoDB")
            .withUsername("postgres")
            .withPassword("suasenha");

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testValidaIntegridadedoSchemaSQL() {
        assertDoesNotThrow(() ->
                entityManager.createQuery("SELECT c FROM Credito c").getResultList()
        );
    }

    @Test
    void testDetectaMudancasNosCamposdoDataSQL() {
        assertDoesNotThrow(() -> {
            List<?> result = entityManager.createNativeQuery(
                    "SELECT column_name FROM information_schema.columns WHERE table_name = 'credito'")
                    .getResultList();
            assertFalse(result.isEmpty());
        });
    }

    @Test
    void testInsertWithFullSchema() {
        assertDoesNotThrow(() ->
                entityManager.createNativeQuery(
                        "INSERT INTO credito (numero_credito, numero_nfse, data_constituicao, valor_issqn, tipo_credito, simples_nacional, aliquota, valor_faturado, valor_deducao, base_calculo) " +
                                "VALUES ('test123', 'nfse123', CURRENT_DATE, 100.0, 'ISSQN', TRUE, 1.5, 1000.0, 100.0, 900.0)")
                        .executeUpdate()
        );
    }
}
