package com.horizonx.recruiting_microservices.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private T data;
    private Operation operation;

    public ApiResponse() {
        this.operation = new Operation("OK", null);
    }

    public ApiResponse(T data) {
        this.data = data;
        this.operation = new Operation("OK", null);
    }

    public ApiResponse(T data, String code, String message) {
        this.data = data;
        this.operation = new Operation(code, message);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, "OK", null);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(null, "FAIL", message);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Operation getOperation() {
        return operation;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public static class Operation {
        private String code;
        private String message;

        public Operation() {}

        public Operation(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
