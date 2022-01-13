package com.jinternals.demo.testcontainers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.test.util.TestPropertyValues.of;

@Slf4j
public class SpringBootContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

        // Initialize and start test containers
        CouchbaseTestContainersSetup.initTestContainers(configurableApplicationContext.getEnvironment());
        KafkaTestContainersSetup.initTestContainers(configurableApplicationContext.getEnvironment());
        String couchbaseConnectionString = "spring.couchbase.connection-string=" + CouchbaseTestContainersSetup.getConnectionString();
        String kafkaBootstrapServers = "spring.kafka.producer.bootstrap-servers=" + KafkaTestContainersSetup.getBootstrapServers();

        of(couchbaseConnectionString, kafkaBootstrapServers)
                .applyTo(configurableApplicationContext);

    }
}
