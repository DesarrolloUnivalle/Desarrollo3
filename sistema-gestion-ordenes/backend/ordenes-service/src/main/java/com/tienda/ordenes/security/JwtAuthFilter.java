package com.tienda.ordenes.security;

import com.tienda.ordenes.security.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        logger.debug("Procesando petición: {}", request.getRequestURI());

        final String authHeader = request.getHeader("Authorization");
        final String token;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            logger.debug("No se encontró token en el header");
            filterChain.doFilter(request, response);
            return;
        }

        token = authHeader.substring(7);
        logger.debug("Token encontrado: {}", token);

        try {
            if (!jwtUtil.isTokenExpired(token)) {
                String username = jwtUtil.extractUsername(token);
                if (username != null) {
                    // Crear un objeto Jwt y almacenarlo en el contexto de seguridad
                    Jwt jwt = Jwt.withTokenValue(token)
                            .header("alg", "HS256")
                            .claim("sub", username)
                            .build();

                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(jwt, null, new ArrayList<>());
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.debug("Token validado y autenticación establecida para usuario: {}", username);
                } else {
                    logger.warn("No se pudo extraer el username del token");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido");
                    return;
                }
            } else {
                logger.warn("Token expirado");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expirado");
                return;
            }
        } catch (Exception e) {
            logger.error("Error al procesar el token: {}", e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token inválido o expirado");
            return;
        }

        filterChain.doFilter(request, response);
    }
}