package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Este método mágico buscará a un usuario por su cédula para el Login
    Optional<Usuario> findByCedula(String cedula);
}