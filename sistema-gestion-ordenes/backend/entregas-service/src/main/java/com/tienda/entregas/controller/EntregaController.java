package com.tienda.entregas.controller;

import com.tienda.entregas.dto.EntregaRequest;
import com.tienda.entregas.dto.EntregaResponse;
import com.tienda.entregas.service.EntregaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/entregas")
@SecurityRequirement(name = "bearerAuth")  // Autenticación para Swagger
public class EntregaController {

    private final EntregaService entregaService;

    public EntregaController(EntregaService entregaService) {
        this.entregaService = entregaService;
    }

    @Operation(summary = "Asignar una entrega a un repartidor")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntregaResponse> crearEntrega(
            @RequestBody @Valid EntregaRequest request) {
        EntregaResponse response = entregaService.crearEntrega(request);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Actualizar estado de una entrega")
    @PutMapping("/{id}/estado")
    @PreAuthorize("hasAnyRole('ADMIN', 'REPARTIDOR')")
    public ResponseEntity<EntregaResponse> actualizarEstadoEntrega(
            @PathVariable Long id,
            @RequestBody String nuevoEstado,
            @AuthenticationPrincipal Jwt jwt) {
        EntregaResponse response = entregaService.actualizarEstadoEntrega(id, nuevoEstado);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar entregas del repartidor actual")
    @GetMapping("/repartidor")
    @PreAuthorize("hasAnyRole('ADMIN', 'REPARTIDOR')")
    public ResponseEntity<List<EntregaResponse>> listarEntregasPorRepartidor(
            @AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getSubject();
        if (email == null) {
            throw new IllegalArgumentException("El token no contiene el email del repartidor");
        }
        List<EntregaResponse> response = entregaService.listarEntregasPorRepartidorEmail(email);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Listar entregas asociadas a una orden")
    @GetMapping("/orden/{ordenId}")
    public ResponseEntity<List<EntregaResponse>> listarEntregasPorOrden(
            @PathVariable Long ordenId) {
        List<EntregaResponse> response = entregaService.listarEntregasPorOrden(ordenId);
        return ResponseEntity.ok(response);
    }
}