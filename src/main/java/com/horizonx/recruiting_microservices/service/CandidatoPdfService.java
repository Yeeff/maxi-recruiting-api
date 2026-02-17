package com.horizonx.recruiting_microservices.service;

import com.horizonx.recruiting_microservices.dto.CandidatoResponse;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class CandidatoPdfService {

    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final float MARGIN = 50;
    private static final float LINE_HEIGHT = 15;
    private static final float TITLE_SIZE = 18;
    private static final float SECTION_SIZE = 14;
    private static final float NORMAL_SIZE = 11;

    public byte[] generateCandidatoPdf(CandidatoResponse candidato) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            PDType1Font boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font regularFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                float yPosition = page.getMediaBox().getHeight() - MARGIN;
                float pageWidth = page.getMediaBox().getWidth();

                // Title
                yPosition = drawText(contentStream, "CURRICULUM VITAE", MARGIN, yPosition, boldFont, TITLE_SIZE, true);
                yPosition -= 10;

                // Candidate Name
                String fullName = String.format("%s %s %s %s",
                    candidato.getNombre1() != null ? candidato.getNombre1() : "",
                    candidato.getNombre2() != null ? candidato.getNombre2() : "",
                    candidato.getApellido1() != null ? candidato.getApellido1() : "",
                    candidato.getApellido2() != null ? candidato.getApellido2() : ""
                ).trim();
                
                yPosition = drawText(contentStream, fullName.toUpperCase(), MARGIN, yPosition, boldFont, SECTION_SIZE, true);
                yPosition -= LINE_HEIGHT;

                // Personal Information Section
                yPosition = drawSectionTitle(contentStream, "INFORMACIÓN PERSONAL", MARGIN, yPosition, boldFont);
                yPosition = drawText(contentStream, String.format("Documento: %s", candidato.getDocumentoIdentidad()), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                yPosition = drawText(contentStream, String.format("Fecha de Nacimiento: %s", candidato.getFechaNacimiento() != null ? candidato.getFechaNacimiento().format(DATE_FORMAT) : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                yPosition = drawText(contentStream, String.format("Edad: %d años", candidato.getEdad() != null ? candidato.getEdad() : 0), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                yPosition = drawText(contentStream, String.format("Sexo: %s", candidato.getSexo() != null ? candidato.getSexo() : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                yPosition = drawText(contentStream, String.format("Estado Civil: %s", candidato.getEstadoCivil() != null ? candidato.getEstadoCivil() : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                yPosition -= LINE_HEIGHT;

                // Contact Information Section
                yPosition = drawSectionTitle(contentStream, "INFORMACIÓN DE CONTACTO", MARGIN, yPosition, boldFont);
                yPosition = drawText(contentStream, String.format("Correo: %s", candidato.getCorreoElectronico() != null ? candidato.getCorreoElectronico() : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                yPosition = drawText(contentStream, String.format("Teléfono: %s", candidato.getTelefono() != null ? candidato.getTelefono() : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                yPosition = drawText(contentStream, String.format("Celular: %s", candidato.getCelular() != null ? candidato.getCelular() : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                yPosition = drawText(contentStream, String.format("Dirección: %s", candidato.getDireccion() != null ? candidato.getDireccion() : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                
                String location = "";
                if (candidato.getBarrio() != null) {
                    location += candidato.getBarrio();
                }
                if (candidato.getCiudad() != null) {
                    location += (location.isEmpty() ? "" : ", ") + candidato.getCiudad();
                }
                if (candidato.getDepartamento() != null) {
                    location += (location.isEmpty() ? "" : ", ") + candidato.getDepartamento();
                }
                if (!location.isEmpty()) {
                    yPosition = drawText(contentStream, String.format("Ubicación: %s", location), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                }
                yPosition -= LINE_HEIGHT;

                // Professional Information
                yPosition = drawSectionTitle(contentStream, "INFORMACIÓN PROFESIONAL", MARGIN, yPosition, boldFont);
                yPosition = drawText(contentStream, String.format("Nivel de Estudio: %s", candidato.getNivelEstudio() != null ? candidato.getNivelEstudio() : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                yPosition = drawText(contentStream, String.format("Fuente de Reclutamiento: %s", candidato.getFuenteReclutamiento() != null ? candidato.getFuenteReclutamiento() : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                yPosition = drawText(contentStream, String.format("Estado: %s", candidato.getEstadoCandidato() != null ? candidato.getEstadoCandidato().toString() : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                yPosition -= LINE_HEIGHT;

                // Work Experience
                if (candidato.getExperienciasLaborales() != null && !candidato.getExperienciasLaborales().isEmpty()) {
                    yPosition = drawSectionTitle(contentStream, "EXPERIENCIA LABORAL", MARGIN, yPosition, boldFont);
                    
                    for (CandidatoResponse.ExperienciaLaboralResponse exp : candidato.getExperienciasLaborales()) {
                        if (yPosition < 100) {
                            // Add new page if needed
                            PDPage newPage = new PDPage(PDRectangle.A4);
                            document.addPage(newPage);
                            contentStream.close();
                            // This is a simplified approach - in production you'd want to handle this better
                        }
                        
                        yPosition = drawText(contentStream, String.format("Empresa: %s", exp.getEmpresa()), MARGIN, yPosition, boldFont, NORMAL_SIZE, false);
                        yPosition = drawText(contentStream, String.format("Cargo: %s", exp.getCargo()), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                        
                        String fechas = String.format("Período: %s - %s",
                            exp.getFechaIngreso() != null ? exp.getFechaIngreso().format(DATE_FORMAT) : "N/A",
                            exp.getEsActual() ? "Actual" : (exp.getFechaRetiro() != null ? exp.getFechaRetiro().format(DATE_FORMAT) : "N/A")
                        );
                        yPosition = drawText(contentStream, fechas, MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                        
                        if (exp.getDescripcionFunciones() != null && !exp.getDescripcionFunciones().isEmpty()) {
                            yPosition = drawText(contentStream, String.format("Funciones: %s", exp.getDescripcionFunciones()), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                        }
                        
                        yPosition -= LINE_HEIGHT;
                    }
                }

                // Education
                if (candidato.getEducaciones() != null && !candidato.getEducaciones().isEmpty()) {
                    yPosition -= LINE_HEIGHT;
                    yPosition = drawSectionTitle(contentStream, "EDUCACIÓN", MARGIN, yPosition, boldFont);
                    
                    for (CandidatoResponse.EducacionResponse edu : candidato.getEducaciones()) {
                        yPosition = drawText(contentStream, String.format("Nivel: %s", edu.getNivelEstudio()), MARGIN, yPosition, boldFont, NORMAL_SIZE, false);
                        yPosition = drawText(contentStream, String.format("Institución: %s", edu.getInstitucion()), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                        yPosition = drawText(contentStream, String.format("Título: %s", edu.getTituloObtenido()), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                        
                        if (edu.getFechaInicio() != null || edu.getFechaFin() != null) {
                            String fechas = String.format("Período: %s - %s",
                                edu.getFechaInicio() != null ? edu.getFechaInicio().format(DATE_FORMAT) : "N/A",
                                edu.getFechaFin() != null ? edu.getFechaFin().format(DATE_FORMAT) : "En curso"
                            );
                            yPosition = drawText(contentStream, fechas, MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                        }
                        
                        yPosition -= LINE_HEIGHT;
                    }
                }

                // Personal References
                if (candidato.getReferenciasPersonales() != null && !candidato.getReferenciasPersonales().isEmpty()) {
                    yPosition -= LINE_HEIGHT;
                    yPosition = drawSectionTitle(contentStream, "REFERENCIAS PERSONALES", MARGIN, yPosition, boldFont);
                    
                    for (CandidatoResponse.ReferenciaPersonalResponse ref : candidato.getReferenciasPersonales()) {
                        yPosition = drawText(contentStream, String.format("Nombre: %s", ref.getNombreCompleto() != null ? ref.getNombreCompleto() : "N/A"), MARGIN, yPosition, boldFont, NORMAL_SIZE, false);
                        yPosition = drawText(contentStream, String.format("Teléfono: %s", ref.getTelefono() != null ? ref.getTelefono() : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                        yPosition = drawText(contentStream, String.format("Relación: %s", ref.getParentesco() != null ? ref.getParentesco() : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                        yPosition -= LINE_HEIGHT;
                    }
                }

                // Emergency Contacts
                if (candidato.getContactosEmergencia() != null && !candidato.getContactosEmergencia().isEmpty()) {
                    yPosition -= LINE_HEIGHT;
                    yPosition = drawSectionTitle(contentStream, "CONTACTOS DE EMERGENCIA", MARGIN, yPosition, boldFont);
                    
                    for (CandidatoResponse.ContactoEmergenciaResponse emerg : candidato.getContactosEmergencia()) {
                        yPosition = drawText(contentStream, String.format("Nombre: %s", emerg.getNombreCompleto() != null ? emerg.getNombreCompleto() : "N/A"), MARGIN, yPosition, boldFont, NORMAL_SIZE, false);
                        yPosition = drawText(contentStream, String.format("Teléfono: %s", emerg.getTelefono() != null ? emerg.getTelefono() : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                        yPosition = drawText(contentStream, String.format("Parentesco: %s", emerg.getParentesco() != null ? emerg.getParentesco() : "N/A"), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                        yPosition -= LINE_HEIGHT;
                    }
                }

                // Notes
                if (candidato.getNotas() != null && !candidato.getNotas().isEmpty()) {
                    yPosition -= LINE_HEIGHT;
                    yPosition = drawSectionTitle(contentStream, "OBSERVACIONES", MARGIN, yPosition, boldFont);
                    yPosition = drawText(contentStream, candidato.getNotas(), MARGIN, yPosition, regularFont, NORMAL_SIZE, false);
                }

                // Footer with generation date
                contentStream.setFont(regularFont, 9);
                contentStream.beginText();
                contentStream.newLineAtOffset(MARGIN, 30);
                contentStream.showText(String.format("Documento generado el: %s", LocalDate.now().format(DATE_FORMAT)));
                contentStream.endText();
            }

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            document.save(baos);
            return baos.toByteArray();
        }
    }

    private float drawText(PDPageContentStream contentStream, String text, float x, float y, PDType1Font font, float fontSize, boolean center) throws IOException {
        contentStream.setFont(font, fontSize);
        
        if (center) {
            float textWidth = font.getStringWidth(text) / 1000 * fontSize;
            float pageWidth = 595; // A4 width
            x = (pageWidth - textWidth) / 2;
        }
        
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text != null ? text : "");
        contentStream.endText();
        return y - LINE_HEIGHT;
    }

    private float drawSectionTitle(PDPageContentStream contentStream, String title, float x, float y, PDType1Font font) throws IOException {
        contentStream.setFont(font, SECTION_SIZE);
        
        // Draw a line under the title
        contentStream.setLineWidth(1f);
        contentStream.moveTo(x, y + 3);
        contentStream.lineTo(545, y + 3);
        contentStream.stroke();
        
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(title);
        contentStream.endText();
        return y - LINE_HEIGHT - 5;
    }
}
