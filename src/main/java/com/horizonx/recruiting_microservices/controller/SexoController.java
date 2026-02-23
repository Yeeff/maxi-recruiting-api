package com.horizonx.recruiting_microservices.controller;

import com.horizonx.recruiting_microservices.dto.SexoResponse;
import com.horizonx.recruiting_microservices.service.SexoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST para gestionar el catálogo de Sexos.
 */
@RestController
@RequestMapping("/api/sexos")
public class SexoController {

    private final SexoService sexoService;

    public SexoController(SexoService sexoService) {
        this.sexoService = sexoService;
    }

    /**
     * Obtiene todos los sexos del catálogo.
     *
     * GET /api/sexos
     *
     * @return lista de SexoResponse
     */
    @GetMapping
    public ResponseEntity<List<SexoResponse>> findAll() {
        return ResponseEntity.ok(sexoService.findAll());
    }
}
