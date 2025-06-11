package com.tienda.entregas.kafka;

import com.tienda.entregas.model.entity.Entrega;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class KafkaProducer {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    public void publicarEventoEntregaAsignada(Entrega entrega) {
        kafkaTemplate.send("entregas-asignadas", entrega.getId().toString(), entrega);
    }
    
    public void publicarEventoEntregaCompletada(Entrega entrega) {
        kafkaTemplate.send("entregas-completadas", entrega.getId().toString(), entrega);
    }
} 