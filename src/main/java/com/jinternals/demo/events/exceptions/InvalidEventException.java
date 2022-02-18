package com.jinternals.demo.events.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class InvalidEventException extends RuntimeException {

    public InvalidEventException(String message) {
        super(message);
    }

}
