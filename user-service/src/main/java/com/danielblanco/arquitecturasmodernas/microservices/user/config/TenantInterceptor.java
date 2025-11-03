package com.danielblanco.arquitecturasmodernas.microservices.user.config;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Obtener zona_veredal_id del header (puedes cambiarlo a JWT o query param)
        String zonaVeredalId = request.getHeader("X-Zona-Veredal-Id");
        
        if (zonaVeredalId != null && !zonaVeredalId.isEmpty()) {
            try {
                Long zonaVeredalIdLong = Long.parseLong(zonaVeredalId);
                TenantContext.setCurrentTenant(zonaVeredalIdLong);
                
                // Activar filtro de Hibernate
                Session session = entityManager.unwrap(Session.class);
                Filter filter = session.enableFilter("zonaVeredalFilter");
                filter.setParameter("zonaVeredalId", zonaVeredalIdLong);
            } catch (NumberFormatException e) {
                // Si no es válido, no aplicar filtro
            }
        }
        
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, 
                               Object handler, Exception ex) {
        TenantContext.clear();
    }
}