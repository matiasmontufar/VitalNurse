package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.models.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {
    // Este método nos servirá para evitar que se registren pacientes duplicados
    boolean existsByCedula(String cedula);

    // Este método nos servirá para la consulta rápida del historial
    Optional<Paciente> findByCedula(String cedula);
}