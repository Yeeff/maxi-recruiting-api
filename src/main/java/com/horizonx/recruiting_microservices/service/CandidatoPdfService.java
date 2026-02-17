package com.horizonx.recruiting_microservices.service;

import com.horizonx.recruiting_microservices.dto.CandidatoResponse;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

@Service
public class CandidatoPdfService {

    private final TemplateEngine templateEngine;

    public CandidatoPdfService() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.XML); // XHTML mode for Flying Saucer
        
        this.templateEngine = new SpringTemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver);
    }

    public byte[] generateCandidatoPdf(CandidatoResponse candidato) throws Exception {
        // Set up Thymeleaf context with candidate data
        Context context = new Context();
        context.setVariable("candidato", candidato);
        
        // Render HTML template with Thymeleaf
        String htmlContent = templateEngine.process("candidato-cv", context);
        
        // Convert HTML to PDF using Flying Saucer
        try (OutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            renderer.setDocumentFromString(htmlContent);
            renderer.layout();
            renderer.createPDF(outputStream);
            
            return ((ByteArrayOutputStream) outputStream).toByteArray();
        }
    }
}
