package com.jinternals.demo.cucumber;

import com.jinternals.demo.Application;
import com.jinternals.demo.testcontainers.SpringBootContextInitializer;
import com.jinternals.demo.repositories.ProductRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.spring.CucumberContextConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CucumberSpringContextConfiguration {

    @Autowired
    private ProductRepository productRepository;

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

}
