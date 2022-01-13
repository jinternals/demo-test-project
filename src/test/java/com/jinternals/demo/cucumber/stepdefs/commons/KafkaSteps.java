package com.jinternals.demo.cucumber.stepdefs.commons;

import com.jinternals.demo.cucumber.DataBag;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

public class KafkaSteps {

    @Autowired
    private DataBag dataBag;

    @Then("verify {string} is published on {string} topic with content:")
    public void verify_is_published_with_content(String eventType, String topic, String document) {
        Class aClass = dataBag.getModels().get(eventType);


    }
}
