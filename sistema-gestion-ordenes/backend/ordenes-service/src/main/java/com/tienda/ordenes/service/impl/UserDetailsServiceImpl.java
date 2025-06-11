package com.tienda.ordenes.service.impl;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // No consultamos base de datos, solo creamos un usuario ficticio con el email del token
        return org.springframework.security.core.userdetails.User
                .withUsername(email)
                .password("") // No se usa porque no hacemos login en este servicio
                .roles("USER") // Puedes cambiar esto según el token, si deseas extraer roles reales
                .build();
    }
}
