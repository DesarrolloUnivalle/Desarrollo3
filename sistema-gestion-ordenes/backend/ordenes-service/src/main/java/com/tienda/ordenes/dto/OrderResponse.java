package com.tienda.ordenes.dto;

import com.tienda.ordenes.model.OrderStatus;
import java.time.LocalDateTime;

public class OrderResponse {
    private String id;
    private OrderStatus status;
    private LocalDateTime fechaCreacion;
    private Double total;

    private OrderResponse(Builder builder) {
        this.id = builder.id;
        this.status = builder.status;
        this.fechaCreacion = builder.fechaCreacion;
        this.total = builder.total;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getId() {
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

    public static class Builder {
        private String id;
        private OrderStatus status;
        private LocalDateTime fechaCreacion;
        private Double total;

        public Builder id(String id) {
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

        public OrderResponse build() {
            return new OrderResponse(this);
        }
    }
}