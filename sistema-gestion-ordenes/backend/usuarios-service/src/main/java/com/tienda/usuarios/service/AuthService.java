package com.tienda.usuarios.service;

import com.tienda.usuarios.dto.AuthRequestDTO;
import com.tienda.usuarios.dto.AuthResponseDTO;
import com.tienda.usuarios.model.User;
import com.tienda.usuarios.repository.UserRepository;
import com.tienda.usuarios.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthResponseDTO autenticarUsuario(AuthRequestDTO request) {
        // Verificar si el usuario existe
        User usuario = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Verificar la contraseña
        if (!passwordEncoder.matches(request.getPassword(), usuario.getPassword())) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        // Crear el token JWT
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        String username = ((UserDetails) authentication.getPrincipal()).getUsername();
        String token = jwtUtil.generateToken(username);

        return new AuthResponseDTO(token);
    }
}