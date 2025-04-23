package com.tienda.usuarios.service;

import com.tienda.usuarios.model.User;
import com.tienda.usuarios.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // Constructor
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Buscar al usuario por su email en la base de datos
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
        
        // Devolver el usuario autenticado con Spring Security
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())  // El email es el nombre de usuario para la autenticación
            .password(user.getPassword())  // La contraseña cifrada
            .roles(user.getRol().getNombre())  // El rol del usuario (puedes personalizarlo si necesitas más roles)
            .build();
    }
}