package com.tienda.ordenes.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.tienda.ordenes.client.UsuarioClient;
import com.tienda.ordenes.dto.ConfirmarPagoRequest;
import com.tienda.ordenes.dto.OrderRequest;
import com.tienda.ordenes.dto.OrderResponse;
import com.tienda.ordenes.dto.UserResponseDTO;
import com.tienda.ordenes.repository.OrderRepository;
import com.tienda.ordenes.service.EmailService;
import com.tienda.ordenes.service.OrderService;
import com.tienda.ordenes.service.impl.OrderServiceImpl;
import com.tienda.ordenes.model.Order;
import com.tienda.ordenes.model.OrderStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/ordenes")
@SecurityRequirement(name = "bearerAuth")  // Para Swagger/OpenAPI
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final EmailService emailService;
    private final OrderRepository orderRepository;
    private final UsuarioClient usuarioClient;


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
    
    @PostMapping("/pago-exitoso")
    public ResponseEntity<OrderResponse> confirmarPago(@RequestBody ConfirmarPagoRequest request) {
        Long orderId = request.getOrderId();
        String emailUsuario = request.getEmailUsuario();

        

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Orden no encontrada"));
        if (order.getEstado() != OrderStatus.PENDIENTE) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "La orden #" + orderId + " no puede ser procesada porque su estado actual es: " + order.getEstado().getValue()
            );
        }
        System.out.println("Confirmando pago para la orden ID: " + orderId + " con el email " + emailUsuario);    

        UserResponseDTO usuario = usuarioClient.obtenerUsuarioPorEmail(emailUsuario);

        // 

        ((OrderServiceImpl) orderService).procesarPago(order, usuario);

        OrderResponse response = OrderResponse.fromEntity(order);

        return ResponseEntity.ok(response);
    }   
}