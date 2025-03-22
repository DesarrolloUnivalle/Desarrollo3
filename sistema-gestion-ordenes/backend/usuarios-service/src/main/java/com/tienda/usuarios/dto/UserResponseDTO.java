package com.tienda.usuarios.dto;





import lombok.Data;

@Data
public class UserResponseDTO {
    private Long id;
    private String nombre;
    private String email;  
    private String rol;
}
