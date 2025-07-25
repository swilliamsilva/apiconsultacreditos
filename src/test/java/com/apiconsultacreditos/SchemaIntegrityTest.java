package com.apiconsultacreditos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
@Transactional
@TestPropertySource(properties = {
    "spring.jpa.hibernate.ddl-auto=validate", // Alterado para validar após criação
    "spring.jpa.defer-datasource-initialization=true", // Garante execução do script antes da validação
    "spring.sql.init.mode=always" // Garante inicialização do schema
})
class SchemaIntegrityTest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15-alpine")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withInitScript("init.sql"); // Script de inicialização

    @DynamicPropertySource
    static void props(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");
        registry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        
        // Configurações adicionais para garantir comportamento correto
        registry.add("spring.jpa.properties.hibernate.default_schema", () -> "public");
        registry.add("spring.jpa.show-sql", () -> "true");
    }

    @Autowired
    private DataSource dataSource;
    
    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void testSchemaIntegrity() throws Exception {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement()) {
            
            // Verifica se a tabela 'credito' existe
            ResultSet rs = statement.executeQuery(
                "SELECT EXISTS (SELECT 1 FROM information_schema.tables WHERE table_name = 'credito')"
            );
            
            assertTrue(rs.next());
            assertTrue(rs.getBoolean(1), "Tabela 'credito' não encontrada");
        }
    }

    @Test
    void testValidaIntegridadedoSchemaSQL() {
        // Verifica se a entidade Credito pode ser consultada via JPA
        assertDoesNotThrow(() -> 
            entityManager.createQuery("SELECT c FROM Credito c", Object.class).getResultList(),
            "Falha na consulta JPA da entidade Credito"
        );
    }

    @Test
    void testDetectaMudancasNosCamposdoDataSQL() {
        // Verifica se colunas essenciais existem
        List<?> columns = entityManager.createNativeQuery(
            "SELECT column_name " +
            "FROM information_schema.columns " +
            "WHERE table_name = 'credito'")
            .getResultList();
        
        assertFalse(columns.isEmpty(), "Nenhuma coluna encontrada na tabela 'credito'");
        assertTrue(columns.contains("numero_credito"), "Coluna 'numero_credito' não encontrada");
        assertTrue(columns.contains("valor_issqn"), "Coluna 'valor_issqn' não encontrada");
    }

    @Test
    void testInsertWithFullSchema() {
        // Testa inserção completa
        assertDoesNotThrow(() -> {
            int count = entityManager.createNativeQuery(
                    "INSERT INTO credito (numero_credito, numero_nfse, data_constituicao, valor_issqn, tipo_credito, simples_nacional, aliquota, valor_faturado, valor_deducao, base_calculo) " +
                    "VALUES ('test123', 'nfse123', CURRENT_DATE, 100.0, 'ISSQN', TRUE, 1.5, 1000.0, 100.0, 900.0)")
                    .executeUpdate();
            
            assertEquals(1, count, "Inserção falhou ou afetou número incorreto de linhas");
        }, "Erro durante inserção de dados");
        
        // Limpeza
        entityManager.createNativeQuery("DELETE FROM credito WHERE numero_credito = 'test123'")
                     .executeUpdate();
    }
}