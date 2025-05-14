package com.tienda.entregas.config;

import feign.Logger;
import feign.codec.ErrorDecoder;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignClientConfig {

    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(FeignClientConfig.class);

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    public static class FeignErrorDecoder implements ErrorDecoder {
        private final ErrorDecoder defaultErrorDecoder = new Default();

        @Override
        public Exception decode(String methodKey, feign.Response response) {
            logger.error("Error en llamada a servicio Feign: {} - Status: {}", methodKey, response.status());
            
            // Usar el decodificador por defecto para obtener la excepción
            Exception exception = defaultErrorDecoder.decode(methodKey, response);
            
            // Podemos personalizar el manejo de errores según el estado HTTP
            if (response.status() == 404) {
                logger.error("El recurso solicitado no existe en el servicio remoto");
            } else if (response.status() == 500) {
                logger.error("Error interno en el servicio remoto");
            }
            
            return exception;
        }
    }
} 