package com.tienda.ordenes.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class OrderItemDTO {
    @NotNull(message = "El ID del producto es obligatorio")
    private Long productoId;  // ID del producto

    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    private int cantidad;
}