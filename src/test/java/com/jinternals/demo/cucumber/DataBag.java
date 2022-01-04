package com.jinternals.demo.cucumber;

import io.cucumber.spring.ScenarioScope;
import lombok.Data;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.web.reactive.server.EntityExchangeResult;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;


@Data
@Component
@ScenarioScope
public class DataBag {

    private Map<String,String> headers = new HashMap<>();
    private Map<String,Class> models = new HashMap<>();
    private Map<String,Object> data = new HashMap<>();
    private EntityExchangeResult<String> result;

}
