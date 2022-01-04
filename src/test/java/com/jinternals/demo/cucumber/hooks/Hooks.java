package com.jinternals.demo.cucumber.hooks;


import com.jinternals.demo.repositories.ProductRepository;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.springframework.beans.factory.annotation.Autowired;

public class Hooks {

    @Autowired
    private ProductRepository productRepository;

    @Before(order=0)
    public void beforeScenarioStart(){
        System.out.println("-----------------Start of Scenario-----------------");
    }


    @After(order=0)
    public void afterScenarioFinish(){
        System.out.println("-----------------End of Scenario-----------------");

        productRepository.deleteAll();
    }

}
