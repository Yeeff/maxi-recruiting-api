package com.horizonx.recruiting_microservices.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.horizonx.recruiting_microservices.model.entity.Educacion;
import com.horizonx.recruiting_microservices.model.entity.ExperienciaLaboral;
import com.horizonx.recruiting_microservices.model.entity.ContactoEmergencia;
import com.horizonx.recruiting_microservices.model.entity.ReferenciaPersonal;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

/**
 * DTO para crear o actualizar un candidato con todos sus datos relacionados.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidatoRequest {

    // Datos básicos del candidato
    @NotBlank(message = "El documento de identidad es obligatorio")
    private String documentoIdentidad;

    @NotBlank(message = "El primer nombre es obligatorio")
    private String nombre1;

    private String nombre2;

    @NotBlank(message = "El primer apellido es obligatorio")
    private String apellido1;

    private String apellido2;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate fechaNacimiento;

    @Email(message = "El correo electrónico debe ser válido")
    private String correoElectronico;

    private String telefono;

    private String celular;

    private String direccion;

    // FK opcionales
    private Long barrioId;
    private Long ciudadId;

    @NotNull(message = "El sexo es obligatorio")
    private Long sexoId;

    private Long estadoCivilId;
    private Long nivelEstudioId;

    private String fuenteReclutamiento;
    private String notas;

    // Listas de entidades relacionadas (con validación)
    @Valid
    private List<ExperienciaLaboralRequest> experienciasLaborales;

    @Valid
    private List<EducacionRequest> educaciones;

    @Valid
    private List<ReferenciaPersonalRequest> referenciasPersonales;

    @Valid
    private List<ContactoEmergenciaRequest> contactosEmergencia;

    // DTOs anidados para entidades relacionadas
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExperienciaLaboralRequest {
        private String empresa;
        private String cargo;
        private String descripcionFunciones;
        private String nombreJefeInmediato;
        private String telefonoEmpresa;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate fechaIngreso;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate fechaRetiro;
        private Boolean esActual;
        private String motivoRetiro;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EducacionRequest {
        private Long nivelEstudioId;
        private String institucion;
        private String tituloObtenido;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate fechaInicio;
        @JsonFormat(pattern = "yyyy-MM-dd")
        private LocalDate fechaFin;
        private Educacion.EstadoEducacion estado;
        private String notas;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReferenciaPersonalRequest {
        @NotBlank(message = "El nombre de la referencia es obligatorio")
        private String nombreCompleto;
        private String parentesco;
        private String telefono;
        private String correo;
        private ReferenciaPersonal.TipoReferencia tipoReferencia;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactoEmergenciaRequest {
        @NotBlank(message = "El nombre del contacto de emergencia es obligatorio")
        private String nombreCompleto;
        private String parentesco;
        private String telefono;
        private String celular;
        private String direccion;
    }
}
