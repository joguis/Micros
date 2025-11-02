package co.edu.unicauca.usuario_service.capaSeguridad;



import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;



@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  @Override
  public void handle(HttpServletRequest request,
                     HttpServletResponse response,
                     AccessDeniedException accessDeniedException) throws IOException {

    response.setContentType("application/json");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403

    response.getWriter().write("{\"error\": \"No tienes permisos para acceder a este recurso.\"}");
  }
}

