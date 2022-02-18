package com.jinternals.demo.cucumber.stepdefs.commons;

import com.jayway.jsonpath.JsonPath;
import com.jinternals.demo.cucumber.DataBag;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class UtilitySteps {

    @Autowired
    private DataBag dataBag;

    @Given("register entity {string} as {string}")
    public void register_as_entity(String className, String key) throws Exception {
        Class<?> cls = Class.forName(className);
        dataBag.getEntities().put(key, cls);
    }

    @Given("configure http headers to:")
    public void i_set_http_headers_to(Map<String, String> headers) {
        headers.forEach((key, value) -> dataBag.getHeaders().put(key, value));
    }

    @Given("save in scope {string} as {string}")
    public void save_in_scope_as(String jsonPath, String key) {
        Object read = JsonPath.read(dataBag.getResult().getResponseBody(), jsonPath);
        dataBag.getData().put(key, read);
    }
}
