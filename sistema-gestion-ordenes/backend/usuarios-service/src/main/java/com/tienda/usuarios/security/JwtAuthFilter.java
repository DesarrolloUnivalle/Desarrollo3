package com.tienda.usuarios.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
    
        final String authHeader = request.getHeader("Authorization");
        final String token;
        final String email;
    
        System.out.println("🛠 Verificando token..."); // 👈 Agrega esto para ver si el filtro se ejecuta
    
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            System.out.println("🚨 No se encontró token en la solicitud.");
            filterChain.doFilter(request, response);
            return;
        }
    
        // Extraer token del encabezado
        token = authHeader.substring(7);
        email = jwtUtil.extractUsername(token);
    
        System.out.println("🔎 Token recibido: " + token);
        System.out.println("📧 Usuario del token: " + email);
    
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);
    
            if (jwtUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                System.out.println("✅ Token válido, autenticación establecida.");
            } else {
                System.out.println("❌ Token inválido.");
            }
        }
    
        filterChain.doFilter(request, response);
    }
}