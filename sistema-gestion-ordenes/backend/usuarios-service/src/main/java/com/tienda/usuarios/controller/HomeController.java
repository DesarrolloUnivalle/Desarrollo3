package com.tienda.usuarios.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home() {
        return "Bienvenido al servicio de gestión de usuarios 🚀";
    }
    @GetMapping("/inicio")
    public String Inicio() {
        return "¡El servicio de usuarios está funcionando! 🚀";
    }
    @GetMapping("/public")
    public String publicEndpoint() {
        return "Este endpoint es público.";
    }

    @GetMapping("/admin")
    public String adminEndpoint() {
        return "Bienvenido, administrador.";
    }
}
