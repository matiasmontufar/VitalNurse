package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.seguridad.Rol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
    Optional<Rol> findByCodigoAndIsDeletedFalse(String codigo);
}
