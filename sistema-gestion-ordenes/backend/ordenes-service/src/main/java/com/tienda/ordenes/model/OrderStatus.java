package com.tienda.ordenes.model;

public enum OrderStatus {
    PENDIENTE("Pendiente"),
    PAGADA("Pagada"),
    ENVIADA("Enviada"),
    ENTREGADA("Entregada"),
    CANCELADA("Cancelada");

    private final String value;

    OrderStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}