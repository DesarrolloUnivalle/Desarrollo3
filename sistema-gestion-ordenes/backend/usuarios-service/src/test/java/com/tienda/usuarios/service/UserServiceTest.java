package com.tienda.usuarios.service;

import com.tienda.usuarios.dto.UserRequestDTO;
import com.tienda.usuarios.dto.UserResponseDTO;
import com.tienda.usuarios.model.Role;
import com.tienda.usuarios.model.User;
import com.tienda.usuarios.repository.RoleRepository;
import com.tienda.usuarios.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

      @Mock
    private RoleRepository roleRepository;  // ¡Aquí se agrega la anotación @Mock!


    @Mock
    private BCryptPasswordEncoder passwordEncoder;  // Aseguramos que se inicializa correctamente

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    
        Role adminRole = new Role();
        adminRole.setId(1);
        adminRole.setNombre("Admin");
    
        when(roleRepository.findAll()).thenReturn(List.of(adminRole));
        when(roleRepository.findByNombre("Admin")).thenReturn(Optional.of(adminRole));
    }
}
