package com.horizonx.recruiting_microservices.dto.export;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidatoExportDto {

    private String documentoIdentidad;
    private String nombre1;
    private String nombre2;
    private String apellido1;
    private String apellido2;
    private String correoElectronico;
    private String telefono;
    private String celular;
    private String cargo;
    private String estado;
    private LocalDateTime fechaRegistro;
    private Long id;
}
