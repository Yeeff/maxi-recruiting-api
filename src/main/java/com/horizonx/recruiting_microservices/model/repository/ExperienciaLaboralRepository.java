package com.horizonx.recruiting_microservices.model.repository;

import com.horizonx.recruiting_microservices.model.entity.ExperienciaLaboral;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExperienciaLaboralRepository extends JpaRepository<ExperienciaLaboral, Long> {

    List<ExperienciaLaboral> findByCandidatoId(Long candidatoId);

    List<ExperienciaLaboral> findByCandidatoIdAndEsActualTrue(Long candidatoId);

    @Query("SELECT e FROM ExperienciaLaboral e WHERE e.candidato.id = :candidatoId ORDER BY e.fechaIngreso DESC")
    List<ExperienciaLaboral> findByCandidatoIdOrderByFechaIngresoDesc(@Param("candidatoId") Long candidatoId);
}
