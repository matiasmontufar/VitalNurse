package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.models.Evaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long> {
    
    List<Evaluacion> findByPacienteIdPacienteOrderByFechaDesc(Long idPaciente);
    
}
