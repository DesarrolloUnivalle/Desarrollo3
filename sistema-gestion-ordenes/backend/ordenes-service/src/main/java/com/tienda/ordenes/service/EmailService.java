package com.tienda.ordenes.service;

import com.tienda.ordenes.dto.OrderResponse;
import com.tienda.ordenes.dto.UserResponseDTO;
import com.tienda.ordenes.model.Order;


public interface EmailService {
    void enviarConfirmacionPago(String email, String nombre, String orderId, OrderResponse orden);
    OrderResponse procesarPago(Order order, UserResponseDTO usuario);
}

