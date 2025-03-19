package com.tienda.usuarios.service;

import com.tienda.usuarios.model.Role;
import com.tienda.usuarios.model.User;
import com.tienda.usuarios.repository.RoleRepository;
import com.tienda.usuarios.repository.UserRepository;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository; // Agregado
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerUser(User user) {
        // Buscar el rol en la BD (por defecto "Cliente" si no envían otro)
        Role defaultRole = roleRepository.findByNombre(user.getRol().getNombre())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));

        // Asignar el rol al usuario
        user.setRol(defaultRole);
        user.setPassword(passwordEncoder.encode(user.getPassword())); // Cifrar la contraseña

        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    
}
