package com.horizonx.recruiting_microservices.service;

import com.horizonx.recruiting_microservices.dto.EstadoCivilResponse;
import com.horizonx.recruiting_microservices.model.entity.EstadoCivil;
import com.horizonx.recruiting_microservices.model.repository.EstadoCivilRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar el catálogo de Estados Civiles.
 */
@Service
public class EstadoCivilService {

    private final EstadoCivilRepository estadoCivilRepository;

    public EstadoCivilService(EstadoCivilRepository estadoCivilRepository) {
        this.estadoCivilRepository = estadoCivilRepository;
    }

    /**
     * Obtiene todos los estados civiles del catálogo.
     *
     * @return lista de EstadoCivilResponse
     */
    public List<EstadoCivilResponse> findAll() {
        return estadoCivilRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad EstadoCivil a EstadoCivilResponse.
     *
     * @param estadoCivil entidad EstadoCivil
     * @return EstadoCivilResponse
     */
    private EstadoCivilResponse toResponse(EstadoCivil estadoCivil) {
        return EstadoCivilResponse.builder()
                .id(estadoCivil.getId())
                .nombre(estadoCivil.getNombre())
                .descripcion(estadoCivil.getDescripcion())
                .build();
    }
}
