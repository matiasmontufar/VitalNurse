package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.catalogo.CatSignoVital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CatSignoVitalRepository extends JpaRepository<CatSignoVital, Long> {
    Optional<CatSignoVital> findByCodigo(String codigo);
}
