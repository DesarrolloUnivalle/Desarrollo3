package com.tienda.usuarios.service;

import com.tienda.usuarios.model.User;
import com.tienda.usuarios.repository.UserRepository;

import com.tienda.usuarios.security.CustomUserDetails;

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

        var authority = new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + user.getRol().getNombre().toUpperCase());
        return new CustomUserDetails(user, java.util.List.of(authority));
    }

}