package com.tienda.entregas.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class EntregaResponse {
    private Long id;
    private Long ordenId;
    private Long pedidoId;
    private Long repartidorId;
    private String estado;
    private LocalDateTime fechaAsignacion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaEntrega;
    private String direccionEntrega;
}