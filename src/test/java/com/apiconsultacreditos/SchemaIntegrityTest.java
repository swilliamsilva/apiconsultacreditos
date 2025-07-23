package com.apiconsultacreditos;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@SpringBootTest
class SchemaIntegrityTest {

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldValidateSchemaIntegrity() {
        assertDoesNotThrow(() -> {
            entityManager.createQuery("SELECT c FROM Credito c").getResultList();
        }, "Erro ao acessar a entidade Credito. Verifique se o schema.sql ou data.sql refletem corretamente o modelo.");
    }

    @Test
    void shouldDetectMissingFieldsInDataSQL() {
        assertDoesNotThrow(() -> {
            entityManager.createNativeQuery("SELECT identificador FROM credito").getResultList();
        }, "Campo 'identificador' não foi carregado corretamente. Verifique se está presente no arquivo data.sql");
    }

    @Test
    void shouldAllowInsertWithFullSchema() {
        assertDoesNotThrow(() -> {
            entityManager.createNativeQuery("INSERT INTO credito (numero_credito, numero_nfse, data_constituicao, valor_issqn, tipo_credito, simples_nacional, aliquota, valor_faturado, valor_deducao, base_calculo, identificador) VALUES ('test123', 'nfse123', CURRENT_DATE, 100.0, 'ISSQN', TRUE, 1.5, 1000.0, 100.0, 900.0, 'ident123')").executeUpdate();
        }, "Erro ao inserir crédito completo, verifique se o schema ou constraints estão corretos.");
    }
} 