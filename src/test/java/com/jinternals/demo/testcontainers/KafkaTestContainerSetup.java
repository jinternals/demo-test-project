package com.jinternals.demo.testcontainers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.ConfigurableEnvironment;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;
import org.testcontainers.utility.DockerImageName;

@Slf4j
public class KafkaTestContainerSetup {
    private static final String KAFKA_IMAGE = "confluentinc/cp-kafka:6.2.1";
    private static final Logger KAFKA_LOGGER = LoggerFactory.getLogger("container.Kafka");

    private static final KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse(KAFKA_IMAGE)
                    .asCompatibleSubstituteFor("confluentinc/kafka"));


    public static void initTestContainers(ConfigurableEnvironment configEnv) {
        if(kafkaContainer.isRunning()){
            return;
        }

        kafkaContainer.withEmbeddedZookeeper();

        KAFKA_LOGGER.info("Stating kafka test container");

        kafkaContainer.start();

        kafkaContainer.followOutput(new Slf4jLogConsumer(KAFKA_LOGGER));

    }

    public static String getBootstrapServers() {
        return kafkaContainer.getBootstrapServers();
    }


}
