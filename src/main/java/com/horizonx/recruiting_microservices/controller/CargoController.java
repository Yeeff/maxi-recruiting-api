package com.horizonx.recruiting_microservices.controller;

import com.horizonx.recruiting_microservices.dto.CargoResponse;
import com.horizonx.recruiting_microservices.service.CargoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
