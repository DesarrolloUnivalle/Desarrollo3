package com.tienda.usuarios.dto;

import com.tienda.usuarios.model.Role;

import lombok.Data;

@Data
public class UserRequestDTO {
    private String nombre;
    private String email;  
    private String password;
    private Role rol;  
}
