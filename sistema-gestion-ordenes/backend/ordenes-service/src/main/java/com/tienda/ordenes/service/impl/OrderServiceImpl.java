package com.tienda.ordenes.service.impl;

import com.tienda.ordenes.client.ProductoClient;
import com.tienda.ordenes.dto.OrderRequest;
import com.tienda.ordenes.dto.OrderResponse;
import com.tienda.ordenes.model.Order;
import com.tienda.ordenes.model.OrderItem;
import com.tienda.ordenes.model.OrderStatus;
import com.tienda.ordenes.repository.OrderRepository;
import com.tienda.ordenes.service.OrderService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductoClient productoClient;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public OrderServiceImpl(OrderRepository orderRepository, 
                          ProductoClient productoClient,
                          KafkaTemplate<String, Object> kafkaTemplate) {
        this.orderRepository = orderRepository;
        this.productoClient = productoClient;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public OrderResponse crearOrden(String usuarioId, OrderRequest request) {
        // Validar stock
        productoClient.validarStock(request.getItems());

        // Crear orden
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setUsuarioId(usuarioId);
        order.setDetalles(request.getItems());
        order.setEstado(OrderStatus.CREADA);
        order.setFechaCreacion(LocalDateTime.now());
        order.setTotal(calcularTotal(request.getItems()));

        // Guardar orden
        order = orderRepository.save(order);

        // Publicar evento
        kafkaTemplate.send("ordenes", Map.of(
            "orderId", order.getId(),
            "status", order.getEstado().name()
        ));

        // Construir respuesta
        return OrderResponse.builder()
            .id(order.getId())
            .status(order.getEstado())
            .fechaCreacion(order.getFechaCreacion())
            .total(order.getTotal())
            .build();
    }

    @Override
    public List<OrderResponse> listarOrdenesPorUsuario(String usuarioId) {
        return orderRepository.findByUsuarioId(usuarioId).stream()
            .map(order -> OrderResponse.builder()
                .id(order.getId())
                .status(order.getEstado())
                .fechaCreacion(order.getFechaCreacion())
                .total(order.getTotal())
                .build())
            .collect(Collectors.toList());
    }

    private Double calcularTotal(List<OrderItem> items) {
        // TODO: Implementar cálculo del total basado en los precios de los productos
        return items.stream()
            .mapToDouble(item -> item.getCantidad() * obtenerPrecioProducto(item.getProductoId()))
            .sum();
    }

    private Double obtenerPrecioProducto(String productoId) {
        // TODO: Implementar obtención del precio del producto
        return 0.0;
    }
}