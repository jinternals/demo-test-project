package com.jinternals.demo.testcontainers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.testcontainers.containers.BindMode;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.utility.DockerImageName;

@Slf4j
public class WiremockTestContainerSetup {
    private static final Logger WIREMOCK_LOGGER = LoggerFactory.getLogger("container.Wiremock");

    private static final GenericContainer<?> wiremockContainer =
            new GenericContainer(DockerImageName.parse("wiremock/wiremock:2.32.0-alpine"));


    public static void initTestContainers(ConfigurableEnvironment configEnv) {
        if(wiremockContainer.isRunning()){
            return;
        }

        wiremockContainer.withExposedPorts(8080)
                .withClasspathResourceMapping("wiremock",
                        "/home/wiremock",
                        BindMode.READ_ONLY)
                .waitingFor(Wait.forHttp("/__admin/mappings")
                        .withMethod("GET")
                        .forStatusCode(200));

        WIREMOCK_LOGGER.info("Stating kafka test container");

        wiremockContainer.start();

        wiremockContainer.followOutput(new Slf4jLogConsumer(WIREMOCK_LOGGER));

    }

    public static String getWiremockServerUrl() {
        return "http://localhost:"+wiremockContainer.getMappedPort(8080);
    }


}
