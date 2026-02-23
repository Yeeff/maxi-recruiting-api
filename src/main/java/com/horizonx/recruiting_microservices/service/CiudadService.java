package com.horizonx.recruiting_microservices.service;

import com.horizonx.recruiting_microservices.dto.CiudadResponse;
import com.horizonx.recruiting_microservices.model.entity.Ciudad;
import com.horizonx.recruiting_microservices.model.repository.CiudadRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar el catálogo de Ciudades.
 */
@Service
public class CiudadService {

    private final CiudadRepository ciudadRepository;

    public CiudadService(CiudadRepository ciudadRepository) {
        this.ciudadRepository = ciudadRepository;
    }

    /**
     * Obtiene todas las ciudades del catálogo.
     *
     * @return lista de CiudadResponse
     */
    public List<CiudadResponse> findAll() {
        return ciudadRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Obtiene las ciudades de un departamento específico.
     *
     * @param departamentoId ID del departamento
     * @return lista de CiudadResponse
     */
    public List<CiudadResponse> findByDepartamentoId(Long departamentoId) {
        return ciudadRepository.findByDepartamentoId(departamentoId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Ciudad a CiudadResponse.
     *
     * @param ciudad entidad Ciudad
     * @return CiudadResponse
     */
    private CiudadResponse toResponse(Ciudad ciudad) {
        return CiudadResponse.builder()
                .id(ciudad.getId())
                .nombre(ciudad.getNombre())
                .codigo(ciudad.getCodigo())
                .departamentoId(ciudad.getDepartamento().getId())
                .departamentoNombre(ciudad.getDepartamento().getNombre())
                .build();
    }
}
