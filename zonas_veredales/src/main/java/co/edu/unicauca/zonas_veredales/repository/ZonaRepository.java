package co.edu.unicauca.zonas_veredales.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unicauca.zonas_veredales.entity.Zona;

import java.util.Optional;

public interface ZonaRepository extends JpaRepository<Zona, Long> {
    Optional<Zona> findByCodigo(String codigo);
}
