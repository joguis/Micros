package co.edu.unicauca.usuario_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unicauca.usuario_service.entities.UsuarioEntity;
import co.edu.unicauca.usuario_service.service.UsuarioService;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;

    @PutMapping("/{id}/asociar-zona/{zonaCodigo}")
    public ResponseEntity<UsuarioEntity> asociarZona(
            @PathVariable Long id,
            @PathVariable String zonaCodigo) {

        UsuarioEntity actualizado = service.asociarZona(id, zonaCodigo);
        return ResponseEntity.ok(actualizado);
    }


    @GetMapping
    public List<UsuarioEntity> listar() {
        return service.listarTodos();
    }

    @PostMapping
    public ResponseEntity<UsuarioEntity> crear(@RequestBody UsuarioEntity u) {
        return ResponseEntity.ok(service.crear(u));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioEntity> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(service.obtenerPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
