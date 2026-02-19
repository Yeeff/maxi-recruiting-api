package com.horizonx.recruiting_microservices.service;

import com.horizonx.recruiting_microservices.dto.ObservacionRequest;
import com.horizonx.recruiting_microservices.dto.ObservacionResponse;
import com.horizonx.recruiting_microservices.exception.ResourceNotFoundException;
import com.horizonx.recruiting_microservices.model.entity.Candidato;
import com.horizonx.recruiting_microservices.model.entity.Observacion;
import com.horizonx.recruiting_microservices.model.repository.CandidatoRepository;
import com.horizonx.recruiting_microservices.model.repository.ObservacionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar las observaciones de candidatos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ObservacionService {

    private final ObservacionRepository observacionRepository;
    private final CandidatoRepository candidatoRepository;

    /**
     * Crea una nueva observación para un candidato.
     */
    @Transactional
    public ObservacionResponse crearObservacion(Long candidatoId, ObservacionRequest request) {
        log.info("Creando observación para candidato con ID: {}", candidatoId);

        Candidato candidato = candidatoRepository.findById(candidatoId)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato no encontrado con ID: " + candidatoId));

        Observacion observacion = Observacion.builder()
                .observacion(request.getObservacion())
                .tipoObservacion(request.getTipoObservacion())
                .esInterna(request.getEsInterna() != null ? request.getEsInterna() : false)
                .candidato(candidato)
                .build();

        Observacion saved = observacionRepository.save(observacion);
        log.info("Observación creada con ID: {}", saved.getId());

        return ObservacionResponse.fromEntity(saved);
    }

    /**
     * Actualiza una observación existente.
     */
    @Transactional
    public ObservacionResponse actualizarObservacion(Long id, ObservacionRequest request) {
        log.info("Actualizando observación con ID: {}", id);

        Observacion observacion = observacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Observación no encontrada con ID: " + id));

        observacion.setObservacion(request.getObservacion());
        observacion.setTipoObservacion(request.getTipoObservacion());
        if (request.getEsInterna() != null) {
            observacion.setEsInterna(request.getEsInterna());
        }

        Observacion updated = observacionRepository.save(observacion);
        log.info("Observación actualizada con ID: {}", updated.getId());

        return ObservacionResponse.fromEntity(updated);
    }

    /**
     * Obtiene una observación por su ID.
     */
    @Transactional(readOnly = true)
    public ObservacionResponse obtenerPorId(Long id) {
        log.info("Obteniendo observación con ID: {}", id);

        Observacion observacion = observacionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Observación no encontrada con ID: " + id));

        return ObservacionResponse.fromEntity(observacion);
    }

    /**
     * Obtiene todas las observaciones de un candidato.
     */
    @Transactional(readOnly = true)
    public List<ObservacionResponse> obtenerPorCandidatoId(Long candidatoId) {
        log.info("Obteniendo observaciones para candidato con ID: {}", candidatoId);

        if (!candidatoRepository.existsById(candidatoId)) {
            throw new ResourceNotFoundException("Candidato no encontrado con ID: " + candidatoId);
        }

        return observacionRepository.findByCandidatoIdOrderByCreatedAtDesc(candidatoId)
                .stream()
                .map(ObservacionResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Elimina una observación por su ID.
     */
    @Transactional
    public void eliminarObservacion(Long id) {
        log.info("Eliminando observación con ID: {}", id);

        if (!observacionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Observación no encontrada con ID: " + id);
        }

        observacionRepository.deleteById(id);
        log.info("Observación eliminada con ID: {}", id);
    }
}
