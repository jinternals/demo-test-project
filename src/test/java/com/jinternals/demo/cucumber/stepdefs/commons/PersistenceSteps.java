package com.jinternals.demo.cucumber.stepdefs.commons;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jinternals.demo.cucumber.DataBag;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.support.Repositories;

import static org.assertj.core.api.Assertions.assertThat;

public class PersistenceSteps {

    @Autowired
    private ApplicationContext applicationContext;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private DataBag dataBag;

    @Given("{string} with following details exist in db:")
    public void with_following_details_exist_in_db(String model, String document) throws Exception {
        Class aClass = dataBag.getEntities().get(model);
        assertThat(aClass).isNotNull();

        Repositories repositories = new Repositories(applicationContext);
        ReactiveCrudRepository repository = (ReactiveCrudRepository) repositories.getRepositoryFor(aClass).get();
        assertThat(repository).isNotNull();

        Object savedEntity = repository.save(objectMapper.readValue(document, aClass)).block();
        assertThat(savedEntity).isInstanceOf(aClass);
    }


}
