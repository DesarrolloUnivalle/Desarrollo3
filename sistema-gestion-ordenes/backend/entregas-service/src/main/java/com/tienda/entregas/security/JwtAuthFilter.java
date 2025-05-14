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
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.ArrayList;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthFilter.class);
    private final JwtUtil jwtUtil;

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
                    var authentication = jwtUtil.getAuthentication(token);
                    // Crear una nueva autenticación con el token completo en las credenciales
                    var newAuth = new UsernamePasswordAuthenticationToken(
                        authentication.getPrincipal(),
                        bearerToken, // Usar el token completo con "Bearer "
                        authentication.getAuthorities()
                    );
                    logger.debug("Autenticación establecida con rol: {}", 
                        authentication.getAuthorities().iterator().next().getAuthority());
                    SecurityContextHolder.getContext().setAuthentication(newAuth);
                }
            } else {
                logger.debug("Token recibido: ausente");
            }
        } catch (Exception e) {
            logger.error("Error al procesar el token JWT", e);
        }

        filterChain.doFilter(request, response);
    }
}