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
        TestContainersSetup.initTestContainers(configurableApplicationContext.getEnvironment());

        String properties = "spring.couchbase.connection-string=" + TestContainersSetup.getConnectionString();

        log.info("Changed config", properties);

        of(
                properties
        ).applyTo(configurableApplicationContext);
    }
}
