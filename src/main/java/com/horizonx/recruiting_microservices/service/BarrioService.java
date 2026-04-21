package com.horizonx.recruiting_microservices.service;

import com.horizonx.recruiting_microservices.dto.BarrioResponse;
import com.horizonx.recruiting_microservices.model.entity.Barrio;
import com.horizonx.recruiting_microservices.model.entity.Ciudad;
import com.horizonx.recruiting_microservices.model.repository.BarrioRepository;
import com.horizonx.recruiting_microservices.model.repository.CiudadRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BarrioService {

    private final BarrioRepository barrioRepository;
    private final CiudadRepository ciudadRepository;

    @Transactional(readOnly = true)
    public List<BarrioResponse> obtenerTodos() {
        log.info("Obteniendo todos los barrios");
        return barrioRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public BarrioResponse crear(String nombre, Long ciudadId) {
        log.info("Creando nuevo barrio: {}", nombre);
        Ciudad ciudadRef = ciudadRepository.getReferenceById(ciudadId);
        Barrio barrio = Barrio.builder()
                .nombre(nombre)
                .ciudad(ciudadRef)
                .build();
        Barrio saved = barrioRepository.save(barrio);
        return toDto(saved);
    }

    private BarrioResponse toDto(Barrio barrio) {
        return BarrioResponse.builder()
                .id(barrio.getId())
                .nombre(barrio.getNombre())
                .ciudadId(barrio.getCiudad() != null ? barrio.getCiudad().getId() : null)
                .build();
    }
}
