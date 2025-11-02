package com.bezkoder.springjwt.configuracionSeguridad.accesoADatos.repositorios;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bezkoder.springjwt.configuracionSeguridad.accesoADatos.entidades.ERole;
import com.bezkoder.springjwt.configuracionSeguridad.accesoADatos.entidades.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByName(ERole name);
}
