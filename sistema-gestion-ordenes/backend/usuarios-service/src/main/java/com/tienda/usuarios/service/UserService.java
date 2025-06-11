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
    
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    public UserResponseDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new UserResponseDTO(
            user.getUsuarioId(), // Cambiar para devolver "usuarioId"
            user.getNombre(),
            user.getEmail(),
            user.getRol().getNombre()
        );
    }

    // Metodo para obtener el usuario por el email
    public UserResponseDTO getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return new UserResponseDTO(
            user.getUsuarioId(), // Cambiar para devolver "usuarioId"
            user.getNombre(),
            user.getEmail(),
            user.getRol().getNombre()
        );
    }


    
    public UserResponseDTO registrarUsuario(UserRequestDTO request) {
        Optional<User> usuarioExistente = userRepository.findByEmail(request.email());
        if (usuarioExistente.isPresent()) {
            throw new IllegalArgumentException("El correo ya está registrado");
        }
        System.out.println("Buscando rol en la BD con nombre: [" + request.rol().getNombre() + "]");
        List<Role> roles = roleRepository.findAll();
        System.out.println("Roles en la base de datos: " + roles);
        
        // Buscar el rol en la base de datos
        Role role = roleRepository.findByNombre(request.rol().getNombre())
                .orElseThrow(() -> new IllegalArgumentException("El rol no existe en la base de datos: " + request.rol().getNombre()));

        
        System.out.println("Resultado de la búsqueda: " + role);
    
        // Crear usuario
        User user = new User();
        user.setNombre(request.nombre());
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password())); // Encriptar contraseña
        user.setRol(role);
        
        // Generar un usuario_id único
        Long maxUsuarioId = userRepository.findAll().stream()
                .map(User::getUsuarioId)
                .max(Long::compareTo)
                .orElse(0L);
        user.setUsuarioId(maxUsuarioId + 1);
    
        // Guardar en la base de datos
        user = userRepository.save(user);
    
        // Crear respuesta
        return new UserResponseDTO(
            user.getId(),
            user.getNombre(),
            user.getEmail(),
            user.getRol().getNombre()
        );
    }
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        userRepository.delete(user);
    }    
}
