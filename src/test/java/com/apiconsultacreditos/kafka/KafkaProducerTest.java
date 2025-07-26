package com.apiconsultacreditos.kafka;

import com.apiconsultacreditos.event.ConsultaCreditoEvent;
import com.apiconsultacreditos.model.Credito;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Testcontainers
@EmbeddedKafka(partitions = 1, topics = {"consulta-creditos-topic"})
public class KafkaProducerTest {

    @Autowired
    private ConsultaCreditoProducer producer;

    @Autowired
    private EmbeddedKafkaBroker embeddedKafka;

    private org.apache.kafka.clients.consumer.Consumer<String, ConsultaCreditoEvent> consumer;

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("CreditoDB")
            .withUsername("postgres")
            .withPassword("postgres");

    @DynamicPropertySource
    static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    void setupConsumer() {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "true", embeddedKafka);
        DefaultKafkaConsumerFactory<String, ConsultaCreditoEvent> consumerFactory =
                new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new org.springframework.kafka.support.serializer.JsonDeserializer<>(ConsultaCreditoEvent.class, false));

        consumer = consumerFactory.createConsumer();
        embeddedKafka.consumeFromAnEmbeddedTopic(consumer, "consulta-creditos-topic");
    }

    @Test
    void devePublicarEventoKafkaComSucesso() {
        Credito credito = Credito.builder()
                .numeroCredito("CRED-12345")
                .numeroNfse("NFSE-54321")
                .dataConstituicao(LocalDate.of(2023, 7, 1))
                .valorIssqn(new BigDecimal("150.00"))
                .tipoCredito("ISSQN")
                .simplesNacional(true)
                .aliquota(new BigDecimal("2.50"))
                .valorFaturado(new BigDecimal("10000.00"))
                .valorDeducao(new BigDecimal("1000.00"))
                .baseCalculo(new BigDecimal("9000.00"))
                .build();

        producer.publicarConsulta(credito);

        ConsumerRecord<String, ConsultaCreditoEvent> record =
                KafkaTestUtils.getSingleRecord(consumer, "consulta-creditos-topic", Duration.ofSeconds(10));

        assertThat(record).isNotNull();
        assertThat(record.value().getNumeroCredito()).isEqualTo("CRED-12345");
        assertThat(record.value().getValorIssqn()).isEqualByComparingTo("150.00");
    }
}
