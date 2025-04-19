package com.tienda.ordenes.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tienda.ordenes.dto.OrderRequest;
import com.tienda.ordenes.dto.OrderResponse;
import com.tienda.ordenes.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ordenes")
@SecurityRequirement(name = "bearerAuth")  // Para Swagger/OpenAPI
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Crear una nueva orden")
    @PostMapping
    public ResponseEntity<OrderResponse> crearOrden(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid OrderRequest request) {
        String usuarioId = jwt.getSubject();
        OrderResponse response = orderService.crearOrden(usuarioId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> listarOrdenesPorUsuario(
            @AuthenticationPrincipal Jwt jwt) {
        String usuarioId = jwt.getSubject();
        List<OrderResponse> response = orderService.listarOrdenesPorUsuario(usuarioId);
        return ResponseEntity.ok(response);
    }
}