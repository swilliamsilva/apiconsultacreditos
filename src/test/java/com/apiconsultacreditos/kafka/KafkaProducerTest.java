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
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

import com.apiconsultacreditos.config.TestKafkaConfig;
import com.apiconsultacreditos.model.Credito;

@SpringBootTest(classes = {ConsultaCreditoProducer.class, TestKafkaConfig.class})
@EmbeddedKafka(partitions = 1, brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
@ActiveProfiles("kafka-test")
@DirtiesContext
public class KafkaProducerTest {
    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private ConsultaCreditoProducer producer;

    private KafkaMessageListenerContainer<String, Credito> container;
    private BlockingQueue<ConsumerRecord<String, Credito>> records;

    @BeforeEach
    void setUp() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("test-group", "true", embeddedKafka);
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.apiconsultacreditos.model");
        consumerProps.put(JsonDeserializer.VALUE_DEFAULT_TYPE, Credito.class.getName());

        var consumerFactory = new DefaultKafkaConsumerFactory<>(
            consumerProps,
            new StringDeserializer(),
            new JsonDeserializer<>(Credito.class)
        );
        
        var containerProps = new ContainerProperties("consulta-creditos-topic");

        records = new LinkedBlockingQueue<>();
        container = new KafkaMessageListenerContainer<>(consumerFactory, containerProps);
        container.setupMessageListener((MessageListener<String, Credito>) records::add);
        container.start();

        ContainerTestUtils.waitForAssignment(container, embeddedKafka.getPartitionsPerTopic());
    }

    @AfterEach
    void tearDown() {
        if (container != null) container.stop();
    }

    @Test
    void testKafkaPublishing() throws Exception {
        producer.publicarConsulta("123456");
        var received = records.poll(10, TimeUnit.SECONDS);

        assertThat(received).isNotNull();
        assertEquals("consulta-creditos-topic", received.topic());
        assertEquals("123456", received.value().getNumeroCredito());
    }

    @DynamicPropertySource
    static void kafkaProps(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers",
            () -> System.getProperty("spring.embedded.kafka.brokers")
        );
    }
}