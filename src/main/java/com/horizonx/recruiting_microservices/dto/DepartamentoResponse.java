package com.horizonx.recruiting_microservices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para Departamento (catálogo geográfico).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DepartamentoResponse {

    private Long id;
    private String nombre;
    private String codigo;
}
