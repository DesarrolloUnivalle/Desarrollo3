package com.tienda.usuarios.service;


import com.tienda.usuarios.dto.UserRequestDTO;
import com.tienda.usuarios.dto.UserResponseDTO;
import com.tienda.usuarios.model.Role;
import com.tienda.usuarios.model.User;
import com.tienda.usuarios.repository.RoleRepository;
import com.tienda.usuarios.repository.UserRepository;

import java.util.List;
import java.util.Optional;

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
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
    
    public UserResponseDTO registrarUsuario(UserRequestDTO request) {
        Optional<User> usuarioExistente = userRepository.findByEmail(request.getEmail());
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
    
        // Buscar el rol en la base de datos
        Role role = roleRepository.findByNombre(request.getRol().getNombre())
                .orElseThrow(() -> new IllegalArgumentException("🚨 El rol no existe en la base de datos: " + request.getRol().getNombre()));
    
        // Crear usuario
        User user = new User();
        user.setNombre(request.getNombre());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encriptar contraseña
        user.setRol(role);
    
        // Guardar en la base de datos
        user = userRepository.save(user);
    
        // ✅ Crear respuesta asegurándonos de que el rol se maneja como String
        UserResponseDTO response = new UserResponseDTO();
        response.setId(user.getId());
        response.setNombre(user.getNombre());
        response.setEmail(user.getEmail());
        response.setRol(user.getRol().getNombre()); // 🔥 Extraer solo el nombre del rol
    
        return response;
    }
    
}
