package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.clinical.EvaluacionCabecera;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluacionCabeceraRepository extends JpaRepository<EvaluacionCabecera, Long> {

    @EntityGraph(attributePaths = {"tipoEvaluacion", "resultado", "usuario"})
    List<EvaluacionCabecera> findByPacienteIdPacienteAndIsDeletedFalseOrderByFechaHoraDesc(Long idPaciente);
}
