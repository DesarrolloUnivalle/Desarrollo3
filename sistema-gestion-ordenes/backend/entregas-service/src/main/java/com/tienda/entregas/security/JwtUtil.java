package com.tienda.entregas.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Collection;
import java.util.List;

@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    public boolean validarToken(String token) {
        try {
            Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
            
            logger.debug("Token validado. Claims: {}", claims);
            return true;
        } catch (Exception e) {
            logger.error("Error al validar el token: {}", e.getMessage());
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parser()
            .verifyWith(getSecretKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();

        String subject = claims.getSubject();
        String role = claims.get("role", String.class);
        
        logger.debug("Procesando token para usuario: {} con rol: {}", subject, role);
        
        // Asegurarnos de que el rol tenga el prefijo ROLE_
        if (role != null && !role.startsWith("ROLE_")) {
            role = "ROLE_" + role;
        }

        Collection<? extends GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority(role)
        );

        logger.debug("Autoridades establecidas: {}", authorities);
        return new JwtAuthenticationToken(subject, authorities);
    }

    private SecretKey getSecretKey() {
        try {
            byte[] decodedKey = Base64.getDecoder().decode(secret);
            return Keys.hmacShaKeyFor(decodedKey);
        } catch (Exception e) {
            logger.error("Error al procesar la clave secreta: {}", e.getMessage());
            throw e;
        }
    }
}