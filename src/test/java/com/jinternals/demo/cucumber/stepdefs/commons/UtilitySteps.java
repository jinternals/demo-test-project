package com.jinternals.demo.cucumber.stepdefs.commons;

import com.jinternals.demo.cucumber.DataBag;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class UtilitySteps {

    @Autowired
    private DataBag dataBag;

    @Given("register pojo {string} as {string}")
    public void register_pojo_as(String className, String key) throws Exception {
        Class<?> cls = Class.forName(className);
        dataBag.getModels().put(key, cls);
    }

    @Given("configure http headers to:")
    public void i_set_http_headers_to(Map<String, String> headers) {
        headers.forEach((key, value) -> dataBag.getHeaders().put(key, value));
    }
}
