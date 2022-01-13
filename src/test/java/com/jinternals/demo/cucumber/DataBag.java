package com.jinternals.demo.cucumber;

import com.jinternals.demo.testcontainers.KafkaTestContainersSetup;
import io.cucumber.spring.ScenarioScope;
import lombok.Data;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.jetbrains.annotations.NotNull;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.EntityExchangeResult;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG;


@Data
@Component
@ScenarioScope
public class DataBag {

    private Map<String,String> headers = new HashMap<>();
    private Map<String,Class> entities = new HashMap<>();
    private Map<String,Class<?>> events = new HashMap<>();
    private Map<String,Object> data = new HashMap<>();
    private static Map<String,Consumer<String, String>> consumers = new HashMap<>();
    private Map<String,Integer> timeouts = new HashMap<>();

    private EntityExchangeResult<String> result;

    public Consumer<String, String> prepareConsumer(String topic) {
        if(!consumers.containsKey(topic)){
            consumers.put(topic,  prepareKafkaConsumer(topic));
        }
        return consumers.get(topic);
    }

    private Consumer<String, String> prepareKafkaConsumer(String topic) {
        Map<String, Object> consumerProps = KafkaTestUtils.consumerProps(KafkaTestContainersSetup.getBootstrapServers(), "testGroup","true");
        consumerProps.put(AUTO_OFFSET_RESET_CONFIG, "earliest");

        Consumer<String, String> consumer = new DefaultKafkaConsumerFactory<>(consumerProps, new StringDeserializer(), new StringDeserializer()).createConsumer();
        consumer.subscribe(Collections.singletonList(topic));
        return consumer;
    }
}
