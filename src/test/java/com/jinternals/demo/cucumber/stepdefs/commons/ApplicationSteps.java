package com.jinternals.demo.cucumber.stepdefs.commons;


import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.reactive.server.WebTestClient;

public class ApplicationSteps {

    @Autowired
    private WebTestClient webTestClient;;

    @Given("^the app has started$")
    public void the_app_has_started() {
        ResponseEntity<String> response;
        webTestClient.get()
                .uri("/")
                .exchange()
                .expectStatus().isNotFound();
    }

}

