package com.apiconsultacreditos.controller;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.DescribeClusterResult;
import org.apache.kafka.common.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@RestController
@RequestMapping("/actuator")
public class HealthController {
    
    private static final Logger logger = LoggerFactory.getLogger(HealthController.class);
    
    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;
    
    @Value("${kafka.health.timeout.seconds:5}")
    private int kafkaHealthTimeout;

    @Autowired
    private DataSource dataSource;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> status = new HashMap<>();
        status.put("status", "UP");
        status.put("timestamp", Instant.now().toString());
        status.put("service", "Consulta Créditos API");
        status.put("version", "1.0.0");
        return ResponseEntity.ok(status);
    }

    @GetMapping("/health/db")
    public ResponseEntity<Map<String, Object>> healthDb() {
        Map<String, Object> status = new HashMap<>();
        status.put("timestamp", Instant.now().toString());
        
        try (Connection connection = dataSource.getConnection()) {
            // Valida a conexão com um timeout de 2 segundos
            if (connection.isValid(2)) {
                DatabaseMetaData metaData = connection.getMetaData();
                
                status.put("status", "UP");
                status.put("database", metaData.getDatabaseProductName());
                status.put("version", metaData.getDatabaseProductVersion());
                status.put("driver", metaData.getDriverName());
                status.put("driverVersion", metaData.getDriverVersion());
            } else {
                status.put("status", "DOWN");
                status.put("error", "Conexão inválida");
                logger.error("Falha na verificação do banco de dados: conexão inválida");
            }
        } catch (SQLException e) {
            handleDbError(status, e);
        } catch (Exception e) {
            status.put("status", "DOWN");
            status.put("error", "Erro inesperado: " + e.getMessage());
            logger.error("Falha inesperada na verificação do banco de dados", e);
        }
        
        return ResponseEntity.ok(status);
    }

    private void handleDbError(Map<String, Object> status, SQLException e) {
        status.put("status", "DOWN");
        status.put("error", e.getMessage());
        status.put("sqlState", e.getSQLState());
        status.put("errorCode", e.getErrorCode());
        
        logger.error("Falha na verificação do banco de dados: {}", e.getMessage(), e);
    }

    @GetMapping("/health/kafka")
    public ResponseEntity<Map<String, Object>> healthKafka() {
        Map<String, Object> status = new HashMap<>();
        status.put("timestamp", Instant.now().toString());
        status.put("bootstrapServers", bootstrapServers);
        
        if (bootstrapServers == null || bootstrapServers.isEmpty()) {
            status.put("status", "DOWN");
            status.put("error", "Bootstrap servers não configurados");
            logger.error("Kafka health check falhou: bootstrap servers não configurados");
            return ResponseEntity.ok(status);
        }

        Properties props = new Properties();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(AdminClientConfig.REQUEST_TIMEOUT_MS_CONFIG, (int) TimeUnit.SECONDS.toMillis(kafkaHealthTimeout));
        
        try (AdminClient admin = AdminClient.create(props)) {
            DescribeClusterResult clusterResult = admin.describeCluster();
            
            // Verifica conexão obtendo informações dos nós
            Collection<Node> nodes = clusterResult.nodes().get(kafkaHealthTimeout, TimeUnit.SECONDS);
            String controllerId = clusterResult.controller().get(kafkaHealthTimeout, TimeUnit.SECONDS).idString();
            
            status.put("status", "UP");
            status.put("nodes", nodes.size());
            status.put("controllerId", controllerId);
            status.put("nodeIds", nodes.stream().map(Node::id).toList());
            
            logger.info("Health check Kafka: conectado com sucesso. Nodes: {}", nodes.size());
        } catch (TimeoutException e) {
            handleKafkaError(status, "Timeout ao conectar no Kafka (" + kafkaHealthTimeout + "s)", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restaura flag de interrupção
            handleKafkaError(status, "Verificação interrompida", e);
        } catch (ExecutionException e) {
            handleKafkaError(status, "Erro na execução: " + 
                (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()), e);
        } catch (Exception e) {
            handleKafkaError(status, "Erro inesperado: " + e.getMessage(), e);
        }
        
        return ResponseEntity.ok(status);
    }

    private void handleKafkaError(Map<String, Object> status, String message, Exception e) {
        status.put("status", "DOWN");
        status.put("error", message);
        logger.error("Falha no health check do Kafka: {}", message, e);
    }
}