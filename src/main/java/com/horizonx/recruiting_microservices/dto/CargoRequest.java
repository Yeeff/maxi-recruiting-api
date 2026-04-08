package com.horizonx.recruiting_microservices.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para crear un nuevo Cargo.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoRequest {

    @NotBlank(message = "El nombre del cargo es obligatorio")
    private String nombre;
    
    private String descripcion;
}
