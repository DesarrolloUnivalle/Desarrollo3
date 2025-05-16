package com.tienda.entregas.service.impl;

import com.tienda.entregas.client.UsuarioClient;
import com.tienda.entregas.dto.EntregaRequest;
import com.tienda.entregas.dto.EntregaResponse;
import com.tienda.entregas.dto.UserResponseDTO;
import com.tienda.entregas.exception.EntregaNotFoundException;
import com.tienda.entregas.exception.RolInvalidoException;
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

        UserResponseDTO repartidor = null; // Inicializar con null para evitar error de compilación
        Long repartidorId;
        
        try {
            repartidor = usuarioClient.obtenerUsuarioPorId(request.getRepartidorId(), token);
            logger.info("Información del repartidor: {}", repartidor);
            
            // Validamos que el usuario tenga el rol de REPARTIDOR (insensible a mayúsculas/minúsculas)
            if (!"REPARTIDOR".equalsIgnoreCase(repartidor.getRol()) && !"Repartidor".equalsIgnoreCase(repartidor.getRol())) {
                String mensaje = "El usuario con ID " + request.getRepartidorId() + 
                    " no es un repartidor. Rol actual: " + repartidor.getRol();
                logger.error(mensaje);
                throw new RolInvalidoException(mensaje);
            }
            
            // Usar el usuarioId devuelto por el servicio de usuarios
            repartidorId = repartidor.getUsuarioId();
            logger.info("Usando el usuarioId {} devuelto por el servicio de usuarios", repartidorId);
        } catch (feign.FeignException e) {
            logger.error("Error al comunicarse con el servicio de usuarios: {}", e.getMessage());
            throw new IllegalArgumentException("No se pudo verificar la información del repartidor. Servicio de usuarios no disponible.");
        } catch (Exception e) {
            if (e instanceof RolInvalidoException) {
                throw e;
            }
            logger.error("Error inesperado al obtener información del repartidor: {}", e.getMessage());
            throw new IllegalArgumentException("Error al verificar la información del repartidor: " + e.getMessage());
        }

        Entrega entrega = new Entrega();
        entrega.setOrdenId(request.getOrdenId());
        entrega.setPedidoId(request.getOrdenId());
        entrega.setRepartidorId(repartidorId);
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

        UserResponseDTO repartidor = null; // Inicializar con null para evitar error de compilación
        Long repartidorId;
        
        try {
            repartidor = usuarioClient.obtenerUsuarioPorId(repartidorIdHardcodeado, token);
            
            // Validamos que el usuario tenga el rol de REPARTIDOR (insensible a mayúsculas/minúsculas)
            if (!"REPARTIDOR".equalsIgnoreCase(repartidor.getRol()) && !"Repartidor".equalsIgnoreCase(repartidor.getRol())) {
                String mensaje = "El usuario con ID " + repartidorIdHardcodeado + 
                    " no es un repartidor. Rol actual: " + repartidor.getRol();
                logger.error(mensaje);
                throw new RolInvalidoException(mensaje);
            }
            
            // Usar el usuarioId devuelto por el servicio de usuarios
            repartidorId = repartidor.getUsuarioId();
            logger.info("Usando el usuarioId {} devuelto por el servicio de usuarios", repartidorId);
        } catch (feign.FeignException e) {
            logger.error("Error al comunicarse con el servicio de usuarios: {}", e.getMessage());
            logger.warn("Continuando con el ID proporcionado debido a que el servicio de usuarios no está disponible");
            repartidorId = repartidorIdHardcodeado; // Usamos el ID hardcodeado en caso de error
        } catch (Exception e) {
            if (e instanceof RolInvalidoException) {
                throw e;
            }
            logger.error("Error inesperado al obtener información del repartidor: {}", e.getMessage());
            throw new IllegalArgumentException("Error al verificar la información del repartidor: " + e.getMessage());
        }

        Entrega entrega = new Entrega();
        entrega.setOrdenId(ordenId);
        entrega.setPedidoId(ordenId);
        entrega.setRepartidorId(repartidorId);
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

    @Override
    public List<EntregaResponse> listarEntregasPorRepartidorEmail(String email) {
        String token = obtenerToken();
        UserResponseDTO usuario = usuarioClient.obtenerUsuarioPorEmail(email, token);
        if (usuario == null) {
            throw new IllegalArgumentException("No se encontró el repartidor con email: " + email);
        }
        return listarEntregasPorRepartidor(usuario.getUsuarioId());
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