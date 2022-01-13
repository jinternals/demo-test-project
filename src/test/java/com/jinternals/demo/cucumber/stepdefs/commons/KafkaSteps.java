package com.jinternals.demo.cucumber.stepdefs.commons;

import com.jinternals.demo.cucumber.DataBag;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getSingleRecord;

public class KafkaSteps {
    public static final String KAFKA_TIMEOUT_KEY = "kafka-timeout";
    public static final Integer TIMEOUT = 5000;

    @Autowired
    private DataBag dataBag;

    @Given("configure kafka-timeout to {int}")
    public void configure_kafka_timeout_to(Integer timeout) {
        dataBag.getTimeouts().put(KAFKA_TIMEOUT_KEY, timeout);
    }

    @Given("register event {string} as {string}")
    public void register_as_event(String className, String key) throws Exception {
        Class<?> cls = Class.forName(className);
        dataBag.getEvents().put(key, cls);
    }

    @Then("verify {string} is published on {string} topic with content:")
    public void verify_is_published_with_content(String eventType, String topic, String document) {
       // Class aClass = dataBag.getEntities().get(eventType);

        ConsumerRecord<String, String> singleRecord =
                getSingleRecord(dataBag.prepareConsumer(topic), topic, dataBag.getTimeouts().getOrDefault(KAFKA_TIMEOUT_KEY, TIMEOUT));

        assertThat(singleRecord).isNotNull();

        assertThatJson(singleRecord.value()).isEqualTo(document);
    }
}
