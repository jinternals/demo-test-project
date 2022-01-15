package com.jinternals.demo.configuration;

import com.jinternals.demo.event.KafkaEventGateway;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;

import java.util.Arrays;

class KafkaProducerConfigurationTest {
    ApplicationContextRunner context = new ApplicationContextRunner()
            .withPropertyValues(
                    "event.package=com.jinternals.demo.configuration"
            )
            .withUserConfiguration(TestConfig.class, KafkaProducerConfiguration.class);

    @TestConfiguration
    static  class TestConfig{
        @Bean
        KafkaProperties getKafkaProperties(){
            KafkaProperties kafkaProperties = new KafkaProperties();
            kafkaProperties.setBootstrapServers(Arrays.asList("localhost:9092"));
            kafkaProperties.buildProducerProperties()
                    .put("key-serializer", "org.apache.kafka.common.serialization.StringSerializer");
            kafkaProperties.buildProducerProperties()
                    .put("value-serializer", "org.springframework.kafka.support.serializer.JsonSerializer");

            return kafkaProperties;
        }
    }
    @Test
    public void shouldVerifyBeansAreConfigured() {

        context.run(it -> {
            Assertions.assertThat(it).hasBean("destinations");
            Assertions.assertThat(it).hasSingleBean(ReactiveKafkaProducerTemplate.class);
            Assertions.assertThat(it).hasSingleBean(KafkaEventGateway.class);
        });
    }
}
