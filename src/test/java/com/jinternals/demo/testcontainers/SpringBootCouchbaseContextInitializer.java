package com.jinternals.demo.testcontainers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.test.util.TestPropertyValues.of;

@Slf4j
public class SpringBootCouchbaseContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

        // Initialize and start test containers
        CouchbaseTestContainerSetup.initTestContainers(configurableApplicationContext.getEnvironment());

        String properties = "spring.couchbase.connection-string=" + CouchbaseTestContainerSetup.getConnectionString();

        log.info("Changed config", properties);

        of(
                properties
        ).applyTo(configurableApplicationContext);
    }
}
