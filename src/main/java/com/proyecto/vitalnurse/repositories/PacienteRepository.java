package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.persona.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Optional<Paciente> findByPersonaCedulaAndIsDeletedFalse(String cedula);

    boolean existsByPersonaCedulaAndIsDeletedFalse(String cedula);

    @Query("SELECT p FROM Paciente p JOIN FETCH p.persona WHERE p.isDeleted = false ORDER BY p.persona.apellidos")
    java.util.List<Paciente> findAllActiveWithPersona();
}