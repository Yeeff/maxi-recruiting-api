package com.horizonx.recruiting_microservices.model.repository;

import com.horizonx.recruiting_microservices.model.entity.Observacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObservacionRepository extends JpaRepository<Observacion, Long> {

    /**
     * Obtiene todas las observaciones de un candidato ordenadas por fecha de creación descendente.
     */
    List<Observacion> findByCandidatoIdOrderByCreatedAtDesc(Long candidatoId);

    /**
     * Obtiene todas las observaciones de un candidato por tipo.
     */
    List<Observacion> findByCandidatoIdAndTipoObservacion(Long candidatoId, Observacion.TipoObservacion tipoObservacion);

    /**
     * Verifica si existen observaciones para un candidato.
     */
    boolean existsByCandidatoId(Long candidatoId);
}
