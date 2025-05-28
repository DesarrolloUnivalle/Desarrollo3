package com.tienda.entregas.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtUtil jwtUtil;
    
    // Constructor explícito
    public JwtAuthFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        
        try {
            String bearerToken = request.getHeader("Authorization");
            logger.debug("Header Authorization: {}", bearerToken);
            
            if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
                String token = bearerToken.substring(7);
                logger.debug("Token recibido: presente");
                
                boolean esValido = jwtUtil.validarToken(token);
                logger.debug("Token válido: {}", esValido);
                
                if (esValido) {
                    String username = jwtUtil.extractUsername(token);
                    
                    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // Crear un objeto Jwt y almacenarlo en el contexto de seguridad
                        Jwt jwt = Jwt.withTokenValue(token)
                                .header("alg", "HS256")
                                .claim("sub", username)
                                .build();
                        
                        var authentication = jwtUtil.getAuthentication(token);
                        
                        var newAuth = new UsernamePasswordAuthenticationToken(
                            jwt, // Principal como objeto Jwt
                            null, // Credenciales
                            authentication.getAuthorities() // Autoridades originales
                        );
                        
                        SecurityContextHolder.getContext().setAuthentication(newAuth);
                        logger.debug("Autenticación establecida para usuario: {}", username);
                    }
                }
            } else {
                logger.debug("Token recibido: ausente");
            }
        } catch (Exception e) {
            logger.error("Error al procesar el token JWT", e);
            // No mandamos error a la respuesta para dejar que continúe el filtro
        }

        filterChain.doFilter(request, response);
    }
}