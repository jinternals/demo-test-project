package com.jinternals.demo.cucumber;

import com.jinternals.demo.Application;
import com.jinternals.demo.events.annotation.Event;
import com.jinternals.demo.test.spring.CouchbaseContextInitializer;
import com.jinternals.demo.test.spring.KafkaContextInitializer;
import com.jinternals.demo.test.spring.WiremockContextInitializer;
import com.jinternals.demo.utils.ReflectionUtils;
import io.cucumber.java.After;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jinternals.demo.cucumber.stepdefs.commons.KafkaSteps.KAFKA_TIMEOUT_KEY;
import static com.jinternals.demo.cucumber.stepdefs.commons.KafkaSteps.TIMEOUT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.kafka.test.utils.KafkaTestUtils.getRecords;

@Slf4j
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(
        initializers = {
                KafkaContextInitializer.class,
                CouchbaseContextInitializer.class,
                WiremockContextInitializer.class
        },
        classes = {
                Application.class
        }
)
/*
    https://shareablecode.com/snippets/fixing-webtestclient-exception-timeout-on-blocking-read-for-5000-milliseconds-D7UE-JMjW
 */
@AutoConfigureWebTestClient(timeout = "20000")
public class CucumberSpringContextConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DataBag dataBag;

    @After(order = 0)
    public void cleanUp() {
        log.info("-----------------start data clean up database-----------------");

        List<Class> classes = dataBag.getEntities().values().stream().collect(Collectors.toList());
        assertThat(classes).isNotNull();

        Repositories repositories = new Repositories(applicationContext);

        Map<Class, ReactiveCrudRepository> crudRepositories = classes.stream().collect(Collectors.toMap(Function.identity(), (aClass) -> (ReactiveCrudRepository) repositories.getRepositoryFor(aClass).get()));

        crudRepositories.forEach((type, reactiveCrudRepository) -> {
                    log.info("Total Item count {} in {}", reactiveCrudRepository.count().block(), type);
                    reactiveCrudRepository.deleteAll().subscribe();
                }
        );

        log.info("-----------------end data clean up database-----------------");
    }

    @After(order = 1)
    public void cleanUpKafka() {
        log.info("-----------------start kafka clean up -----------------");
        List<Class<?>> classes = dataBag.getEvents().values().stream().collect(Collectors.toList());

        classes.forEach(aClass -> {
            String destination = ReflectionUtils.getEventDestination(aClass, applicationContext.getEnvironment());
            ConsumerRecords<String, String> records = getRecords(dataBag.prepareConsumer(destination), dataBag.getTimeouts().getOrDefault(KAFKA_TIMEOUT_KEY, TIMEOUT));
            log.info("Cleaned up topic {} message count {}", destination, records.count());
        });

        log.info("-----------------end kafka clean up-----------------");
    }

}
