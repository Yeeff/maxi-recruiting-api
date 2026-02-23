package com.horizonx.recruiting_microservices.controller;

import com.horizonx.recruiting_microservices.dto.NivelEstudioResponse;
import com.horizonx.recruiting_microservices.service.NivelEstudioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST para gestionar el catálogo de Niveles de Estudio.
 */
@RestController
@RequestMapping("/api/niveles-estudio")
public class NivelEstudioController {

    private final NivelEstudioService nivelEstudioService;

    public NivelEstudioController(NivelEstudioService nivelEstudioService) {
        this.nivelEstudioService = nivelEstudioService;
    }

    /**
     * Obtiene todos los niveles de estudio del catálogo.
     *
     * GET /api/niveles-estudio
     *
     * @return lista de NivelEstudioResponse
     */
    @GetMapping
    public ResponseEntity<List<NivelEstudioResponse>> findAll() {
        return ResponseEntity.ok(nivelEstudioService.findAll());
    }
}
