package com.tienda.usuarios.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/repartidor")
public class RepartidorController {

    @GetMapping("/acceso")
    public String accesoRepartidor() {
        return "Hola, repartidor. Puedes ver esta ruta.";
    }
}
