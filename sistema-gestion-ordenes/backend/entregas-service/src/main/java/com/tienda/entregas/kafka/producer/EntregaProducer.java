package com.tienda.entregas.kafka.producer;

import com.tienda.entregas.dto.EntregaAsignadaEvent;
import com.tienda.entregas.model.entity.Entrega;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EntregaProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void publicarEventoEntregaAsignada(Entrega entrega) {
        EntregaAsignadaEvent evento = EntregaAsignadaEvent.builder()
                .ordenId(entrega.getOrdenId())
                .repartidorId(entrega.getRepartidorId())
                .estado(entrega.getEstado().name())
                .fechaAsignacion(entrega.getFechaAsignacion())
                .build();

        kafkaTemplate.send("entregas-asignadas", evento);
        log.info("Evento de entrega asignada publicado: {}", evento);
    }

    public void publicarEventoEntregaCompletada(Entrega entrega) {
        EntregaAsignadaEvent evento = EntregaAsignadaEvent.builder()
                .ordenId(entrega.getOrdenId())
                .repartidorId(entrega.getRepartidorId())
                .estado(entrega.getEstado().name())
                .fechaAsignacion(entrega.getFechaAsignacion())
                .build();

        kafkaTemplate.send("entregas-completadas", evento);
        log.info("Evento de entrega completada publicado: {}", evento);
    }
}