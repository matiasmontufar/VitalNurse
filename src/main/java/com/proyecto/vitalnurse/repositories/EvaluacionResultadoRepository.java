package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.clinical.EvaluacionResultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EvaluacionResultadoRepository extends JpaRepository<EvaluacionResultado, Long> {
}
