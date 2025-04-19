package com.tienda.ordenes.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class StockInsuficienteException extends RuntimeException {
    
    public StockInsuficienteException(String message) {
        super(message);
    }
    
    public StockInsuficienteException(String message, Throwable cause) {
        super(message, cause);
    }
} 