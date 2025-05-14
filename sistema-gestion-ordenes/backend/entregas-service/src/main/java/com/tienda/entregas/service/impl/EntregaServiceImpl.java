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

        UserResponseDTO repartidor = usuarioClient.obtenerUsuarioPorId(request.getRepartidorId(), token);
        if (!repartidor.getRoles().contains("REPARTIDOR")) {
            throw new IllegalArgumentException("El usuario no es un repartidor válido.");
        }

        Entrega entrega = new Entrega();
        entrega.setOrdenId(request.getOrdenId());
        entrega.setRepartidorId(request.getRepartidorId());
        entrega.setEstado(EntregaStatus.ASIGNADA);
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

        EntregaStatus status = EntregaStatus.valueOf(nuevoEstado);
        entrega.setEstado(status);

        if (status == EntregaStatus.EN_CAMINO) {
            entrega.setFechaInicio(LocalDateTime.now());
        } else if (status == EntregaStatus.ENTREGADA) {
            entrega.setFechaEntrega(LocalDateTime.now());
            kafkaProducer.publicarEventoEntregaCompletada(entrega); // Notificar a órdenes
        }

        return mapToEntregaResponse(entregaRepository.save(entrega));
    }

    @Override
    @Transactional
    public void asignarRepartidorAutomatico(Long ordenId, String direccionEntrega) {
        Long repartidorIdHardcodeado = 69L;
        String token = obtenerToken();
        logger.info("Asignando repartidor automático {} para orden {}", repartidorIdHardcodeado, ordenId);

        UserResponseDTO repartidor = usuarioClient.obtenerUsuarioPorId(repartidorIdHardcodeado, token);
        if (!repartidor.getRoles().contains("REPARTIDOR")) {
            throw new IllegalStateException("El repartidor hardcodeado no es válido.");
        }

        Entrega entrega = new Entrega();
        entrega.setOrdenId(ordenId);
        entrega.setRepartidorId(repartidorIdHardcodeado);
        entrega.setEstado(EntregaStatus.ASIGNADA);
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
                .repartidorId(entrega.getRepartidorId())
                .estado(entrega.getEstado().name())
                .fechaAsignacion(entrega.getFechaAsignacion())
                .fechaInicio(entrega.getFechaInicio())
                .fechaEntrega(entrega.getFechaEntrega())
                .direccionEntrega(entrega.getDireccionEntrega())
                .build();
    }
}