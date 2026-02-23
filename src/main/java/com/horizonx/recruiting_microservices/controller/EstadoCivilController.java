package com.horizonx.recruiting_microservices.controller;

import com.horizonx.recruiting_microservices.dto.EstadoCivilResponse;
import com.horizonx.recruiting_microservices.service.EstadoCivilService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST para gestionar el catálogo de Estados Civiles.
 */
@RestController
@RequestMapping("/api/estados-civiles")
public class EstadoCivilController {

    private final EstadoCivilService estadoCivilService;

    public EstadoCivilController(EstadoCivilService estadoCivilService) {
        this.estadoCivilService = estadoCivilService;
    }

    /**
     * Obtiene todos los estados civiles del catálogo.
     *
     * GET /api/estados-civiles
     *
     * @return lista de EstadoCivilResponse
     */
    @GetMapping
    public ResponseEntity<List<EstadoCivilResponse>> findAll() {
        return ResponseEntity.ok(estadoCivilService.findAll());
    }
}
