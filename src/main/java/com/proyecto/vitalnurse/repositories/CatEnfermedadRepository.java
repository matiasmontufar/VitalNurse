package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.catalogo.CatEnfermedadPreexistente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CatEnfermedadRepository extends JpaRepository<CatEnfermedadPreexistente, Long> {
    List<CatEnfermedadPreexistente> findByIsActiveTrue();
}
