package co.edu.unicauca.usuario_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unicauca.usuario_service.entities.UsuarioEntity;

import java.util.List;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    List<UsuarioEntity> findByZona(String zona);
    UsuarioEntity findByEmail(String email);
}
