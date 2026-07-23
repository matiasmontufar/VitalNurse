package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.persona.Persona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonaRepository extends JpaRepository<Persona, Long> {
    Optional<Persona> findByCedulaAndIsDeletedFalse(String cedula);
    boolean existsByCedulaAndIsDeletedFalse(String cedula);
}
