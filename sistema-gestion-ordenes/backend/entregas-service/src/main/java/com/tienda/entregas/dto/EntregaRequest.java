package com.tienda.entregas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregaRequest {
    private Long ordenId;
    private Long repartidorId;
    private String direccionEntrega;
}