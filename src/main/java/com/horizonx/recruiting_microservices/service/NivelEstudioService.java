package com.horizonx.recruiting_microservices.service;

import com.horizonx.recruiting_microservices.dto.NivelEstudioResponse;
import com.horizonx.recruiting_microservices.model.entity.NivelEstudio;
import com.horizonx.recruiting_microservices.model.repository.NivelEstudioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar el catálogo de Niveles de Estudio.
 */
@Service
public class NivelEstudioService {

    private final NivelEstudioRepository nivelEstudioRepository;

    public NivelEstudioService(NivelEstudioRepository nivelEstudioRepository) {
        this.nivelEstudioRepository = nivelEstudioRepository;
    }

    /**
     * Obtiene todos los niveles de estudio del catálogo.
     *
     * @return lista de NivelEstudioResponse
     */
    public List<NivelEstudioResponse> findAll() {
        return nivelEstudioRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad NivelEstudio a NivelEstudioResponse.
     *
     * @param nivelEstudio entidad NivelEstudio
     * @return NivelEstudioResponse
     */
    private NivelEstudioResponse toResponse(NivelEstudio nivelEstudio) {
        return NivelEstudioResponse.builder()
                .id(nivelEstudio.getId())
                .nombre(nivelEstudio.getNombre())
                .descripcion(nivelEstudio.getDescripcion())
                .orden(nivelEstudio.getOrden())
                .build();
    }
}
