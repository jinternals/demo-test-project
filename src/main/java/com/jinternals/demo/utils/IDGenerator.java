package com.jinternals.demo.utils;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class IDGenerator {

    public String generateId(){
        return UUID.randomUUID().toString();
    }

}
