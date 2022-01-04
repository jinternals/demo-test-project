package com.jinternals.demo.cucumber.stepdefs.commons;

import com.jinternals.demo.cucumber.DataBag;
import io.cucumber.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

public class WebClientSteps {

    @Autowired
    private DataBag dataBag;
    @Autowired
    private WebTestClient webTestClient;

    @When("the client invokes POST {string} with:")
    public void the_client_post_to_with(String string, String jsonBody) throws Exception {
        EntityExchangeResult<String> result = webTestClient
                .post()
                .uri(string)
                .headers(httpHeaders -> dataBag.getHeaders().forEach(httpHeaders::add))
                .body(Mono.just(jsonBody), String.class)
                .exchange()
                .expectBody(String.class)
                .returnResult();

        dataBag.setResult(result);
    }

    @When("the client invokes GET {string}")
    public void the_client_get_to(String uri) {
        EntityExchangeResult<String> result = webTestClient
                .get()
                .uri(uri, dataBag.getData())
                .headers(httpHeaders -> dataBag.getHeaders().forEach(httpHeaders::add))
                .exchange()
                .expectBody(String.class)
                .returnResult();

        dataBag.setResult(result);
    }

}
