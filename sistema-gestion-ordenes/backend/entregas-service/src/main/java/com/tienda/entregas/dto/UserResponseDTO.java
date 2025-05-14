package com.tienda.entregas.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class UserResponseDTO {
    @JsonProperty("usuarioId")
    private Long usuarioId;

    private String nombre;

    @JsonProperty("email")
    private String correo;

    private List<String> roles; // Lista de roles (ej: ["REPARTIDOR", "CLIENTE"])

    // Getters y Setters
    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "usuarioId=" + usuarioId +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", roles=" + roles +
                '}';
    }
}