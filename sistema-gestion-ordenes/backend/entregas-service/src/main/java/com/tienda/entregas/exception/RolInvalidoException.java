package com.tienda.entregas.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RolInvalidoException extends RuntimeException {

    public RolInvalidoException(String message) {
        super(message);
    }

    public RolInvalidoException(String message, Throwable cause) {
        super(message, cause);
    }
} 