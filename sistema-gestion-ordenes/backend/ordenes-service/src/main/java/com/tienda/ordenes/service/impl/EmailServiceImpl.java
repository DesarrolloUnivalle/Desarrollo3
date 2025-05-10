package com.tienda.ordenes.service.impl;

import com.tienda.ordenes.service.EmailService;
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
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void enviarConfirmacionPago(String email, String nombre, String orderId) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);

            helper.setTo(email);
            helper.setSubject("Confirmación de Pago");
            helper.setText(
                String.format("Hola %s,\n\nTu pago para la orden #%s ha sido recibido exitosamente.\n\n¡Gracias por tu compra!",
                        nombre, orderId),
                false
            );

            mailSender.send(mensaje);
            logger.info("Correo enviado exitosamente a {}", email);

        } catch (MessagingException e) {
            logger.error("Error al enviar el correo: ", e);
        }
    }
}
