package co.edu.unicauca.usuario_service.config;


import org.springframework.stereotype.Component;
import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

@Component
public class TenantFilter implements Filter {
    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        String tenant = request.getHeader("X-Tenant-ID");
        if (tenant == null) tenant = "public";
        TenantContext.setTenantId(tenant);
        try {
            chain.doFilter(req, res);
        } finally {
            TenantContext.clear();
        }
    }
}