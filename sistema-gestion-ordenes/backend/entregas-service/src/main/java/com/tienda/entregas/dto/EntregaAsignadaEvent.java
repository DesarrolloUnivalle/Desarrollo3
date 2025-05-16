package com.tienda.entregas.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EntregaAsignadaEvent {
    private Long ordenId;
    private Long repartidorId;
    private String estado;
    private LocalDateTime fechaAsignacion;
}