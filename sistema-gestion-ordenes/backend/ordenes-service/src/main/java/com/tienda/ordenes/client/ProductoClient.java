package com.tienda.ordenes.client;

import com.tienda.ordenes.model.OrderItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "productos-service", url = "http://localhost:8081")
public interface ProductoClient {
    
    @PostMapping("/api/productos/validar-stock")
    void validarStock(@RequestBody List<OrderItem> items);

    @PutMapping("/api/productos/{productoId}/stock")
    void actualizarStock(@PathVariable("productoId") Long id, @RequestParam("cantidad") Integer cantidad);
} 