package co.edu.unicauca.usuario_service.service;

import org.springframework.stereotype.Service;

import co.edu.unicauca.usuario_service.entities.UsuarioEntity;
import co.edu.unicauca.usuario_service.entities.ZonaLocal;
import co.edu.unicauca.usuario_service.repositories.UsuarioRepository;
import co.edu.unicauca.usuario_service.repositories.ZonaLocalRepository;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepo;
    private final ZonaLocalRepository zonaRepo;

    public List<UsuarioEntity> listarTodos() {
        return usuarioRepo.findAll();
    }

    public UsuarioEntity crear(UsuarioEntity u) {
        return usuarioRepo.save(u);
    }

    public UsuarioEntity obtenerPorId(Long id) {
        return usuarioRepo.findById(id).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public void eliminar(Long id) {
        usuarioRepo.deleteById(id);
    }

    public UsuarioEntity asociarZona(Long usuarioId, String zonaCodigo) {
        UsuarioEntity usuario = obtenerPorId(usuarioId);
        Optional<ZonaLocal> zonaOpt = zonaRepo.findByCodigo(zonaCodigo);

        if (zonaOpt.isEmpty()) {
            throw new RuntimeException("Zona no encontrada o aún no sincronizada");
        }

        // (Opcional) Validar tenant del JWT contra la zona (si aplicas multitenencia lógica)
        /*
        String tenant = TenantContext.getCurrentTenant();
        if (tenant != null && !tenant.equalsIgnoreCase(zonaCodigo)) {
            throw new AccessDeniedException("No puedes asociar a una zona distinta de tu tenant");
        }
        */

        usuario.setZonaCodigo(zonaCodigo);
        return usuarioRepo.save(usuario);
    }
}

