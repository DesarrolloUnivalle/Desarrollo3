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

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + email));
    
        System.out.println("🔎 Cargando usuario: " + email);
        System.out.println("🔹 Rol del usuario: " + user.getRol().getNombre());
    
        return org.springframework.security.core.userdetails.User.builder()
            .username(user.getEmail())
            .password(user.getPassword()) // Asegúrate de que la contraseña está cifrada
            .roles(user.getRol().getNombre()) // Spring Security usa roles
            .build();
    }
}