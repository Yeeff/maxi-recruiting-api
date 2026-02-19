package com.horizonx.recruiting_microservices.controller;

import com.horizonx.recruiting_microservices.dto.ObservacionRequest;
import com.horizonx.recruiting_microservices.dto.ObservacionResponse;
import com.horizonx.recruiting_microservices.service.ObservacionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST para gestionar observaciones de candidatos.
 */
@RestController
@RequestMapping("/api")
public class ObservacionController {

    private static final Logger log = LoggerFactory.getLogger(ObservacionController.class);

    private final ObservacionService observacionService;

    public ObservacionController(ObservacionService observacionService) {
        this.observacionService = observacionService;
    }

    /**
     * Obtiene todas las observaciones de un candidato.
     *
     * GET /api/candidatos/{candidatoId}/observaciones
     */
    @GetMapping("/candidatos/{candidatoId}/observaciones")
    public ResponseEntity<List<ObservacionResponse>> obtenerObservacionesPorCandidato(
            @PathVariable Long candidatoId) {
        log.info("Recibida petición para obtener observaciones del candidato con ID: {}", candidatoId);
        List<ObservacionResponse> response = observacionService.obtenerPorCandidatoId(candidatoId);
        return ResponseEntity.ok(response);
    }

    /**
     * Crea una nueva observación para un candidato.
     *
     * POST /api/candidatos/{candidatoId}/observaciones
     */
    @PostMapping("/candidatos/{candidatoId}/observaciones")
    public ResponseEntity<ObservacionResponse> crearObservacion(
            @PathVariable Long candidatoId,
            @Valid @RequestBody ObservacionRequest request) {
        log.info("Recibida petición para crear observación del candidato con ID: {}", candidatoId);
        ObservacionResponse response = observacionService.crearObservacion(candidatoId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza una observación existente.
     *
     * PUT /api/observaciones/{id}
     */
    @PutMapping("/observaciones/{id}")
    public ResponseEntity<ObservacionResponse> actualizarObservacion(
            @PathVariable Long id,
            @Valid @RequestBody ObservacionRequest request) {
        log.info("Recibida petición para actualizar observación con ID: {}", id);
        ObservacionResponse response = observacionService.actualizarObservacion(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene una observación por su ID.
     *
     * GET /api/observaciones/{id}
     */
    @GetMapping("/observaciones/{id}")
    public ResponseEntity<ObservacionResponse> obtenerObservacionPorId(@PathVariable Long id) {
        log.info("Recibida petición para obtener observación con ID: {}", id);
        ObservacionResponse response = observacionService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina una observación por su ID.
     *
     * DELETE /api/observaciones/{id}
     */
    @DeleteMapping("/observaciones/{id}")
    public ResponseEntity<Void> eliminarObservacion(@PathVariable Long id) {
        log.info("Recibida petición para eliminar observación con ID: {}", id);
        observacionService.eliminarObservacion(id);
        return ResponseEntity.noContent().build();
    }
}
