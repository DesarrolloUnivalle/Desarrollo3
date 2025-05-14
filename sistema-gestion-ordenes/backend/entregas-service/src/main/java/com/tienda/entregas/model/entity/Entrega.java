package com.tienda.entregas.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "entregas")
public class Entrega {

    public enum EntregaStatus {
        Asignado("Asignado"), 
        En_camino("En camino"), 
        Entregado("Entregado"), 
        Cancelado("Cancelado");
        
        private final String valor;
        
        EntregaStatus(String valor) {
            this.valor = valor;
        }
        
        public String getValor() {
            return valor;
        }
        
        @Override
        public String toString() {
            return valor;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "orden_id")
    private Long ordenId;  // Referencia al ID de la orden en el servicio de órdenes

    @NotNull
    @Column(name = "pedido_id")
    private Long pedidoId;  // Campo requerido por la base de datos

    @NotNull
    @Column(name = "repartidor_id")
    private Long repartidorId;  // ID del usuario con rol "Repartidor"

    @Column(name = "estado")
    private String estado;

    @Column(name = "fecha_entrega")
    private LocalDateTime fechaEntrega;

    @Column(name = "ubicacion_actual")
    private String ubicacionActual;  // Coordenadas o dirección

    @NotNull
    @Column(name = "direccion_entrega")
    private String direccionEntrega;

    @Column(name = "fecha_asignacion")
    private LocalDateTime fechaAsignacion;

    @Column(name = "fecha_inicio")
    private LocalDateTime fechaInicio;

    @Column(name = "observaciones", length = 500)
    private String observaciones;

    // Constructor, Getters y Setters
    public Entrega() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrdenId() {
        return ordenId;
    }

    public void setOrdenId(Long ordenId) {
        this.ordenId = ordenId;
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public Long getRepartidorId() {
        return repartidorId;
    }

    public void setRepartidorId(Long repartidorId) {
        this.repartidorId = repartidorId;
    }

    public EntregaStatus getEstado() {
        for (EntregaStatus status : EntregaStatus.values()) {
            if (status.getValor().equals(estado)) {
                return status;
            }
        }
        return null;
    }

    public void setEstado(EntregaStatus estado) {
        this.estado = estado.getValor();
    }

    public LocalDateTime getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getUbicacionActual() {
        return ubicacionActual;
    }

    public void setUbicacionActual(String ubicacionActual) {
        this.ubicacionActual = ubicacionActual;
    }

    public String getDireccionEntrega() {
        return direccionEntrega;
    }

    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public LocalDateTime getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(LocalDateTime fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}