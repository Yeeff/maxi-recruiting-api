package com.horizonx.recruiting_microservices.model.repository;

import com.horizonx.recruiting_microservices.model.entity.Barrio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BarrioRepository extends JpaRepository<Barrio, Long> {

    List<Barrio> findByCiudadId(Long ciudadId);
}
