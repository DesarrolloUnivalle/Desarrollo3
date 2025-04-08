package com.tienda.productos.dto;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
    
    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String nombre;
    
    @NotBlank(message = "La descripción no puede estar vacía")
    private String descripcion;
    
    @NotNull(message = "El precio es obligatorio")
    @Min(value = 0, message = "El precio no puede ser negativo")
    private Double precio;
    
    @NotNull(message = "La cantidad en stock es obligatoria")
    @Min(value = 0, message = "La cantidad en stock no puede ser negativa")
    private Integer stock;
    
    @NotNull(message = "El ID de la categoría es obligatorio")
    private Long categoriaId;   
}
