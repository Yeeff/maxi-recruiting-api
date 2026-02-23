package com.horizonx.recruiting_microservices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para Ciudad (catálogo geográfico).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CiudadResponse {

    private Long id;
    private String nombre;
    private String codigo;
    private Long departamentoId;
    private String departamentoNombre;
}
