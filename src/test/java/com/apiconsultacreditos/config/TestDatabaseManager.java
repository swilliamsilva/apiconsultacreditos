package com.apiconsultacreditos.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@Profile("!kafka-test") // Só carrega quando NÃO for perfil kafka-test
public class TestDatabaseManager {

    @Autowired(required = false) // Torna a dependência opcional
    private JdbcTemplate jdbc;

    public void limparBanco() {
        if (jdbc != null) {
            jdbc.execute("DELETE FROM credito");
        }
    }
}