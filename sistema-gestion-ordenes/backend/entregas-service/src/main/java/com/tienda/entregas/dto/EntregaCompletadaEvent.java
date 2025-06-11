package com.tienda.entregas.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class EntregaCompletadaEvent {
    private Long entregaId;
    private Long ordenId;
    private LocalDateTime fechaEntrega;
}