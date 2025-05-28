package com.tienda.productos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryDTO {

    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long id;    
}
