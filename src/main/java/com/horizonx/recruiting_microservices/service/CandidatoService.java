package com.horizonx.recruiting_microservices.service;

import com.horizonx.recruiting_microservices.dto.CandidatoRequest;
import com.horizonx.recruiting_microservices.dto.CandidatoResponse;
import com.horizonx.recruiting_microservices.exception.ResourceNotFoundException;
import com.horizonx.recruiting_microservices.model.entity.*;
import com.horizonx.recruiting_microservices.model.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio unificado para gestionar candidatos con todas sus relaciones.
 * Maneja creación, actualización, eliminación y consulta de candidatos.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CandidatoService {

    private final CandidatoRepository candidatoRepository;
    private final SexoRepository sexoRepository;
    private final EstadoCivilRepository estadoCivilRepository;
    private final NivelEstudioRepository nivelEstudioRepository;
    private final CiudadRepository ciudadRepository;
    private final BarrioRepository barrioRepository;

    /**
     * Crea un nuevo candidato con todas sus relaciones.
     * Guarda automáticamente experiencias, educaciones, referencias y contactos de emergencia.
     */
    @Transactional
    public CandidatoResponse crearCandidato(CandidatoRequest request) {
        log.info("Creando nuevo candidato con documento: {}", request.getDocumentoIdentidad());

        // Validar documento único
        if (candidatoRepository.existsByDocumentoIdentidad(request.getDocumentoIdentidad())) {
            throw new IllegalArgumentException("Ya existe un candidato con el documento: " + request.getDocumentoIdentidad());
        }

        // Construir entidad Candidato
        Candidato candidato = construirCandidato(request);

        // Guardar (cascade guardará las listas automáticamente)
        Candidato saved = candidatoRepository.save(candidato);
        log.info("Candidato creado con ID: {}", saved.getId());

        return CandidatoResponse.fromEntity(saved);
    }

    /**
     * Actualiza un candidato existente con todas sus relaciones.
     * Reemplaza completamente las listas de experiencias, educaciones, referencias y contactos.
     */
    @Transactional
    public CandidatoResponse actualizarCandidato(Long id, CandidatoRequest request) {
        log.info("Actualizando candidato con ID: {}", id);

        Candidato existente = candidatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato no encontrado con ID: " + id));

        // Validar documento único (si cambió)
        if (!existente.getDocumentoIdentidad().equals(request.getDocumentoIdentidad()) &&
            candidatoRepository.existsByDocumentoIdentidad(request.getDocumentoIdentidad())) {
            throw new IllegalArgumentException("Ya existe un candidato con el documento: " + request.getDocumentoIdentidad());
        }

        // Actualizar datos básicos
        actualizarDatosBasicos(existente, request);

        // Limpiar y reconstruir relaciones
        existente.getExperienciasLaborales().clear();
        existente.getEducaciones().clear();
        existente.getReferenciasPersonales().clear();
        existente.getContactosEmergencia().clear();

        agregarRelaciones(existente, request);

        Candidato updated = candidatoRepository.save(existente);
        log.info("Candidato actualizado con ID: {}", updated.getId());

        return CandidatoResponse.fromEntity(updated);
    }

    /**
     * Obtiene un candidato por ID con todas sus relaciones.
     */
    @Transactional(readOnly = true)
    public CandidatoResponse obtenerPorId(Long id) {
        log.info("Obteniendo candidato con ID: {}", id);

        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato no encontrado con ID: " + id));

        return CandidatoResponse.fromEntity(candidato);
    }

    /**
     * Obtiene un candidato por documento de identidad.
     */
    @Transactional(readOnly = true)
    public CandidatoResponse obtenerPorDocumento(String documento) {
        log.info("Obteniendo candidato con documento: {}", documento);

        Candidato candidato = candidatoRepository.findByDocumentoIdentidad(documento)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato no encontrado con documento: " + documento));

        return CandidatoResponse.fromEntity(candidato);
    }

    /**
     * Obtiene todos los candidatos.
     */
    @Transactional(readOnly = true)
    public List<CandidatoResponse> obtenerTodos() {
        log.info("Obteniendo todos los candidatos");

        return candidatoRepository.findAll().stream()
                .map(CandidatoResponse::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Elimina un candidato por ID.
     */
    @Transactional
    public void eliminarPorId(Long id) {
        log.info("Eliminando candidato con ID: {}", id);

        if (!candidatoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Candidato no encontrado con ID: " + id);
        }

        candidatoRepository.deleteById(id);
        log.info("Candidato eliminado con ID: {}", id);
    }

    /**
     * Construye la entidad Candidato desde el request.
     */
    private Candidato construirCandidato(CandidatoRequest request) {
        Candidato candidato = Candidato.builder()
                .documentoIdentidad(request.getDocumentoIdentidad())
                .nombre1(request.getNombre1())
                .nombre2(request.getNombre2())
                .apellido1(request.getApellido1())
                .apellido2(request.getApellido2())
                .fechaNacimiento(request.getFechaNacimiento())
                .edad(calcularEdad(request.getFechaNacimiento()))
                .correoElectronico(request.getCorreoElectronico())
                .telefono(request.getTelefono())
                .celular(request.getCelular())
                .direccion(request.getDireccion())
                .fuenteReclutamiento(request.getFuenteReclutamiento())
                .notas(request.getNotas())
                .estadoCandidato(Candidato.EstadoCandidato.POSTULADO)
                .build();

        // Asignar relaciones de catálogos
        if (request.getSexoId() != null) {
            candidato.setSexo(sexoRepository.findById(request.getSexoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sexo no encontrado con ID: " + request.getSexoId())));
        }
        if (request.getEstadoCivilId() != null) {
            candidato.setEstadoCivil(estadoCivilRepository.findById(request.getEstadoCivilId())
                    .orElseThrow(() -> new ResourceNotFoundException("Estado civil no encontrado con ID: " + request.getEstadoCivilId())));
        }
        if (request.getNivelEstudioId() != null) {
            candidato.setNivelEstudio(nivelEstudioRepository.findById(request.getNivelEstudioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Nivel de estudio no encontrado con ID: " + request.getNivelEstudioId())));
        }
        if (request.getCiudadId() != null) {
            candidato.setCiudad(ciudadRepository.findById(request.getCiudadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ciudad no encontrada con ID: " + request.getCiudadId())));
        }
        if (request.getBarrioId() != null) {
            candidato.setBarrio(barrioRepository.findById(request.getBarrioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Barrio no encontrado con ID: " + request.getBarrioId())));
        }

        // Agregar relaciones
        agregarRelaciones(candidato, request);

        return candidato;
    }

    /**
     * Actualiza los datos básicos de un candidato existente.
     */
    private void actualizarDatosBasicos(Candidato candidato, CandidatoRequest request) {
        candidato.setDocumentoIdentidad(request.getDocumentoIdentidad());
        candidato.setNombre1(request.getNombre1());
        candidato.setNombre2(request.getNombre2());
        candidato.setApellido1(request.getApellido1());
        candidato.setApellido2(request.getApellido2());
        candidato.setFechaNacimiento(request.getFechaNacimiento());
        candidato.setEdad(calcularEdad(request.getFechaNacimiento()));
        candidato.setCorreoElectronico(request.getCorreoElectronico());
        candidato.setTelefono(request.getTelefono());
        candidato.setCelular(request.getCelular());
        candidato.setDireccion(request.getDireccion());
        candidato.setFuenteReclutamiento(request.getFuenteReclutamiento());
        candidato.setNotas(request.getNotas());

        // Actualizar relaciones de catálogos
        if (request.getSexoId() != null) {
            candidato.setSexo(sexoRepository.findById(request.getSexoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Sexo no encontrado con ID: " + request.getSexoId())));
        }
        if (request.getEstadoCivilId() != null) {
            candidato.setEstadoCivil(estadoCivilRepository.findById(request.getEstadoCivilId())
                    .orElseThrow(() -> new ResourceNotFoundException("Estado civil no encontrado con ID: " + request.getEstadoCivilId())));
        }
        if (request.getNivelEstudioId() != null) {
            candidato.setNivelEstudio(nivelEstudioRepository.findById(request.getNivelEstudioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Nivel de estudio no encontrado con ID: " + request.getNivelEstudioId())));
        }
        if (request.getCiudadId() != null) {
            candidato.setCiudad(ciudadRepository.findById(request.getCiudadId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ciudad no encontrada con ID: " + request.getCiudadId())));
        }
        if (request.getBarrioId() != null) {
            candidato.setBarrio(barrioRepository.findById(request.getBarrioId())
                    .orElseThrow(() -> new ResourceNotFoundException("Barrio no encontrado con ID: " + request.getBarrioId())));
        }
    }

    /**
     * Agrega las listas de relaciones a un candidato.
     */
    private void agregarRelaciones(Candidato candidato, CandidatoRequest request) {
        // Experiencias laborales
        if (request.getExperienciasLaborales() != null) {
            for (CandidatoRequest.ExperienciaLaboralRequest expReq : request.getExperienciasLaborales()) {
                ExperienciaLaboral experiencia = ExperienciaLaboral.builder()
                        .empresa(expReq.getEmpresa())
                        .cargo(expReq.getCargo())
                        .descripcionFunciones(expReq.getDescripcionFunciones())
                        .nombreJefeInmediato(expReq.getNombreJefeInmediato())
                        .telefonoEmpresa(expReq.getTelefonoEmpresa())
                        .fechaIngreso(expReq.getFechaIngreso())
                        .fechaRetiro(expReq.getFechaRetiro())
                        .esActual(expReq.getEsActual())
                        .motivoRetiro(expReq.getMotivoRetiro())
                        .build();
                experiencia.setCandidato(candidato);
                candidato.getExperienciasLaborales().add(experiencia);
            }
        }

        // Educaciones
        if (request.getEducaciones() != null) {
            for (CandidatoRequest.EducacionRequest eduReq : request.getEducaciones()) {
                Educacion educacion = Educacion.builder()
                        .institucion(eduReq.getInstitucion())
                        .tituloObtenido(eduReq.getTituloObtenido())
                        .fechaInicio(eduReq.getFechaInicio())
                        .fechaFin(eduReq.getFechaFin())
                        .estado(eduReq.getEstado())
                        .notas(eduReq.getNotas())
                        .build();

                if (eduReq.getNivelEstudioId() != null) {
                    educacion.setNivelEstudio(nivelEstudioRepository.findById(eduReq.getNivelEstudioId())
                            .orElseThrow(() -> new ResourceNotFoundException("Nivel de estudio no encontrado con ID: " + eduReq.getNivelEstudioId())));
                }

                educacion.setCandidato(candidato);
                candidato.getEducaciones().add(educacion);
            }
        }

        // Referencias personales
        if (request.getReferenciasPersonales() != null) {
            for (CandidatoRequest.ReferenciaPersonalRequest refReq : request.getReferenciasPersonales()) {
                ReferenciaPersonal referencia = ReferenciaPersonal.builder()
                        .nombreCompleto(refReq.getNombreCompleto())
                        .parentesco(refReq.getParentesco())
                        .telefono(refReq.getTelefono())
                        .correo(refReq.getCorreo())
                        .tipoReferencia(refReq.getTipoReferencia())
                        .build();
                referencia.setCandidato(candidato);
                candidato.getReferenciasPersonales().add(referencia);
            }
        }

        // Contactos de emergencia
        if (request.getContactosEmergencia() != null) {
            for (CandidatoRequest.ContactoEmergenciaRequest contReq : request.getContactosEmergencia()) {
                ContactoEmergencia contacto = ContactoEmergencia.builder()
                        .nombreCompleto(contReq.getNombreCompleto())
                        .parentesco(contReq.getParentesco())
                        .telefono(contReq.getTelefono())
                        .celular(contReq.getCelular())
                        .direccion(contReq.getDireccion())
                        .build();
                contacto.setCandidato(candidato);
                candidato.getContactosEmergencia().add(contacto);
            }
        }
    }

    /**
     * Calcula la edad a partir de la fecha de nacimiento.
     */
    private Integer calcularEdad(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            return null;
        }
        return Period.between(fechaNacimiento, LocalDate.now()).getYears();
    }
}
