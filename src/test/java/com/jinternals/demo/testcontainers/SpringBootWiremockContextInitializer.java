package com.jinternals.demo.testcontainers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import static org.springframework.boot.test.util.TestPropertyValues.of;

@Slf4j
public class SpringBootWiremockContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext configurableApplicationContext) {

        // Initialize and start test containers
        WiremockTestContainerSetup.initTestContainers(configurableApplicationContext.getEnvironment());

        String kafkaProperties = "spring.bootstrap-servers=" + WiremockTestContainerSetup.getWiremockServerUrl();

        log.info("Changed config", kafkaProperties);

        of(kafkaProperties).applyTo(configurableApplicationContext);
    }
}
