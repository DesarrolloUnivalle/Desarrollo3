package com.tienda.entregas.client;

import com.tienda.entregas.dto.UserResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "usuarios-service", url = "${usuarios-service.url}")
public interface UsuarioClient {

    Logger logger = LoggerFactory.getLogger(UsuarioClient.class);

    // Obtener usuario por ID - version con log detallado
    @GetMapping("/usuarios/{usuario_id}")
    default UserResponseDTO obtenerUsuarioPorId(@PathVariable("usuario_id") Long id, @RequestHeader("Authorization") String token) {
        logger.info("Llamando a obtenerUsuarioPorId con ID: {} y token: {}", id, token);
        try {
            UserResponseDTO usuario = obtenerUsuarioPorIdInternal(id, token);
            logger.info("Respuesta del servicio de usuarios (ID {}): {}", id, usuario);
            return usuario;
        } catch (Exception e) {
            logger.error("Error al obtener usuario con ID {}: {}", id, e.getMessage(), e);
            throw e;
        }
    }

    // Obtener usuario por email
    @GetMapping("/usuarios/email/{email}")
    default UserResponseDTO obtenerUsuarioPorEmail(@PathVariable("email") String email, @RequestHeader("Authorization") String token) {
        logger.info("Llamando a obtenerUsuarioPorEmail con email: {} y token: {}", email, token);
        try {
            UserResponseDTO usuario = obtenerUsuarioPorEmailInternal(email, token);
            logger.info("Respuesta del servicio de usuarios (email {}): {}", email, usuario);
            return usuario;
        } catch (Exception e) {
            logger.error("Error al obtener usuario con email {}: {}", email, e.getMessage(), e);
            throw e;
        }
    }

    // Métodos internos para Feign
    @GetMapping("/usuarios/{usuario_id}")
    UserResponseDTO obtenerUsuarioPorIdInternal(@PathVariable("usuario_id") Long id, @RequestHeader("Authorization") String token);

    @GetMapping("/usuarios/email/{email}")
    UserResponseDTO obtenerUsuarioPorEmailInternal(@PathVariable("email") String email, @RequestHeader("Authorization") String token);
}