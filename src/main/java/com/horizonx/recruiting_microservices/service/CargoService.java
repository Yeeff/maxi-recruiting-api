package com.horizonx.recruiting_microservices.service;

import com.horizonx.recruiting_microservices.dto.CargoResponse;
import com.horizonx.recruiting_microservices.model.entity.Cargo;
import com.horizonx.recruiting_microservices.model.repository.CargoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para gestionar el catálogo de Cargos.
 */
@Service
public class CargoService {

    private final CargoRepository cargoRepository;

    public CargoService(CargoRepository cargoRepository) {
        this.cargoRepository = cargoRepository;
    }

    /**
     * Obtiene todos los cargos del catálogo ordenados por nombre.
     *
     * @return lista de CargoResponse
     */
    public List<CargoResponse> findAll() {
        return cargoRepository.findByOrderByNombreAsc().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Convierte una entidad Cargo a CargoResponse.
     *
     * @param cargo entidad Cargo
     * @return CargoResponse
     */
    private CargoResponse toResponse(Cargo cargo) {
        return CargoResponse.builder()
                .id(cargo.getId())
                .nombre(cargo.getNombre())
                .descripcion(cargo.getDescripcion())
                .build();
    }
}
