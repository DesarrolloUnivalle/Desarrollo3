package com.tienda.ordenes.client;

import com.tienda.ordenes.dto.UserResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "usuarios-service", url = "http://localhost:8082")
public interface UsuarioClient {

    Logger logger = LoggerFactory.getLogger(UsuarioClient.class);

    @GetMapping("/usuarios/{usuario_id}")
    default UserResponseDTO obtenerUsuario(@PathVariable("usuario_id") Long id) {
        UserResponseDTO usuario = obtenerUsuarioInternal(id);
        logger.info("Respuesta del servicio de usuarios (por ID): {}", usuario);
        return usuario;
    }

    @GetMapping("/usuarios/email/{email}")
    default UserResponseDTO obtenerUsuarioPorEmail(@PathVariable("email") String email) {
        UserResponseDTO usuario = obtenerUsuarioPorEmailInternal(email);
        logger.info("Respuesta del servicio de usuarios (por email): {}", usuario);
        return usuario;
    }

    @GetMapping("/usuarios/{usuario_id}")
    UserResponseDTO obtenerUsuarioInternal(@PathVariable("usuario_id") Long id);

    @GetMapping("/usuarios/email/{email}")
    UserResponseDTO obtenerUsuarioPorEmailInternal(@PathVariable("email") String email);
}

