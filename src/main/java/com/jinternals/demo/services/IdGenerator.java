package com.jinternals.demo.services;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IdGenerator {

    public String generateId(){
        return UUID.randomUUID().toString();
    }

}
