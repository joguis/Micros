package co.edu.unicauca.zonas_veredales.service;

import org.springframework.stereotype.Service;

import co.edu.unicauca.zonas_veredales.entity.Zona;
import co.edu.unicauca.zonas_veredales.eventos.ZonaEvent;
import co.edu.unicauca.zonas_veredales.repository.ZonaRepository;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import lombok.RequiredArgsConstructor;
import java.util.List;


@Service
@RequiredArgsConstructor
public class ZonaService {
    private final ZonaRepository repo;
    private final RabbitTemplate rabbit;
    @Value("${app.exchange}")
    private String exchange;
    @Value("${app.routingKeyPrefix}")
    private String routingKeyPrefix;

    public List<Zona> listar() { return repo.findAll(); }

    public Zona crear(Zona z) {
        Zona saved = repo.save(z);
        publishEvent("created", saved);
        return saved;
    }

    public Zona actualizar(Long id, Zona z) {
        Zona existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Zona no encontrada"));
        existing.setCodigo(z.getCodigo());
        existing.setNombre(z.getNombre());
        existing.setDescripcion(z.getDescripcion());
        existing.setDepartamento(z.getDepartamento());
        existing.setMunicipio(z.getMunicipio());
        Zona saved = repo.save(existing);
        publishEvent("updated", saved);
        return saved;
    }

    public void eliminar(Long id) {
        Zona existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Zona no encontrada"));
        repo.delete(existing);
        publishEvent("deleted", existing);
    }

    private void publishEvent(String action, Zona z) {
        ZonaEvent ev = new ZonaEvent(action, z.getId(), z.getCodigo(), z.getNombre(), z.getDescripcion(), z.getDepartamento(), z.getMunicipio());
        String rk = routingKeyPrefix + action; // e.g. zona.created
        rabbit.convertAndSend(exchange, rk, ev);
    }
}
