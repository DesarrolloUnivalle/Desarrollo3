package com.tienda.ordenes.config;

import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class FeignConfig {
    private static final Logger logger = LoggerFactory.getLogger(FeignConfig.class);

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            try {
                Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                if (principal instanceof Jwt jwt) {
                    String token = jwt.getTokenValue();
                    logger.debug("Agregando token JWT a la petición: {}", token);
                    requestTemplate.header("Authorization", "Bearer " + token);
                } else {
                    logger.warn("El principal no es una instancia de Jwt: {}", principal);
                }
            } catch (Exception e) {
                logger.error("Error al obtener el token JWT: {}", e.getMessage());
            }
        };
    }
} 