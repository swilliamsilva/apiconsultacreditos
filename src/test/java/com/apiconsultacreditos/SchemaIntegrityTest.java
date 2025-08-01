package com.apiconsultacreditos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
@Transactional

@TestPropertySource(properties = {
	    "spring.jpa.hibernate.ddl-auto=validate",
	    "spring.sql.init.mode=never"
	})

class SchemaIntegrityTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
        .withDatabaseName("testdb")
        .withUsername("test")
        .withPassword("test")
        .withClasspathResourceMapping(
            "schema.sql", // Nome do arquivo no classpath
            "/docker-entrypoint-initdb.d/init.sql", // Caminho no container
            BindMode.READ_ONLY
        );

    @DynamicPropertySource
    static void registerPgProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
    }

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testValidaIntegridadedoSchemaSQL() {
        assertDoesNotThrow(() ->
            entityManager.createQuery("SELECT c FROM Credito c").getResultList(),
            "Consulta JPA falhou - esquema incompatível"
        );
    }

    @Test
    void testDetectaMudancasNosCamposdoDataSQL() {
        List<?> columns = entityManager.createNativeQuery(
            "SELECT column_name FROM information_schema.columns " +
            "WHERE table_name = 'credito'")
            .getResultList();
        
        assertFalse(columns.isEmpty(), "Nenhuma coluna encontrada na tabela credito");
        assertTrue(columns.contains("numero_credito"), "Coluna numero_credito ausente");
        assertTrue(columns.contains("valor_issqn"), "Coluna valor_issqn ausente");
    }

    @Test
    void testInsertWithFullSchema() {
        int count = entityManager.createNativeQuery(
            "INSERT INTO credito (numero_credito, numero_nfse, data_constituicao, " +
            "valor_issqn, tipo_credito, simples_nacional, aliquota, valor_faturado, " +
            "valor_deducao, base_calculo) " +
            "VALUES ('test123', 'nfse123', CURRENT_DATE, 100.0, 'ISSQN', TRUE, 1.5, " +
            "1000.0, 100.0, 900.0)")
            .executeUpdate();
        
        assertEquals(1, count, "Número incorreto de linhas inseridas");
        
        // Limpeza
        entityManager.createNativeQuery("DELETE FROM credito WHERE numero_credito = 'test123'")
                    .executeUpdate();
    }
}