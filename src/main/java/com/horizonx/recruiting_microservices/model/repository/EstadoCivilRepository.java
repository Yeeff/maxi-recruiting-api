package com.horizonx.recruiting_microservices.model.repository;

import com.horizonx.recruiting_microservices.model.entity.EstadoCivil;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstadoCivilRepository extends JpaRepository<EstadoCivil, Long> {
}
