package com.tienda.ordenes.dto;

import com.tienda.ordenes.model.OrderStatus;
import com.tienda.ordenes.model.OrderItem;
import java.time.LocalDateTime;
import java.util.List;

public class OrderResponse {
    private Long id;
    private OrderStatus status;
    private LocalDateTime fechaCreacion;
    private Double total;
    private List<OrderItem> items;

    public static OrderResponse fromEntity(com.tienda.ordenes.model.Order order) {
    return OrderResponse.builder()
            .id(order.getId())
            .status(order.getEstado())  // Usamos directamente el enum
            .fechaCreacion(order.getFechaCreacion())
            .total(order.getTotal())
            .items(order.getDetalles()) // Ya es una lista de OrderItem
            .build();
    }


    private OrderResponse(Builder builder) {
        this.id = builder.id;
        this.status = builder.status;
        this.fechaCreacion = builder.fechaCreacion;
        this.total = builder.total;
        this.items = builder.items;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Double getTotal() {
        return total;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public static class Builder {
        private Long id;
        private OrderStatus status;
        private LocalDateTime fechaCreacion;
        private Double total;
        private List<OrderItem> items;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder status(OrderStatus status) {
            this.status = status;
            return this;
        }

        public Builder fechaCreacion(LocalDateTime fechaCreacion) {
            this.fechaCreacion = fechaCreacion;
            return this;
        }

        public Builder total(Double total) {
            this.total = total;
            return this;
        }

        public Builder items(List<OrderItem> items) {
            this.items = items;
            return this;
        }

        public OrderResponse build() {
            return new OrderResponse(this);
        }
    }
}