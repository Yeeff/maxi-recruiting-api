package com.horizonx.recruiting_microservices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para Sexo (catálogo).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SexoResponse {

    private Long id;
    private String nombre;
    private String descripcion;
}
