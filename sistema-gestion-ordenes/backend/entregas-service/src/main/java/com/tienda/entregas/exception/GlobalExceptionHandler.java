package com.tienda.entregas.exception;

import feign.FeignException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(EntregaNotFoundException.class)
    public ResponseEntity<Object> handleEntregaNotFoundException(
            EntregaNotFoundException ex, WebRequest request) {
        return crearRespuestaError(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(RolInvalidoException.class)
    public ResponseEntity<Object> handleRolInvalidoException(
            RolInvalidoException ex, WebRequest request) {
        return crearRespuestaError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<Object> handleFeignException(
            FeignException ex, WebRequest request) {
        
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (ex.status() > 0) {
            status = HttpStatus.valueOf(ex.status());
        }
        
        String mensaje = "Error al comunicarse con otro servicio: " + ex.getMessage();
        logger.error(mensaje);
        
        return crearRespuestaError(status, mensaje, request);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgumentException(
            IllegalArgumentException ex, WebRequest request) {
        return crearRespuestaError(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(
            Exception ex, WebRequest request) {
        
        logger.error("Error no controlado: ", ex);
        String mensaje = "Ha ocurrido un error inesperado: " + ex.getMessage();
        
        return crearRespuestaError(HttpStatus.INTERNAL_SERVER_ERROR, mensaje, request);
    }

    private ResponseEntity<Object> crearRespuestaError(
            HttpStatus status, String mensaje, WebRequest request) {
        
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now().toString());
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", mensaje);
        body.put("path", request.getDescription(false).replace("uri=", ""));
        
        return new ResponseEntity<>(body, status);
    }
} 