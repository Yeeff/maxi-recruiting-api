package com.horizonx.recruiting_microservices.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoUpdateRequest {

    @NotBlank(message = "El estado es requerido")
    private String estado;
}
