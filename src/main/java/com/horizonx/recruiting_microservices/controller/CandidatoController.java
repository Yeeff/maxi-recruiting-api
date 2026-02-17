package com.horizonx.recruiting_microservices.controller;

import com.horizonx.recruiting_microservices.dto.CandidatoRequest;
import com.horizonx.recruiting_microservices.dto.CandidatoResponse;
import com.horizonx.recruiting_microservices.service.CandidatoPdfService;
import com.horizonx.recruiting_microservices.service.CandidatoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller REST unificado para gestionar candidatos.
 * Maneja operaciones CRUD completas incluyendo todas las relaciones.
 */
@RestController
@RequestMapping("/api/candidatos")
public class CandidatoController {

    private static final Logger log = LoggerFactory.getLogger(CandidatoController.class);

    private final CandidatoService candidatoService;
    private final CandidatoPdfService candidatoPdfService;

    public CandidatoController(CandidatoService candidatoService, CandidatoPdfService candidatoPdfService) {
        this.candidatoService = candidatoService;
        this.candidatoPdfService = candidatoPdfService;
    }

    /**
     * Crea un nuevo candidato con todos sus datos relacionados.
     *
     * POST /api/candidatos
     */
    @PostMapping
    public ResponseEntity<CandidatoResponse> crearCandidato(
            @Valid @RequestBody CandidatoRequest request) {
        log.info("Recibida peticion para crear candidato con documento: {}", request.getDocumentoIdentidad());
        CandidatoResponse response = candidatoService.crearCandidato(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Actualiza un candidato existente con todos sus datos relacionados.
     *
     * PUT /api/candidatos/{id}
     */
    @PutMapping("/{id}")
    public ResponseEntity<CandidatoResponse> actualizarCandidato(
            @PathVariable Long id,
            @Valid @RequestBody CandidatoRequest request) {
        log.info("Recibida peticion para actualizar candidato con ID: {}", id);
        CandidatoResponse response = candidatoService.actualizarCandidato(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un candidato por su ID.
     *
     * GET /api/candidatos/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CandidatoResponse> obtenerPorId(@PathVariable Long id) {
        log.info("Recibida peticion para obtener candidato con ID: {}", id);
        CandidatoResponse response = candidatoService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene un candidato por su documento de identidad.
     *
     * GET /api/candidatos/documento/{documento}
     */
    @GetMapping("/documento/{documento}")
    public ResponseEntity<CandidatoResponse> obtenerPorDocumento(@PathVariable String documento) {
        log.info("Recibida peticion para obtener candidato con documento: {}", documento);
        CandidatoResponse response = candidatoService.obtenerPorDocumento(documento);
        return ResponseEntity.ok(response);
    }

    /**
     * Obtiene todos los candidatos.
     *
     * GET /api/candidatos
     */
    @GetMapping
    public ResponseEntity<List<CandidatoResponse>> obtenerTodos() {
        log.info("Recibida peticion para obtener todos los candidatos");
        List<CandidatoResponse> response = candidatoService.obtenerTodos();
        return ResponseEntity.ok(response);
    }

    /**
     * Elimina un candidato por su ID.
     *
     * DELETE /api/candidatos/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPorId(@PathVariable Long id) {
        log.info("Recibida peticion para eliminar candidato con ID: {}", id);
        candidatoService.eliminarPorId(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Descarga un PDF con la información completa de un candidato.
     *
     * GET /api/candidatos/{id}/pdf
     */
    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Long id) {
        log.info("Recibida peticion para descargar PDF del candidato con ID: {}", id);
        try {
            CandidatoResponse candidato = candidatoService.obtenerPorId(id);
            byte[] pdfBytes = candidatoPdfService.generateCandidatoPdf(candidato);

            String fileName = String.format("candidato_%s_%s.pdf",
                candidato.getDocumentoIdentidad(),
                System.currentTimeMillis());

            return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
        } catch (Exception e) {
            log.error("Error al generar PDF para candidato {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
