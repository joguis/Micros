package co.edu.unicauca.usuario_service.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unicauca.usuario_service.entities.ZonaLocal;
import co.edu.unicauca.usuario_service.repositories.ZonaLocalRepository;


@Component
@RequiredArgsConstructor
public class ZonaEventListener {

    private final ZonaLocalRepository repo;
    private final ObjectMapper mapper = new ObjectMapper();

    @RabbitListener(queues = "zona.queue")
    @Transactional
    public void handle(String message) throws Exception {
        // message es JSON del ZonaEvent
        var node = mapper.readTree(message);
        String action = node.get("action").asText();
        Long id = node.get("id").asLong();
        String codigo = node.get("codigo").asText();
        String nombre = node.get("nombre").asText();
        String descripcion = node.has("descripcion") ? node.get("descripcion").asText() : null;
        String departamento = node.has("departamento") ? node.get("departamento").asText() : null;
        String municipio = node.has("municipio") ? node.get("municipio").asText() : null;

        if ("created".equalsIgnoreCase(action) || "updated".equalsIgnoreCase(action)) {
            ZonaLocal zk = ZonaLocal.builder()
                .id(id)
                .codigo(codigo)
                .nombre(nombre)
                .descripcion(descripcion)
                .departamento(departamento)
                .municipio(municipio)
                .build();
            repo.save(zk);
        } else if ("deleted".equalsIgnoreCase(action)) {
            repo.findById(id).ifPresent(repo::delete);
        }
    }
}
