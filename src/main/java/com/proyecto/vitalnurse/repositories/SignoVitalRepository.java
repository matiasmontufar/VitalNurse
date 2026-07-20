package com.proyecto.vitalnurse.repositories;

import com.proyecto.vitalnurse.models.SignoVital;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SignoVitalRepository extends JpaRepository<SignoVital, Long> {

    List<SignoVital> findByPacienteIdPacienteOrderByFechaDesc(Long idPaciente);
}
