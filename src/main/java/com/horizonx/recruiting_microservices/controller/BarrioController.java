package com.horizonx.recruiting_microservices.controller;

import com.horizonx.recruiting_microservices.dto.BarrioResponse;
import com.horizonx.recruiting_microservices.service.BarrioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/barrios")
@RequiredArgsConstructor
@Slf4j
public class BarrioController {

    private final BarrioService barrioService;

    @GetMapping
    public ResponseEntity<List<BarrioResponse>> obtenerTodos() {
        log.info("Recibida peticion para obtener todos los barrios");
        return ResponseEntity.ok(barrioService.obtenerTodos());
    }

    @PostMapping
    public ResponseEntity<BarrioResponse> crear(@RequestBody Map<String, Object> request) {
        log.info("Recibida peticion para crear nuevo barrio");
        String nombre = (String) request.get("nombre");
        Long ciudadId = Long.valueOf(request.get("ciudadId").toString());
        return ResponseEntity.ok(barrioService.crear(nombre, ciudadId));
    }
}
