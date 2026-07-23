package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.catalogo.TipoEvaluacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoEvaluacionRepository extends JpaRepository<TipoEvaluacion, Long> {
    Optional<TipoEvaluacion> findByCodigo(String codigo);
}
