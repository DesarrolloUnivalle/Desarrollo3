package com.tienda.usuarios.dto;

import com.tienda.usuarios.model.Role;

public record UserRequestDTO(String nombre, String email, String password, Role rol) {}
