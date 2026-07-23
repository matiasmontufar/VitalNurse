package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.seguridad.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByUsernameAndActivoTrue(String username);

    Optional<Usuario> findByPersonaCedulaAndActivoTrue(String cedula);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.persona JOIN FETCH u.usuarioRoles ur JOIN FETCH ur.rol WHERE u.username = :username AND u.activo = true")
    Optional<Usuario> findByUsernameWithRoles(@Param("username") String username);

    @Query("SELECT u FROM Usuario u JOIN FETCH u.persona WHERE u.activo = true")
    java.util.List<Usuario> findAllActiveWithPersona();
}