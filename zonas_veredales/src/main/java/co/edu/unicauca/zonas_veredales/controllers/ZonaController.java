package co.edu.unicauca.zonas_veredales.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import co.edu.unicauca.zonas_veredales.entity.Zona;
import co.edu.unicauca.zonas_veredales.service.ZonaService;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/zonas")
@RequiredArgsConstructor
public class ZonaController {
    private final ZonaService service;

    @GetMapping
    public List<Zona> listar() { return service.listar(); }

    @PostMapping
    public ResponseEntity<Zona> crear(@RequestBody Zona z) {
        return ResponseEntity.ok(service.crear(z));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Zona> actualizar(@PathVariable Long id, @RequestBody Zona z) {
        return ResponseEntity.ok(service.actualizar(id, z));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
