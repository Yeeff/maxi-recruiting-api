package com.horizonx.recruiting_microservices.config;

import com.horizonx.recruiting_microservices.dto.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResponseWrapperAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Only wrap responses that are not already ApiResponse
        return !returnType.getParameterType().equals(ApiResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        
        // Don't wrap strings or primitive types or byte arrays
        if (body instanceof String || body instanceof Number || body instanceof Boolean || body instanceof byte[]) {
            return body;
        }
        
        // Wrap null responses (like 204 No Content) with success response
        if (body == null) {
            return ApiResponse.success(null);
        }
        
        return ApiResponse.success(body);
    }
}
