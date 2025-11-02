package com.bezkoder.springjwt.fitros;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class GatewayHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("X-Gateway-Passed");
        
        if (header == null || !header.equals("true")) {
           response.setStatus(HttpServletResponse.SC_FORBIDDEN);
           response.setContentType("application/json");
           response.getWriter().write("{\"message\": \"Acceso denegado desde el filtro. Debe pasar por el gateway.\"}");
           return;
        }

        // Si la cabecera es válida, deja que la solicitud continúe
        filterChain.doFilter(request, response);
    }

}

