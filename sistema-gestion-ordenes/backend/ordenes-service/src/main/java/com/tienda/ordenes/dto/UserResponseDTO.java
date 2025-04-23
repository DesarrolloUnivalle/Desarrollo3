package com.tienda.ordenes.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class UserResponseDTO {
    @JsonProperty("usuarioId") // Mapea el campo "usuarioId" del JSON
    private Long usuarioId;

    private String nombre;

    @JsonProperty("email")
    private String correo;

    private String rol;

    // Getters y setters
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

    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return "UserResponseDTO{" +
                "usuarioId=" + usuarioId +
                ", nombre='" + nombre + '\'' +
                ", correo='" + correo + '\'' +
                ", rol='" + rol + '\'' +
                '}';
    }
}