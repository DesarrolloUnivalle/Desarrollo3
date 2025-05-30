package com.tienda.entregas.kafka.consumer;

import com.tienda.entregas.dto.PedidoCreadoEvent;
import com.tienda.entregas.service.EntregaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PedidoConsumer {

    private final EntregaService entregaService;

    @KafkaListener(topics = "pedidos-creados", groupId = "entregas-group")
    public void escucharPedidoCreado(PedidoCreadoEvent evento) {
        log.info("Recibido evento de pedido creado: {}", evento);
        entregaService.asignarRepartidorAutomatico(evento.getOrdenId(), evento.getDireccionEntrega());
    }
}