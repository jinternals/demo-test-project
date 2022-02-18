package com.jinternals.demo.testcontainers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.test.util.TestPropertyValues.of;

@Slf4j
public class SpringBootKafkaContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

        // Initialize and start test containers
        KafkaTestContainersSetup.initTestContainers(configurableApplicationContext.getEnvironment());

        String kafkaProperties = "spring.kafka.producer.bootstrap-servers=" + KafkaTestContainersSetup.getBootstrapServers();

        log.info("Changed config", kafkaProperties);

        of(kafkaProperties).applyTo(configurableApplicationContext);
    }
}
