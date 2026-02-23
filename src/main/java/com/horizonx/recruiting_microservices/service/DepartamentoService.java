package com.horizonx.recruiting_microservices.service;

import com.horizonx.recruiting_microservices.dto.DepartamentoResponse;
import com.horizonx.recruiting_microservices.model.entity.Departamento;
import com.horizonx.recruiting_microservices.model.repository.DepartamentoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar el catálogo de Departamentos.
 */
@Service
public class DepartamentoService {

    private final DepartamentoRepository departamentoRepository;

    public DepartamentoService(DepartamentoRepository departamentoRepository) {
        this.departamentoRepository = departamentoRepository;
    }

    /**
     * Obtiene todos los departamentos del catálogo.
     *
     * @return lista de DepartamentoResponse
     */
    public List<DepartamentoResponse> findAll() {
        return departamentoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Departamento a DepartamentoResponse.
     *
     * @param departamento entidad Departamento
     * @return DepartamentoResponse
     */
    private DepartamentoResponse toResponse(Departamento departamento) {
        return DepartamentoResponse.builder()
                .id(departamento.getId())
                .nombre(departamento.getNombre())
                .codigo(departamento.getCodigo())
                .build();
    }
}
