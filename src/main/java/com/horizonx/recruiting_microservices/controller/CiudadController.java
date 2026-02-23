package com.horizonx.recruiting_microservices.controller;

import com.horizonx.recruiting_microservices.dto.CiudadResponse;
import com.horizonx.recruiting_microservices.service.CiudadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST para gestionar el catálogo de Ciudades.
 */
@RestController
@RequestMapping("/api/ciudades")
public class CiudadController {

    private final CiudadService ciudadService;

    public CiudadController(CiudadService ciudadService) {
        this.ciudadService = ciudadService;
    }

    /**
     * Obtiene todas las ciudades del catálogo.
     *
     * GET /api/ciudades
     *
     * @return lista de CiudadResponse
     */
    @GetMapping
    public ResponseEntity<List<CiudadResponse>> findAll() {
        return ResponseEntity.ok(ciudadService.findAll());
    }

    /**
     * Obtiene las ciudades de un departamento específico.
     *
     * GET /api/ciudades/departamento/{departamentoId}
     *
     * @param departamentoId ID del departamento
     * @return lista de CiudadResponse
     */
    @GetMapping("/departamento/{departamentoId}")
    public ResponseEntity<List<CiudadResponse>> findByDepartamentoId(@PathVariable Long departamentoId) {
        return ResponseEntity.ok(ciudadService.findByDepartamentoId(departamentoId));
    }
}
