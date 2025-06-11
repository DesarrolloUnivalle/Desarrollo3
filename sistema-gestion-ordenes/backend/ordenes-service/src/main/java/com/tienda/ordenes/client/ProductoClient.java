package com.tienda.ordenes.client;

import com.tienda.ordenes.model.OrderItem;
import com.tienda.ordenes.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.http.ResponseEntity;
import java.util.List;

@FeignClient(name = "productos-service", url = "${productos-service.url}", configuration = FeignConfig.class)
public interface ProductoClient {
    
    @PostMapping("/api/productos/validar-stock")
    ResponseEntity<Void> validarStock(@RequestBody List<OrderItem> items);

    @PutMapping("/api/productos/{productoId}/stock")
    ResponseEntity<Void> actualizarStock(@PathVariable("productoId") Long id, @RequestParam("cantidad") Integer cantidad);
} 