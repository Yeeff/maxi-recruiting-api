package com.horizonx.recruiting_microservices.controller;

import com.horizonx.recruiting_microservices.dto.CargoRequest;
import com.horizonx.recruiting_microservices.dto.CargoResponse;
import com.horizonx.recruiting_microservices.service.CargoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gestionar el catálogo de Cargos.
 */
@RestController
@RequestMapping("/api/cargos")
public class CargoController {

    private final CargoService cargoService;

    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    /**
     * Obtiene todos los cargos del catálogo.
     *
     * GET /api/cargos
     *
     * @return lista de CargoResponse
     */
    @GetMapping
    public ResponseEntity<List<CargoResponse>> findAll() {
        return ResponseEntity.ok(cargoService.findAll());
    }

    /**
     * Crea un nuevo cargo en el catálogo.
     *
     * POST /api/cargos
     *
     * @param request datos del cargo a crear
     * @return CargoResponse del cargo creado
     */
    @PostMapping
    public ResponseEntity<CargoResponse> create(@Valid @RequestBody CargoRequest request) {
        CargoResponse response = cargoService.create(request);
        return ResponseEntity.ok(response);
    }
}
