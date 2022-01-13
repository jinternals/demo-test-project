package com.jinternals.demo.cucumber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinternals.demo.Application;
import com.jinternals.demo.testcontainers.SpringBootContextInitializer;
import com.jinternals.demo.repositories.ProductRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(
        initializers = {SpringBootContextInitializer.class},
        classes = {Application.class,}
)
/*
    https://shareablecode.com/snippets/fixing-webtestclient-exception-timeout-on-blocking-read-for-5000-milliseconds-D7UE-JMjW
 */
@AutoConfigureWebTestClient(timeout = "10000")
public class CucumberSpringContextConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private DataBag dataBag;

    @After(order = 0)
    public void cleanUp() {
        log.info("-----------------start data clean up database-----------------");

        List<Class> classes = dataBag.getModels().values().stream().collect(Collectors.toList());
        assertThat(classes).isNotNull();

        Repositories repositories = new Repositories(applicationContext);
        Map<Class ,ReactiveCrudRepository> crudRepositories = classes.stream().collect(Collectors.toMap(Function.identity(),(aClass)-> (ReactiveCrudRepository) repositories.getRepositoryFor(aClass).get()));

        crudRepositories.forEach(( type, reactiveCrudRepository) -> {
                    log.info("Total Item count {} in {}", reactiveCrudRepository.count().block(), type);
                    reactiveCrudRepository.deleteAll().subscribe();
                }
        );

        log.info("-----------------end data clean up database-----------------");
    }

}
