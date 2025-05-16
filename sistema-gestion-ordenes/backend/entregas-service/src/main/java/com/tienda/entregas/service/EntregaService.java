package com.tienda.entregas.service;

import com.tienda.entregas.dto.EntregaRequest;
import com.tienda.entregas.dto.EntregaResponse;
import java.util.List;

public interface EntregaService {
    EntregaResponse crearEntrega(EntregaRequest request);
    EntregaResponse actualizarEstadoEntrega(Long entregaId, String nuevoEstado);
    List<EntregaResponse> listarEntregasPorRepartidor(Long repartidorId);
    List<EntregaResponse> listarEntregasPorRepartidorEmail(String email);
    List<EntregaResponse> listarEntregasPorOrden(Long ordenId);
    void asignarRepartidorAutomatico(Long ordenId, String direccionEntrega);
}