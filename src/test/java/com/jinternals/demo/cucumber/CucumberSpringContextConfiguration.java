package com.jinternals.demo.cucumber;

import com.jinternals.demo.Application;
import com.jinternals.demo.testcontainers.SpringBootContextInitializer;
import com.jinternals.demo.repositories.ProductRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeStep;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Slf4j
@CucumberContextConfiguration
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ContextConfiguration(
        initializers = { SpringBootContextInitializer.class },
        classes = {Application.class, }
)
/*
    https://shareablecode.com/snippets/fixing-webtestclient-exception-timeout-on-blocking-read-for-5000-milliseconds-D7UE-JMjW
 */
@AutoConfigureWebTestClient(timeout = "10000")
public class CucumberSpringContextConfiguration {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private DataBag dataBag;


    @Before(order=0)
    public void beforeScenarioStart(){
        log.info("-----------------Start of Scenario-----------------");
    }

    @After(order = 0)
    public void cleanUp() {
        log.info("-----------------clean up database-----------------");
        productRepository.deleteAll().subscribe();
    }

    @After(order=1)
    public void afterScenarioFinish(){
        log.info("-----------------End of Scenario-----------------");
    }

    @Before(value = "@SetupListener")
    public void step() {
        System.out.println("HEADERS");
        dataBag.getHeaders().forEach((s, s2) -> System.out.print(s + " "  + s2));
    }
}
