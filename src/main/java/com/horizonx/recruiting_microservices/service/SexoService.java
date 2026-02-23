package com.horizonx.recruiting_microservices.service;

import com.horizonx.recruiting_microservices.dto.SexoResponse;
import com.horizonx.recruiting_microservices.model.entity.Sexo;
import com.horizonx.recruiting_microservices.model.repository.SexoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar el catálogo de Sexos.
 */
@Service
public class SexoService {

    private final SexoRepository sexoRepository;

    public SexoService(SexoRepository sexoRepository) {
        this.sexoRepository = sexoRepository;
    }

    /**
     * Obtiene todos los sexos del catálogo.
     *
     * @return lista de SexoResponse
     */
    public List<SexoResponse> findAll() {
        return sexoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Sexo a SexoResponse.
     *
     * @param sexo entidad Sexo
     * @return SexoResponse
     */
    private SexoResponse toResponse(Sexo sexo) {
        return SexoResponse.builder()
                .id(sexo.getId())
                .nombre(sexo.getNombre())
                .descripcion(sexo.getDescripcion())
                .build();
    }
}
