package com.horizonx.recruiting_microservices.dto;

import com.horizonx.recruiting_microservices.model.entity.Observacion;
import lombok.*;

import java.time.LocalDateTime;

/**
 * DTO para retornar información de una observación.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObservacionResponse {

    private Long id;
    private Long candidatoId;
    private String observacion;
    private Observacion.TipoObservacion tipoObservacion;
    private Boolean esInterna;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Convierte una entidad Observacion a ObservacionResponse.
     */
    public static ObservacionResponse fromEntity(Observacion observacion) {
        return ObservacionResponse.builder()
                .id(observacion.getId())
                .candidatoId(observacion.getCandidato().getId())
                .observacion(observacion.getObservacion())
                .tipoObservacion(observacion.getTipoObservacion())
                .esInterna(observacion.getEsInterna())
                .createdAt(observacion.getCreatedAt())
                .updatedAt(observacion.getUpdatedAt())
                .build();
    }
}
