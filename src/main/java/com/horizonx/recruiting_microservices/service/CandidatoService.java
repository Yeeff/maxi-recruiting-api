package com.horizonx.recruiting_microservices.service;


import com.horizonx.recruiting_microservices.dto.CandidatoRequest;
import com.horizonx.recruiting_microservices.dto.CandidatoResponse;
import com.horizonx.recruiting_microservices.dto.EstadoUpdateRequest;
import com.horizonx.recruiting_microservices.dto.PageResponse;
import com.horizonx.recruiting_microservices.dto.export.CandidatoExportDto;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;
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
    private final CargoRepository cargoRepository;

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
     * Busca candidatos con filtros por documento, estado y/o fecha de registro con paginacion.
     */
    @Transactional(readOnly = true)
    public PageResponse<CandidatoResponse> buscarCandidatos(String documentoIdentidad, String estado, String fechaDesde, String fechaHasta, Long cargoId, int page, int size) {
        log.info("Buscando candidatos con filtros - documento: {}, estado: {}, fechaDesde: {}, fechaHasta: {}, cargoId: {}, page: {}, size: {}", 
            documentoIdentidad, estado, fechaDesde, fechaHasta, cargoId, page, size);

        List<Candidato> todosCandidatos;

        if ((documentoIdentidad != null && !documentoIdentidad.isBlank()) || 
            (estado != null && !estado.isBlank()) ||
            (fechaDesde != null && !fechaDesde.isBlank()) ||
            (fechaHasta != null && !fechaHasta.isBlank()) ||
            (cargoId != null)) {
            
            java.time.LocalDate desde = null;
            java.time.LocalDate hasta = null;
            
            if (fechaDesde != null && !fechaDesde.isBlank()) {
                desde = java.time.LocalDate.parse(fechaDesde);
            }
            if (fechaHasta != null && !fechaHasta.isBlank()) {
                hasta = java.time.LocalDate.parse(fechaHasta);
            }
            
            final java.time.LocalDate desdeFinal = desde;
            final java.time.LocalDate hastaFinal = hasta;
            
            todosCandidatos = candidatoRepository.findAll().stream()
                .filter(c -> {
                    boolean matchesDocumento = documentoIdentidad == null || 
                        documentoIdentidad.isBlank() || 
                        c.getDocumentoIdentidad() != null && 
                        c.getDocumentoIdentidad().toLowerCase().contains(documentoIdentidad.toLowerCase());
                    
                    boolean matchesEstado = estado == null || 
                        estado.isBlank() || 
                        c.getEstadoCandidato() != null && 
                        c.getEstadoCandidato().name().equalsIgnoreCase(estado);
                    
                    boolean matchesFechaDesde = desdeFinal == null || 
                        c.getFechaRegistro() == null ||
                        !c.getFechaRegistro().toLocalDate().isBefore(desdeFinal);
                    
                    boolean matchesFechaHasta = hastaFinal == null || 
                        c.getFechaRegistro() == null ||
                        !c.getFechaRegistro().toLocalDate().isAfter(hastaFinal);
                    
                    boolean matchesCargo = cargoId == null || 
                        c.getCargo() != null && 
                        c.getCargo().getId().equals(cargoId);
                    
                    return matchesDocumento && matchesEstado && matchesFechaDesde && matchesFechaHasta && matchesCargo;
                })
                .collect(Collectors.toList());
        } else {
            todosCandidatos = candidatoRepository.findAll();
        }

        long totalRecords = todosCandidatos.size();
        int totalPages = (int) Math.ceil((double) totalRecords / size);
        
        List<CandidatoResponse> paginatedData = todosCandidatos.stream()
            .skip((long) page * size)
            .limit(size)
            .map(CandidatoResponse::fromEntity)
            .collect(Collectors.toList());

        return PageResponse.<CandidatoResponse>builder()
            .data(paginatedData)
            .currentPage(page)
            .pageSize(size)
            .totalRecords(totalRecords)
            .totalPages(totalPages)
            .build();
    }

    @Transactional(readOnly = true)
    public byte[] exportarCandidatos(String documentoIdentidad, String estado, String fechaDesde, String fechaHasta, Long cargoId) {
        log.info("Exportando candidatos con filtros - documento: {}, estado: {}, fechaDesde: {}, fechaHasta: {}, cargoId: {}", 
            documentoIdentidad, estado, fechaDesde, fechaHasta, cargoId);

        List<CandidatoExportDto> exportData = buscarCandidatosFiltrados(documentoIdentidad, estado, fechaDesde, fechaHasta, cargoId)
            .stream()
            .map(this::toExportDto)
            .collect(Collectors.toList());
            
        log.info("✅ Generando archivo XLSX real con {} registros", exportData.size());

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            
            org.apache.poi.xssf.usermodel.XSSFSheet sheet = workbook.createSheet("Candidatos");
            
            // Crear estilo para encabezados
            org.apache.poi.xssf.usermodel.XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            org.apache.poi.xssf.usermodel.XSSFCellStyle headerStyle = workbook.createCellStyle();
            headerStyle.setFont(headerFont);
            
            // Encabezados
            String[] headers = {
                "Documento", "Primer Nombre", "Segundo Nombre", "Primer Apellido", 
                "Segundo Apellido", "Correo Electrónico", "Teléfono", "Celular", 
                "Cargo", "Estado", "Fecha Registro"
            };
            
            org.apache.poi.xssf.usermodel.XSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.xssf.usermodel.XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 18 * 256);
            }
            
            // Formato fecha
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            
            // Datos
            int rowNum = 1;
            for (CandidatoExportDto dto : exportData) {
                org.apache.poi.xssf.usermodel.XSSFRow row = sheet.createRow(rowNum++);
                
                row.createCell(0).setCellValue(dto.getDocumentoIdentidad() != null ? dto.getDocumentoIdentidad() : "");
                row.createCell(1).setCellValue(dto.getNombre1() != null ? dto.getNombre1() : "");
                row.createCell(2).setCellValue(dto.getNombre2() != null ? dto.getNombre2() : "");
                row.createCell(3).setCellValue(dto.getApellido1() != null ? dto.getApellido1() : "");
                row.createCell(4).setCellValue(dto.getApellido2() != null ? dto.getApellido2() : "");
                row.createCell(5).setCellValue(dto.getCorreoElectronico() != null ? dto.getCorreoElectronico() : "");
                row.createCell(6).setCellValue(dto.getTelefono() != null ? dto.getTelefono() : "");
                row.createCell(7).setCellValue(dto.getCelular() != null ? dto.getCelular() : "");
                row.createCell(8).setCellValue(dto.getCargo() != null ? dto.getCargo() : "");
                row.createCell(9).setCellValue(dto.getEstado() != null ? dto.getEstado() : "");
                row.createCell(10).setCellValue(dto.getFechaRegistro() != null ? dto.getFechaRegistro().format(formatter) : "");
            }
            
            workbook.write(out);
            out.flush();
            
            byte[] bytes = out.toByteArray();
            log.info("✅ ✅ ✅ ARCHIVO XLSX GENERADO EXITOSAMENTE");
            log.info("✅ Tamaño total: {} bytes", bytes.length);
            log.info("✅ Primeros 4 bytes (deben ser 50 4B 03 04 = ZIP): {:02X} {:02X} {:02X} {:02X}", 
                bytes[0] & 0xFF, bytes[1] & 0xFF, bytes[2] & 0xFF, bytes[3] & 0xFF);
            
            // ✅ GUARDAMOS UNA COPIA EXACTA EN ESCRITORIO
            try {
                java.io.File debugFile = new java.io.File(System.getProperty("user.home"), "Desktop/candidatos_debug.xlsx");
                try (java.io.FileOutputStream fos = new java.io.FileOutputStream(debugFile)) {
                    fos.write(bytes);
                    fos.getFD().sync();
                }
                log.info("✅ ✅ ✅ ARCHIVO DEBUG GUARDADO EN: {}", debugFile.getAbsolutePath());
                log.info("✅ PRUEBA ESTE ARCHIVO PRIMERO:");
                log.info("✅ ✅ SI ESTE ABRE = PROBLEMA 100% EN FRONTEND / PROXY / NAVEGADOR");
                log.info("✅ ❌ SI ESTE TAMBIEN FALLA = PROBLEMA EN BACKEND");
            } catch (Exception ex) {
                log.warn("No se pudo guardar archivo debug: {}", ex.getMessage());
            }
            
            return bytes;
        } catch (Exception e) {
            log.error("❌ Error generando archivo Excel", e);
            throw new RuntimeException("Error al generar el archivo Excel: " + e.getMessage(), e);
        }
    }

    private List<Candidato> buscarCandidatosFiltrados(String documentoIdentidad, String estado, String fechaDesde, String fechaHasta, Long cargoId) {
        java.time.LocalDate desde = fechaDesde != null && !fechaDesde.isBlank() ? java.time.LocalDate.parse(fechaDesde) : null;
        java.time.LocalDate hasta = fechaHasta != null && !fechaHasta.isBlank() ? java.time.LocalDate.parse(fechaHasta) : null;
        
        return candidatoRepository.findAll().stream()
            .filter(c -> {
                boolean matchesDocumento = documentoIdentidad == null || documentoIdentidad.isBlank() || 
                    c.getDocumentoIdentidad() != null && c.getDocumentoIdentidad().toLowerCase().contains(documentoIdentidad.toLowerCase());
                
                boolean matchesEstado = estado == null || estado.isBlank() || 
                    c.getEstadoCandidato() != null && c.getEstadoCandidato().name().equalsIgnoreCase(estado);
                
                boolean matchesFechaDesde = desde == null || c.getFechaRegistro() == null ||
                    !c.getFechaRegistro().toLocalDate().isBefore(desde);
                
                boolean matchesFechaHasta = hasta == null || c.getFechaRegistro() == null ||
                    !c.getFechaRegistro().toLocalDate().isAfter(hasta);
                
                boolean matchesCargo = cargoId == null || 
                    c.getCargo() != null && c.getCargo().getId().equals(cargoId);
                
                return matchesDocumento && matchesEstado && matchesFechaDesde && matchesFechaHasta && matchesCargo;
            })
            .collect(Collectors.toList());
    }

    private CandidatoExportDto toExportDto(Candidato candidato) {
        return CandidatoExportDto.builder()
            .id(candidato.getId())
            .documentoIdentidad(candidato.getDocumentoIdentidad())
            .nombre1(candidato.getNombre1())
            .nombre2(candidato.getNombre2())
            .apellido1(candidato.getApellido1())
            .apellido2(candidato.getApellido2())
            .correoElectronico(candidato.getCorreoElectronico())
            .telefono(candidato.getTelefono())
            .celular(candidato.getCelular())
            .cargo(candidato.getCargo() != null ? candidato.getCargo().getNombre() : "-")
            .estado(candidato.getEstadoCandidato() != null ? candidato.getEstadoCandidato().name() : "-")
            .fechaRegistro(candidato.getFechaRegistro())
            .build();
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
     * Actualiza el estado de un candidato.
     */
    @Transactional
    public CandidatoResponse actualizarEstado(Long id, Candidato.EstadoCandidato nuevoEstado) {
        log.info("Actualizando estado del candidato con ID: {} a {}", id, nuevoEstado);

        Candidato candidato = candidatoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Candidato no encontrado con ID: " + id));

        candidato.setEstadoCandidato(nuevoEstado);
        Candidato updated = candidatoRepository.save(candidato);
        log.info("Estado del candidato actualizado a: {}", nuevoEstado);

        return CandidatoResponse.fromEntity(updated);
    }

    /**
     * Construye la entidad Candidato desde el request.
     */
    private Candidato construirCandidato(CandidatoRequest request) {
        Candidato.EstadoCandidato estadoInicial = Candidato.EstadoCandidato.INGRESADO;
        
        if (request.getSexoId() != null) {
            Sexo sexo = sexoRepository.findById(request.getSexoId()).orElse(null);
            if (sexo != null) {
                Long idSexo = sexo.getId();
                if (idSexo == 1L) {
                    estadoInicial = Candidato.EstadoCandidato.BACKUP_HOMBRE;
                } else if (idSexo == 2L) {
                    estadoInicial = Candidato.EstadoCandidato.BACKUP_MUJER;
                } else if (sexo.getNombre() != null) {
                    String nombreSexo = sexo.getNombre().toLowerCase();
                    if (nombreSexo.contains("hombre") || nombreSexo.equals("m")) {
                        estadoInicial = Candidato.EstadoCandidato.BACKUP_HOMBRE;
                    } else if (nombreSexo.contains("mujer") || nombreSexo.equals("f")) {
                        estadoInicial = Candidato.EstadoCandidato.BACKUP_MUJER;
                    }
                }
            }
        }
        
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
                .estadoCandidato(estadoInicial)
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
        if (request.getCargoId() != null) {
            candidato.setCargo(cargoRepository.findById(request.getCargoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cargo no encontrado con ID: " + request.getCargoId())));
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
        if (request.getCargoId() != null) {
            candidato.setCargo(cargoRepository.findById(request.getCargoId())
                    .orElseThrow(() -> new ResourceNotFoundException("Cargo no encontrado con ID: " + request.getCargoId())));
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
