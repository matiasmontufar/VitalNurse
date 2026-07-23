package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.entity.catalogo.Genero;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroRepository extends JpaRepository<Genero, Integer> {
}
