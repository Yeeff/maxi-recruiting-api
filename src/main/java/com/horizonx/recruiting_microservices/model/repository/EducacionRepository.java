package com.horizonx.recruiting_microservices.model.repository;

import com.horizonx.recruiting_microservices.model.entity.Educacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EducacionRepository extends JpaRepository<Educacion, Long> {

    List<Educacion> findByCandidatoId(Long candidatoId);

    List<Educacion> findByCandidatoIdOrderByFechaFinDesc(Long candidatoId);
}
