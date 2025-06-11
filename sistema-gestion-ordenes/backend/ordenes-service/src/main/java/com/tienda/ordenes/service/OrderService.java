package com.tienda.ordenes.service;

import java.util.List;

import com.tienda.ordenes.dto.OrderRequest;
import com.tienda.ordenes.dto.OrderResponse;

public interface OrderService {
    OrderResponse crearOrden(String userEmail, OrderRequest request);

    List<OrderResponse> listarOrdenesPorUsuario(String userEmail);
}