package com.horizonx.recruiting_microservices.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    @Value("${cors.allowed-origins:*}")
    private String allowedOrigins;

    @Value("${cors.allowed-methods:GET,POST,PUT,DELETE,PATCH,OPTIONS}")
    private String allowedMethods;

    @Value("${cors.allowed-headers:*}")
    private String allowedHeaders;

    @Value("${cors.max-age:3600}")
    private long maxAge;

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(parseOrigins(allowedOrigins))
                .allowedMethods(parseMethods(allowedMethods))
                .allowedHeaders(allowedHeaders)
                .allowCredentials(false)
                .maxAge(maxAge);
    }

    private String[] parseOrigins(String origins) {
        if ("*".equals(origins.trim())) {
            return new String[]{"*"};
        }
        return Arrays.stream(origins.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }

    private String[] parseMethods(String methods) {
        return Arrays.stream(methods.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }
}
