package com.horizonx.recruiting_microservices.model.repository;

import com.horizonx.recruiting_microservices.model.entity.NivelEstudio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NivelEstudioRepository extends JpaRepository<NivelEstudio, Long> {
}
