package com.tienda.ordenes.model;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class OrderStatusConverter implements AttributeConverter<OrderStatus, String> {
    
    @Override
    public String convertToDatabaseColumn(OrderStatus status) {
        if (status == null) {
            return null;
        }
        // Aseguramos que el valor se envíe exactamente como lo espera la base de datos
        switch (status) {
            case PENDIENTE:
                return "Pendiente";
            case PAGADA:
                return "Pagada";
            case ENVIADA:
                return "Enviada";
            case ENTREGADA:
                return "Entregada";
            case CANCELADA:
                return "Cancelada";
            default:
                throw new IllegalArgumentException("Unknown status: " + status);
        }
    }
    
    @Override
    public OrderStatus convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        switch (dbData) {
            case "Pendiente":
                return OrderStatus.PENDIENTE;
            case "Pagada":
                return OrderStatus.PAGADA;
            case "Enviada":
                return OrderStatus.ENVIADA;
            case "Entregada":
                return OrderStatus.ENTREGADA;
            case "Cancelada":
                return OrderStatus.CANCELADA;
            default:
                throw new IllegalArgumentException("Unknown status: " + dbData);
        }
    }
} 