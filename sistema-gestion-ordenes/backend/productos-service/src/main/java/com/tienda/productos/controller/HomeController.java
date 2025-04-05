package com.tienda.productos.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "Bienvenido al servicio de gestión de productos  🚀";
    }
    @GetMapping("/Inicio")
    public String Inicio() {
        return "¡El servicio de productos está funcionando! 🚀";
    }
}
