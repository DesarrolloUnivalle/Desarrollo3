package com.tienda.ordenes.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "detalle_orden")
public class OrderItem {
    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "orden_id")
    private Order orden;

    @Column(name = "producto_id")
    private String productoId;

    @Column(name = "cantidad")
    @NotNull
    private Integer cantidad;

    @Column(name = "precio_id")
    private String precioId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Order getOrden() {
        return orden;
    }

    public void setOrden(Order orden) {
        this.orden = orden;
    }

    public String getProductoId() {
        return productoId;
    }

    public void setProductoId(String productoId) {
        this.productoId = productoId;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getPrecioId() {
        return precioId;
    }

    public void setPrecioId(String precioId) {
        this.precioId = precioId;
    }
}