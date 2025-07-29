package com.apiconsultacreditos.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicsConfig {

    @Bean
    public NewTopic healthCheckTopic() {
        return new NewTopic("health-check-topic", 1, (short) 1);
    }
}