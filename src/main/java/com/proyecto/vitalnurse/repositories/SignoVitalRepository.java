package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.clinical.EvaluacionDetalleSigno;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignoVitalRepository extends JpaRepository<EvaluacionDetalleSigno, Long> {

    List<EvaluacionDetalleSigno> findByCabeceraIdCabeceraAndIsDeletedFalse(Long idCabecera);
}
