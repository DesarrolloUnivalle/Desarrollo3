package com.tienda.ordenes.service;

public interface EmailService {
    void enviarConfirmacionPago(String email, String nombre, String orderId);
}

