package com.tienda.ordenes.service.impl;

import com.tienda.ordenes.dto.OrderResponse;
import com.tienda.ordenes.dto.UserResponseDTO;
import com.tienda.ordenes.service.EmailService;
import com.tienda.ordenes.model.Order;
import com.tienda.ordenes.model.OrderStatus;
import com.tienda.ordenes.repository.OrderRepository;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;
    private final OrderRepository orderRepository;
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void enviarConfirmacionPago(String email, String nombre, String orderId, OrderResponse orden) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

            helper.setTo(email);
            helper.setSubject("Confirmación de Pago");

            String cuerpo = String.format(
                "Hola %s,\n\nTu pago para la orden #%s ha sido recibido exitosamente.\n\nResumen de la orden:\n\n%s\n\n¡Gracias por tu compra!",
                nombre,
                orderId,
                generarResumenJson(orden)
            );

            helper.setText(cuerpo, false);
            mailSender.send(mensaje);
            logger.info("Correo enviado exitosamente a {}", email);

        } catch (MessagingException e) {
            logger.error("Error al enviar el correo: ", e);
        }
    }
    private String generarResumenJson(OrderResponse orden) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append(String.format("  \"id\": %d,\n", orden.getId()));
        sb.append(String.format("  \"status\": \"%s\",\n", orden.getStatus().getValue()));
        sb.append(String.format("  \"fechaCreacion\": \"%s\",\n", orden.getFechaCreacion()));
        sb.append(String.format("  \"total\": %.2f,\n", orden.getTotal()));
        sb.append("  \"items\": [\n");

        for (int i = 0; i < orden.getItems().size(); i++) {
            var item = orden.getItems().get(i);
            sb.append("    {\n");
            sb.append(String.format("      \"id\": %d,\n", item.getId()));
            sb.append(String.format("      \"producto\": \"Producto A\",\n")); // Ajustar si tienes nombre real
            sb.append(String.format("      \"cantidad\": %d,\n", item.getCantidad()));
            sb.append(String.format("      \"precio\": %.2f\n", item.getPrecio()));
            sb.append(i < orden.getItems().size() - 1 ? "    },\n" : "    }\n");
        }

        sb.append("  ]\n");
        sb.append("}");

        return sb.toString();
    }


    @Override
    public OrderResponse procesarPago(Order order, UserResponseDTO usuario) {
        if (order.getEstado() == OrderStatus.PAGADA) {
            return OrderResponse.fromEntity(order);
        }

        order.setEstado(OrderStatus.PAGADA);
        orderRepository.save(order);

        OrderResponse respuesta = OrderResponse.fromEntity(order);

        this.enviarConfirmacionPago(
            usuario.getCorreo(),
            usuario.getNombre(),
            order.getId().toString(),
            respuesta
        );

        return respuesta;

    }
}
