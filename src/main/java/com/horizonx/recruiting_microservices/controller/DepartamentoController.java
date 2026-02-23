package com.horizonx.recruiting_microservices.controller;

import com.horizonx.recruiting_microservices.dto.DepartamentoResponse;
import com.horizonx.recruiting_microservices.service.DepartamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller REST para gestionar el catálogo de Departamentos.
 */
@RestController
@RequestMapping("/api/departamentos")
public class DepartamentoController {

    private final DepartamentoService departamentoService;

    public DepartamentoController(DepartamentoService departamentoService) {
        this.departamentoService = departamentoService;
    }

    /**
     * Obtiene todos los departamentos del catálogo.
     *
     * GET /api/departamentos
     *
     * @return lista de DepartamentoResponse
     */
    @GetMapping
    public ResponseEntity<List<DepartamentoResponse>> findAll() {
        return ResponseEntity.ok(departamentoService.findAll());
    }
}
