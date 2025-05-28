package com.tienda.entregas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class EntregaNotFoundException extends RuntimeException {

    public EntregaNotFoundException(String message) {
        super(message);
    }

    public EntregaNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}