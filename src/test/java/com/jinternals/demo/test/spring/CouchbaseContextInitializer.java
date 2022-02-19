package com.jinternals.demo.test.spring;

import com.jinternals.demo.test.containers.CouchbaseTestContainerSetup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.test.util.TestPropertyValues.of;

@Slf4j
public class CouchbaseContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

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
