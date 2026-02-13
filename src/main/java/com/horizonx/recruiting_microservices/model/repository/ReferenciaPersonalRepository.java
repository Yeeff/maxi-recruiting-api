package com.horizonx.recruiting_microservices.model.repository;

import com.horizonx.recruiting_microservices.model.entity.ReferenciaPersonal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReferenciaPersonalRepository extends JpaRepository<ReferenciaPersonal, Long> {

    List<ReferenciaPersonal> findByCandidatoId(Long candidatoId);

    List<ReferenciaPersonal> findByCandidatoIdAndTipoReferencia(
            Long candidatoId, ReferenciaPersonal.TipoReferencia tipoReferencia);
}
