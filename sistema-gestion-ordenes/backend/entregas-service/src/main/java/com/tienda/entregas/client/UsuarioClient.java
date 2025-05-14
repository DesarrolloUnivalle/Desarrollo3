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

    // Obtener usuario por ID
    @GetMapping("/api/usuarios/{id}")
    default UserResponseDTO obtenerUsuarioPorId(@PathVariable("id") Long id, @RequestHeader("Authorization") String token) {
        logger.info("Llamando a obtenerUsuarioPorId con ID: {} y token: {}", id, token);
        UserResponseDTO usuario = obtenerUsuarioPorIdInternal(id, token);
        logger.info("Respuesta del servicio de usuarios (ID {}): {}", id, usuario);
        return usuario;
    }

    // Obtener usuario por email
    @GetMapping("/api/usuarios/email/{email}")
    default UserResponseDTO obtenerUsuarioPorEmail(@PathVariable("email") String email, @RequestHeader("Authorization") String token) {
        logger.info("Llamando a obtenerUsuarioPorEmail con email: {} y token: {}", email, token);
        UserResponseDTO usuario = obtenerUsuarioPorEmailInternal(email, token);
        logger.info("Respuesta del servicio de usuarios (email {}): {}", email, usuario);
        return usuario;
    }

    // Métodos internos para Feign
    @GetMapping("/api/usuarios/{id}/internal")
    UserResponseDTO obtenerUsuarioPorIdInternal(@PathVariable("id") Long id, @RequestHeader("Authorization") String token);

    @GetMapping("/api/usuarios/email/{email}/internal")
    UserResponseDTO obtenerUsuarioPorEmailInternal(@PathVariable("email") String email, @RequestHeader("Authorization") String token);
}