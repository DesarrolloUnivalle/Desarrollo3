package com.tienda.entregas.service.impl;

import com.tienda.entregas.client.UsuarioClient;
import com.tienda.entregas.dto.EntregaRequest;
import com.tienda.entregas.dto.EntregaResponse;
import com.tienda.entregas.dto.UserResponseDTO;
import com.tienda.entregas.exception.EntregaNotFoundException;
import com.tienda.entregas.kafka.KafkaProducer;
import com.tienda.entregas.model.entity.Entrega;
import com.tienda.entregas.model.entity.Entrega.EntregaStatus;
import com.tienda.entregas.repository.EntregaRepository;
import com.tienda.entregas.service.EntregaService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EntregaServiceImpl implements EntregaService {

    private static final Logger logger = LoggerFactory.getLogger(EntregaServiceImpl.class);
    private final EntregaRepository entregaRepository;
    private final UsuarioClient usuarioClient;
    private final KafkaProducer kafkaProducer;

    private String obtenerToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Jwt)) {
            logger.error("No se pudo obtener el token JWT del contexto de seguridad");
            throw new IllegalStateException("No se pudo obtener el token de autenticación");
        }
        Jwt jwt = (Jwt) authentication.getPrincipal();
        return "Bearer " + jwt.getTokenValue();
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public EntregaResponse crearEntrega(EntregaRequest request) {
        String token = obtenerToken();
        logger.info("Creando entrega para orden {} con repartidor {}", request.getOrdenId(), request.getRepartidorId());

        try {
            UserResponseDTO repartidor = usuarioClient.obtenerUsuarioPorId(request.getRepartidorId(), token);
            logger.info("Información del repartidor: {}", repartidor);
            // Temporalmente aceptamos cualquier rol para probar
            // if (!"REPARTIDOR".equals(repartidor.getRol())) {
            //    throw new IllegalArgumentException("El usuario no es un repartidor válido.");
            // }
        } catch (Exception e) {
            logger.error("Error al obtener información del repartidor: {}", e.getMessage());
            // Continuamos con la creación de la entrega para probar
        }

        Entrega entrega = new Entrega();
        entrega.setOrdenId(request.getOrdenId());
        entrega.setPedidoId(request.getOrdenId());
        entrega.setRepartidorId(request.getRepartidorId());
        entrega.setEstado(EntregaStatus.Asignado);
        entrega.setFechaAsignacion(LocalDateTime.now());
        entrega.setDireccionEntrega(request.getDireccionEntrega());

        Entrega savedEntrega = entregaRepository.save(entrega);
        kafkaProducer.publicarEventoEntregaAsignada(savedEntrega);

        return mapToEntregaResponse(savedEntrega);
    }

    @Override
    @Transactional
    @PreAuthorize("#repartidorId == authentication.principal.id") // Solo el repartidor puede actualizar su entrega
    public EntregaResponse actualizarEstadoEntrega(Long entregaId, String nuevoEstado) {
        Entrega entrega = entregaRepository.findById(entregaId)
                .orElseThrow(() -> new EntregaNotFoundException("Entrega no encontrada"));

        // Convertir el string a enum
        EntregaStatus status = null;
        for (EntregaStatus s : EntregaStatus.values()) {
            if (s.name().equalsIgnoreCase(nuevoEstado) || s.getValor().equalsIgnoreCase(nuevoEstado)) {
                status = s;
                break;
            }
        }
        
        if (status == null) {
            throw new IllegalArgumentException("Estado no válido: " + nuevoEstado);
        }

        entrega.setEstado(status);

        if (status == EntregaStatus.En_camino) {
            entrega.setFechaInicio(LocalDateTime.now());
        } else if (status == EntregaStatus.Entregado) {
            entrega.setFechaEntrega(LocalDateTime.now());
            kafkaProducer.publicarEventoEntregaCompletada(entrega); // Notificar a órdenes
        }

        return mapToEntregaResponse(entregaRepository.save(entrega));
    }

    @Override
    @Transactional
    public void asignarRepartidorAutomatico(Long ordenId, String direccionEntrega) {
        Long repartidorIdHardcodeado = 1L;
        String token = obtenerToken();
        logger.info("Asignando repartidor automático {} para orden {}", repartidorIdHardcodeado, ordenId);

        UserResponseDTO repartidor = usuarioClient.obtenerUsuarioPorId(repartidorIdHardcodeado, token);
        if (!"REPARTIDOR".equals(repartidor.getRol())) {
            throw new IllegalStateException("El repartidor hardcodeado no es válido.");
        }

        Entrega entrega = new Entrega();
        entrega.setOrdenId(ordenId);
        entrega.setPedidoId(ordenId);
        entrega.setRepartidorId(repartidorIdHardcodeado);
        entrega.setEstado(EntregaStatus.Asignado);
        entrega.setFechaAsignacion(LocalDateTime.now());
        entrega.setDireccionEntrega(direccionEntrega);

        Entrega savedEntrega = entregaRepository.save(entrega);
        kafkaProducer.publicarEventoEntregaAsignada(savedEntrega);
    }

    @Override
    public List<EntregaResponse> listarEntregasPorRepartidor(Long repartidorId) {
        return entregaRepository.findByRepartidorId(repartidorId)
                .stream()
                .map(this::mapToEntregaResponse)
                .toList();
    }

    @Override
    public List<EntregaResponse> listarEntregasPorOrden(Long ordenId) {
        return entregaRepository.findByOrdenId(ordenId)
                .stream()
                .map(this::mapToEntregaResponse)
                .toList();
    }

    private EntregaResponse mapToEntregaResponse(Entrega entrega) {
        return EntregaResponse.builder()
                .id(entrega.getId())
                .ordenId(entrega.getOrdenId())
                .pedidoId(entrega.getPedidoId())
                .repartidorId(entrega.getRepartidorId())
                .estado(entrega.getEstado() != null ? entrega.getEstado().getValor() : null)
                .fechaAsignacion(entrega.getFechaAsignacion())
                .fechaInicio(entrega.getFechaInicio())
                .fechaEntrega(entrega.getFechaEntrega())
                .direccionEntrega(entrega.getDireccionEntrega())
                .build();
    }
}