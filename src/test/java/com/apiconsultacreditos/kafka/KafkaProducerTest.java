package com.apiconsultacreditos.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import com.apiconsultacreditos.config.TestKafkaConfig;

@SpringBootTest(classes = {
	    ConsultaCreditoProducer.class, // Apenas a classe que está sendo testada
	    TestKafkaConfig.class           // Configuração de teste do Kafka
	})
	@ActiveProfiles("kafka-test")
	@EmbeddedKafka(
	    topics = "consulta-creditos-topic",
	    brokerProperties = {
	        "listeners=PLAINTEXT://localhost:0",
	        "port=0"
	    }
	)
	class KafkaProducerTest {

	    @Autowired
	    private ConsultaCreditoProducer producer;

	    @Autowired
	    private EmbeddedKafkaBroker embeddedKafkaBroker;

	    private KafkaMessageListenerContainer<String, String> container;
	    private BlockingQueue<ConsumerRecord<String, String>> records;

	    @BeforeEach
	    void setUp() {
	        // Configurar consumidor de teste
	        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(
	            "test-group", "true", embeddedKafkaBroker
	        );
	        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	        
	        DefaultKafkaConsumerFactory<String, String> consumerFactory = 
	            new DefaultKafkaConsumerFactory<>(consumerProps);
	        
	        ContainerProperties containerProperties = new ContainerProperties("consulta-creditos-topic");
	        records = new LinkedBlockingQueue<>();
	        
	        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProperties);
	        container.setupMessageListener((MessageListener<String, String>) records::add);
	        container.start();
	        
	        ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic());
	    }

	    @AfterEach
	    void tearDown() {
	        if (container != null) {
	            container.stop();
	        }
	    }

	    @Test
	    void testKafkaPublishing() throws Exception {
	        // Dados de teste
	        String tipoConsulta = "CREDITO";
	        String identificador = "123456";

	        // Enviar mensagem
	        producer.publicarConsulta(tipoConsulta, identificador);

	        // Receber e validar mensagem
	        ConsumerRecord<String, String> received = records.poll(10, TimeUnit.SECONDS);
	        
	        assertThat(received).isNotNull();
	        assertEquals("consulta-creditos-topic", received.topic());
	        
	        // Verifica se contém os campos esperados
	        assertThat(received.value())
	            .contains("\"tipoConsulta\":\"CREDITO\"")
	            .contains("\"identificador\":\"123456\"");
	    }
	}


