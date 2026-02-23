package com.horizonx.recruiting_microservices.dto;

import com.horizonx.recruiting_microservices.model.entity.Candidato;
import com.horizonx.recruiting_microservices.model.entity.Educacion;
import com.horizonx.recruiting_microservices.model.entity.ExperienciaLaboral;
import com.horizonx.recruiting_microservices.model.entity.ContactoEmergencia;
import com.horizonx.recruiting_microservices.model.entity.ReferenciaPersonal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * DTO de respuesta con todos los datos del candidato y sus relacionadas.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandidatoResponse {

    private Long id;
    private String documentoIdentidad;
    private String nombre1;
    private String nombre2;
    private String apellido1;
    private String apellido2;
    private LocalDate fechaNacimiento;
    private Integer edad;
    private String correoElectronico;
    private String telefono;
    private String celular;
    private String direccion;

    // Información de catálogos
    private Long sexoId;
    private String sexo;
    private Long estadoCivilId;
    private String estadoCivil;
    private Long nivelEstudioId;
    private String nivelEstudio;
    private Long ciudadId;
    private String ciudad;
    private Long departamentoId;
    private String departamento;
    private Long barrioId;
    private String barrio;

    private String fuenteReclutamiento;
    private Candidato.EstadoCandidato estadoCandidato;
    private String notas;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaActualizacion;

    // Listas de entidades relacionadas
    private List<ExperienciaLaboralResponse> experienciasLaborales;
    private List<EducacionResponse> educaciones;
    private List<ReferenciaPersonalResponse> referenciasPersonales;
    private List<ContactoEmergenciaResponse> contactosEmergencia;

    /**
     * Convierte una entidad Candidato a CandidatoResponse.
     */
    public static CandidatoResponse fromEntity(Candidato candidato) {
        CandidatoResponseBuilder builder = CandidatoResponse.builder()
                .id(candidato.getId())
                .documentoIdentidad(candidato.getDocumentoIdentidad())
                .nombre1(candidato.getNombre1())
                .nombre2(candidato.getNombre2())
                .apellido1(candidato.getApellido1())
                .apellido2(candidato.getApellido2())
                .fechaNacimiento(candidato.getFechaNacimiento())
                .edad(candidato.getEdad())
                .correoElectronico(candidato.getCorreoElectronico())
                .telefono(candidato.getTelefono())
                .celular(candidato.getCelular())
                .direccion(candidato.getDireccion())
                .fuenteReclutamiento(candidato.getFuenteReclutamiento())
                .estadoCandidato(candidato.getEstadoCandidato())
                .notas(candidato.getNotas())
                .fechaRegistro(candidato.getFechaRegistro())
                .fechaActualizacion(candidato.getFechaActualizacion());

        // Información de catálogos
        if (candidato.getSexo() != null) {
            builder.sexoId(candidato.getSexo().getId());
            builder.sexo(candidato.getSexo().getNombre());
        }
        if (candidato.getEstadoCivil() != null) {
            builder.estadoCivilId(candidato.getEstadoCivil().getId());
            builder.estadoCivil(candidato.getEstadoCivil().getNombre());
        }
        if (candidato.getNivelEstudio() != null) {
            builder.nivelEstudioId(candidato.getNivelEstudio().getId());
            builder.nivelEstudio(candidato.getNivelEstudio().getNombre());
        }
        if (candidato.getCiudad() != null) {
            builder.ciudadId(candidato.getCiudad().getId());
            builder.ciudad(candidato.getCiudad().getNombre());
            if (candidato.getCiudad().getDepartamento() != null) {
                builder.departamentoId(candidato.getCiudad().getDepartamento().getId());
                builder.departamento(candidato.getCiudad().getDepartamento().getNombre());
            }
        }
        if (candidato.getBarrio() != null) {
            builder.barrioId(candidato.getBarrio().getId());
            builder.barrio(candidato.getBarrio().getNombre());
        }

        // Conversión de listas
        if (candidato.getExperienciasLaborales() != null) {
            builder.experienciasLaborales(
                    candidato.getExperienciasLaborales().stream()
                            .map(ExperienciaLaboralResponse::fromEntity)
                            .collect(Collectors.toList())
            );
        }

        if (candidato.getEducaciones() != null) {
            builder.educaciones(
                    candidato.getEducaciones().stream()
                            .map(EducacionResponse::fromEntity)
                            .collect(Collectors.toList())
            );
        }

        if (candidato.getReferenciasPersonales() != null) {
            builder.referenciasPersonales(
                    candidato.getReferenciasPersonales().stream()
                            .map(ReferenciaPersonalResponse::fromEntity)
                            .collect(Collectors.toList())
            );
        }

        if (candidato.getContactosEmergencia() != null) {
            builder.contactosEmergencia(
                    candidato.getContactosEmergencia().stream()
                            .map(ContactoEmergenciaResponse::fromEntity)
                            .collect(Collectors.toList())
            );
        }

        return builder.build();
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExperienciaLaboralResponse {
        private Long id;
        private String empresa;
        private String cargo;
        private String descripcionFunciones;
        private String nombreJefeInmediato;
        private String telefonoEmpresa;
        private LocalDate fechaIngreso;
        private LocalDate fechaRetiro;
        private Boolean esActual;
        private String motivoRetiro;

        public static ExperienciaLaboralResponse fromEntity(ExperienciaLaboral entity) {
            return ExperienciaLaboralResponse.builder()
                    .id(entity.getId())
                    .empresa(entity.getEmpresa())
                    .cargo(entity.getCargo())
                    .descripcionFunciones(entity.getDescripcionFunciones())
                    .nombreJefeInmediato(entity.getNombreJefeInmediato())
                    .telefonoEmpresa(entity.getTelefonoEmpresa())
                    .fechaIngreso(entity.getFechaIngreso())
                    .fechaRetiro(entity.getFechaRetiro())
                    .esActual(entity.getEsActual())
                    .motivoRetiro(entity.getMotivoRetiro())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EducacionResponse {
        private Long id;
        private String nivelEstudio;
        private String institucion;
        private String tituloObtenido;
        private LocalDate fechaInicio;
        private LocalDate fechaFin;
        private Educacion.EstadoEducacion estado;
        private String notas;

        public static EducacionResponse fromEntity(Educacion entity) {
            EducacionResponseBuilder builder = EducacionResponse.builder()
                    .id(entity.getId())
                    .institucion(entity.getInstitucion())
                    .tituloObtenido(entity.getTituloObtenido())
                    .fechaInicio(entity.getFechaInicio())
                    .fechaFin(entity.getFechaFin())
                    .estado(entity.getEstado())
                    .notas(entity.getNotas());

            if (entity.getNivelEstudio() != null) {
                builder.nivelEstudio(entity.getNivelEstudio().getNombre());
            }

            return builder.build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReferenciaPersonalResponse {
        private Long id;
        private String nombreCompleto;
        private String parentesco;
        private String telefono;
        private String correo;
        private ReferenciaPersonal.TipoReferencia tipoReferencia;

        public static ReferenciaPersonalResponse fromEntity(ReferenciaPersonal entity) {
            return ReferenciaPersonalResponse.builder()
                    .id(entity.getId())
                    .nombreCompleto(entity.getNombreCompleto())
                    .parentesco(entity.getParentesco())
                    .telefono(entity.getTelefono())
                    .correo(entity.getCorreo())
                    .tipoReferencia(entity.getTipoReferencia())
                    .build();
        }
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ContactoEmergenciaResponse {
        private Long id;
        private String nombreCompleto;
        private String parentesco;
        private String telefono;
        private String celular;
        private String direccion;

        public static ContactoEmergenciaResponse fromEntity(ContactoEmergencia entity) {
            return ContactoEmergenciaResponse.builder()
                    .id(entity.getId())
                    .nombreCompleto(entity.getNombreCompleto())
                    .parentesco(entity.getParentesco())
                    .telefono(entity.getTelefono())
                    .celular(entity.getCelular())
                    .direccion(entity.getDireccion())
                    .build();
        }
    }
}
