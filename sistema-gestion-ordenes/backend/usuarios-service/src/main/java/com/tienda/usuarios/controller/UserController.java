package com.tienda.usuarios.controller;


import java.util.List;

import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*;

import com.tienda.usuarios.dto.UserResponseDTO;
import com.tienda.usuarios.model.User;
import com.tienda.usuarios.service.UserService;

import com.tienda.usuarios.security.JwtAuthFilter;
import com.tienda.usuarios.security.JwtUtil;

import org.springframework.security.core.Authentication;


import com.tienda.usuarios.security.CustomUserDetails;

import org.springframework.security.access.prepost.PreAuthorize;


@RestController 
@RequestMapping("/usuarios")
public class UserController {
     private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @GetMapping("/{usuario_id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("usuario_id") Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/{id}/internal")
    public ResponseEntity<UserResponseDTO> getUserByIdInternal(@PathVariable("id") Long id) {
        UserResponseDTO user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/ping")
    public String ping() {
        return "¡El servicio de usuarios está funcionando! 🚀";
    }
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    
    @GetMapping("/email/{email}")
    public ResponseEntity<UserResponseDTO> getUserByEmail(@PathVariable("email") String email) {
        UserResponseDTO user = userService.getUserByEmail(email);  // Ahora que existe el método en el servicio
        return ResponseEntity.ok(user);
    }


    @DeleteMapping("/{usuario_id}")
    public ResponseEntity<Void> deleteUser(@PathVariable("usuario_id") Long id, Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        boolean isAdmin = userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        
        if (!isAdmin && !userDetails.getId().equals(id)) {
            return ResponseEntity.status(403).build(); // Forbidden
        }

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    
    
}
