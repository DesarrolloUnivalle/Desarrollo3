package com.tienda.ordenes.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.issuer}")
    private String issuer;

    private Key getSigningKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(secret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (Exception e) {
            logger.error("Error al generar la clave de firma: {}", e.getMessage());
            throw new RuntimeException("Error al generar la clave de firma", e);
        }
    }

    public String extractUsername(String token) {
        try {
            Claims claims = getClaims(token);
            return claims != null ? claims.getSubject() : null;
        } catch (Exception e) {
            logger.error("Error al extraer username del token: {}", e.getMessage());
            return null;
        }
    }

    public Date extractExpiration(String token) {
        try {
            Claims claims = getClaims(token);
            return claims != null ? claims.getExpiration() : null;
        } catch (Exception e) {
            logger.error("Error al extraer expiración del token: {}", e.getMessage());
            return null;
        }
    }

    public boolean isTokenExpired(String token) {
        try {
            Date expiration = extractExpiration(token);
            return expiration == null || expiration.before(new Date());
        } catch (Exception e) {
            logger.error("Error al verificar expiración del token: {}", e.getMessage());
            return true;
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = getClaims(token);
            return claims != null ? claimsResolver.apply(claims) : null;
        } catch (Exception e) {
            logger.error("Error al extraer claim del token: {}", e.getMessage());
            return null;
        }
    }

    private Claims getClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.warn("Token expirado: {}", e.getMessage());
            return null;
        } catch (JwtException e) {
            logger.error("Error al parsear el token: {}", e.getMessage());
            return null;
        }
    }

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuer(issuer)
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String token, String username) {
        try {
            final String extractedUsername = extractUsername(token);
            return extractedUsername != null && 
                   extractedUsername.equals(username) && 
                   !isTokenExpired(token);
        } catch (Exception e) {
            logger.error("Error al validar el token: {}", e.getMessage());
            return false;
        }
    }
}