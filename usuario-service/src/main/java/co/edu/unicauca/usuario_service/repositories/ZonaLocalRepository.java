package co.edu.unicauca.usuario_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unicauca.usuario_service.entities.ZonaLocal;

import java.util.Optional;

public interface ZonaLocalRepository extends JpaRepository<ZonaLocal, Long> {
    Optional<ZonaLocal> findByCodigo(String codigo);
}