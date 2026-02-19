package com.horizonx.recruiting_microservices.dto;

import com.horizonx.recruiting_microservices.model.entity.Observacion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO para crear o actualizar una observación.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObservacionRequest {

    @NotBlank(message = "La observación no puede estar vacía")
    private String observacion;

    @NotNull(message = "El tipo de observación es requerido")
    private Observacion.TipoObservacion tipoObservacion;

    private Boolean esInterna;
}
