package com.jinternals.demo.testcontainers.spring;

import com.jinternals.demo.testcontainers.KafkaTestContainerSetup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.test.util.TestPropertyValues.of;

@Slf4j
public class KafkaContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

        // Initialize and start test containers
        KafkaTestContainerSetup.initTestContainers(configurableApplicationContext.getEnvironment());

        String kafkaProperties = "spring.kafka.producer.bootstrap-servers=" + KafkaTestContainerSetup.getBootstrapServers();

        log.info("Changed config", kafkaProperties);

        of(kafkaProperties).applyTo(configurableApplicationContext);
    }
}
