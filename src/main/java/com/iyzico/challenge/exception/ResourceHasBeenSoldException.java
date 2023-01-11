package com.iyzico.challenge.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.GONE)
public class ResourceHasBeenSoldException extends RuntimeException {
    public ResourceHasBeenSoldException(String message) {
        super(message);
    }
}
