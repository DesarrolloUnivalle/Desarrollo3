package com.tienda.usuarios.service;

import com.tienda.usuarios.dto.LoginRequest;
import com.tienda.usuarios.model.Usuario;
import com.tienda.usuarios.repository.UsuarioRepository;
import com.tienda.usuarios.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceTest {

    private AuthService authService;
    private UsuarioRepository usuarioRepository;
    private AuthenticationManager authenticationManager;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        authenticationManager = mock(AuthenticationManager.class);
        jwtUtil = mock(JwtUtil.class);
        authService = new AuthService(usuarioRepository, authenticationManager, jwtUtil);
    }

    @Test
    void loginUsuarioValido_devuelveToken() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("usuario", "password");
        Authentication auth = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(auth);
        when(jwtUtil.generateToken(any())).thenReturn("fake-jwt-token");

        // Act
        String token = authService.login(loginRequest);

        // Assert
        assertNotNull(token);
        assertEquals("fake-jwt-token", token);
    }

    @Test
    void loginUsuarioInvalido_lanzaExcepcion() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("usuario", "password");
        when(authenticationManager.authenticate(any()))
            .thenThrow(new RuntimeException("Credenciales inválidas"));

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            authService.login(loginRequest);
        });

        assertEquals("Credenciales inválidas", exception.getMessage());
    }
}
