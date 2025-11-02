package co.edu.unicauca.usuario_service.capaSeguridad;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import co.edu.unicauca.usuario_service.config.TenantContext;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Value("${bezkoder.app.jwtSecret}")
    private String jwtSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
        throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.replace("Bearer ", "");

            try {
                // ✅ 1. Validar y parsear el token
                Jws<Claims> claims = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8)))
                    .build()
                    .parseClaimsJws(token);

                // ✅ 2. Extraer datos principales
                String username = claims.getBody().getSubject();
                List<String> roles = claims.getBody().get("roles", List.class);

                // ✅ 3. Nuevo: extraer zonaId del token
                String zonaId = claims.getBody().get("zonaId", String.class);

                // ✅ 4. Guardar el tenant actual en un contexto ThreadLocal (si lo usarás en otros servicios)
                if (zonaId != null) {
                    TenantContext.setTenantId(zonaId);
                }

                // ✅ 5. Construir authorities
                List<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

                // ✅ 6. Crear el objeto de autenticación
                UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"error\": \"Token inválido o expirado\"}");
                return;
            } catch (Exception e) {
                System.out.println("Error procesando JWT: " + e.getMessage());
                return;
            }
        }

        // ✅ 7. Continuar con la cadena de filtros
        filterChain.doFilter(request, response);

        // ✅ 8. Limpiar el tenant del contexto al final de la petición
        TenantContext.clear();
    }
}
