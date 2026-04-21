package com.horizonx.recruiting_microservices.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BarrioResponse {
    private Long id;
    private String nombre;
    private Long ciudadId;
}
