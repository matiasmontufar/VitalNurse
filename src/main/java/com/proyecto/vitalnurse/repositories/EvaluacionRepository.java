package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.clinical.EvaluacionCabecera;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluacionRepository extends JpaRepository<EvaluacionCabecera, Long> {

    List<EvaluacionCabecera> findByPacienteIdPacienteAndIsDeletedFalseOrderByFechaHoraDesc(Long idPaciente);
}
