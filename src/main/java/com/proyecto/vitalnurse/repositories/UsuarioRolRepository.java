package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.seguridad.UsuarioRol;
import com.proyecto.vitalnurse.entity.seguridad.UsuarioRolId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRolRepository extends JpaRepository<UsuarioRol, UsuarioRolId> {
}
