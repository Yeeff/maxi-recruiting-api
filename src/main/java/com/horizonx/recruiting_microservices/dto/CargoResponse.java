package com.horizonx.recruiting_microservices.dto;

import com.horizonx.recruiting_microservices.model.entity.Cargo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de respuesta para Cargo (catálogo).
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoResponse {

    private Long id;
    private String nombre;
    private String descripcion;

    public static CargoResponse fromEntity(Cargo cargo) {
        if (cargo == null) {
            return null;
        }
        return CargoResponse.builder()
                .id(cargo.getId())
                .nombre(cargo.getNombre())
                .descripcion(cargo.getDescripcion())
                .build();
    }
}
