package com.jinternals.demo.cucumber.stepdefs.commons;

import com.jayway.jsonpath.JsonPath;
import com.jinternals.demo.cucumber.DataBag;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.valueOf;

public class AssertSteps {

    @Autowired
    private DataBag dataBag;

    @And("{string} should be {string}")
    public void should_be(String path, String value) {
        String read = JsonPath.read(dataBag.getResult().getResponseBody(), path);
        if(value.equals("null"))
        {
            assertThat(read).isNull();
        }else {
            assertThat(read).isEqualTo(value);
        }
    }

    @And("{string} should not be {string}")
    public void should_not_be_null(String path, String value) {
        String read = JsonPath.read(dataBag.getResult().getResponseBody(), path);
        if(value.equals("null"))
        {
            assertThat(read).isNotNull();
        }else {
            assertThat(read).isNotEqualTo(value);
        }
    }


    @Then("api respond with status {int}")
    public void api_respond_with_status(int status) {
        assertThat(dataBag.getResult().getStatus()).isEqualTo(valueOf(status));
    }

}
