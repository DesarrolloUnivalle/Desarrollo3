package com.tienda.usuarios.controller;

import java.util.List;

import org.springframework.http.ResponseEntity; 
import org.springframework.web.bind.annotation.*;

import com.tienda.usuarios.dto.UserResponseDTO;
import com.tienda.usuarios.model.User;
import com.tienda.usuarios.service.UserService;

@RestController 
@RequestMapping("/usuarios")
public class UserController {
     private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registro")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        User newUser = userService.registerUser(user);
        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/{usuario_id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable("usuario_id") Long id) {
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
    
}
