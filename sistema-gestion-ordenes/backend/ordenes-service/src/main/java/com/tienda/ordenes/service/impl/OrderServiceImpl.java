package com.tienda.ordenes.service.impl;

import com.tienda.ordenes.dto.OrderRequest;
import com.tienda.ordenes.dto.OrderResponse;
import com.tienda.ordenes.model.Order;
import com.tienda.ordenes.model.OrderItem;
import com.tienda.ordenes.model.OrderStatus;
import com.tienda.ordenes.repository.OrderRepository;
import com.tienda.ordenes.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    @Transactional
    public OrderResponse crearOrden(String userEmail, OrderRequest request) {
        Order order = new Order();
        order.setUsuarioId(13L); // TODO: Obtener el ID del usuario desde el email
        order.setFechaCreacion(LocalDateTime.now());
        order.setEstado(OrderStatus.PENDIENTE);
        
        List<OrderItem> items = request.getItems().stream()
            .map(itemRequest -> {
                OrderItem item = new OrderItem();
                item.setProductoId(itemRequest.getProductoId());
                item.setCantidad(itemRequest.getCantidad());
                item.setPrecio(itemRequest.getPrecio());
                item.setOrden(order);
                return item;
            })
            .collect(Collectors.toList());
            
        order.setDetalles(items);
        
        // Calcular el total antes de guardar
        Double total = calcularTotal(items);
        order.setTotal(total);
        
        Order savedOrder = orderRepository.save(order);
        
        return OrderResponse.builder()
            .id(savedOrder.getId())
            .status(savedOrder.getEstado())
            .fechaCreacion(savedOrder.getFechaCreacion())
            .total(savedOrder.getTotal())
            .items(savedOrder.getDetalles())
            .build();
    }

    @Override
    public List<OrderResponse> listarOrdenesPorUsuario(String userEmail) {
        return orderRepository.findByUsuarioId(13L).stream()
            .map(order -> OrderResponse.builder()
                .id(order.getId())
                .status(order.getEstado())
                .fechaCreacion(order.getFechaCreacion())
                .total(order.getTotal())
                .items(order.getDetalles())
                .build())
            .collect(Collectors.toList());
    }

    private Double calcularTotal(List<OrderItem> items) {
        return items.stream()
            .mapToDouble(item -> item.getPrecio() * item.getCantidad())
            .sum();
    }

    private Double obtenerPrecioProducto(Long productoId) {
        // TODO: Implementar obtención del precio del producto
        return 0.0; // Placeholder
    }
}