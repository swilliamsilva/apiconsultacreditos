package com.apiconsultacreditos.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import com.apiconsultacreditos.model.Credito;
import com.apiconsultacreditos.repository.CreditoRepository;

@SpringBootTest
@EmbeddedKafka(
    topics = {"consulta-creditos-topic"}, 
    partitions = 1,
    brokerProperties = {
        "listeners=PLAINTEXT://localhost:0", 
        "port=0"
    },
    bootstrapServersProperty = "spring.kafka.bootstrap-servers"
)
@ActiveProfiles("kafka-test")
@DirtiesContext
@Import(SecurityConfig.class) // Importa a configuração de segurança externa
class KafkaProducerTest {

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    @Autowired
    private ConsultaCreditoProducer producer;

    @MockBean
    private CreditoRepository creditoRepository;

    private KafkaMessageListenerContainer<String, Credito> container;
    private BlockingQueue<ConsumerRecord<String, Credito>> records;

    @BeforeEach
    void setUp() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(
            "test-group", "true", embeddedKafka
        );
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        consumerProps.put(JsonDeserializer.TRUSTED_PACKAGES, "com.apiconsultacreditos.model");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");

        var consumerFactory = new DefaultKafkaConsumerFactory<>(
            consumerProps,
            new StringDeserializer(),
            new JsonDeserializer<>(Credito.class, false)
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
        if (container != null) {
            container.stop();
        }
    }

    @Test
    void testKafkaPublishing() throws Exception {
        Credito credito = new Credito();
        credito.setNumeroCredito("123456");
        credito.setNumeroNfse("NFSE-123");
        credito.setTipoCredito("ISSQN");
        credito.setSimplesNacional(true);
        credito.setValorIssqn(new BigDecimal("100.00"));
        credito.setDataConstituicao(java.time.LocalDate.now());
        
        producer.publicarConsulta(credito);

        ConsumerRecord<String, Credito> received = records.poll(10, TimeUnit.SECONDS);

        assertThat(received).isNotNull();
        assertEquals("consulta-creditos-topic", received.topic());
        assertEquals("123456", received.value().getNumeroCredito());
    }
}