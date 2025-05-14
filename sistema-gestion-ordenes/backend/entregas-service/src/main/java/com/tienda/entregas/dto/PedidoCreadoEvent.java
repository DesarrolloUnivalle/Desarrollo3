package com.tienda.entregas.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PedidoCreadoEvent {
    private Long ordenId;
    private String direccionEntrega;
    private LocalDateTime fechaCreacion;
}