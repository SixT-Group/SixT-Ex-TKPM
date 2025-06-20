package com.example.sixt.controllers.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Generic API response wrapper to standardize response structure
 * @param <T> The type of data being returned
 */
public class ApiResponse<T> {
    private int status;
    private String message;
    private T data;

    public ApiResponse() {}

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Static factory methods for common success responses
    public static <T> ResponseEntity<ApiResponse<T>> success(T data, String message, HttpStatus httpStatus) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(httpStatus.value());
        response.setMessage(message);
        response.setData(data);
        return new ResponseEntity<>(response, httpStatus);
    }

    public static <T> ResponseEntity<ApiResponse<T>> success(T data, HttpStatus httpStatus) {
        return success(data, "Success", httpStatus);
    }

    public static <T> ResponseEntity<ApiResponse<T>> created(T data, String message) {
        return success(data, message, HttpStatus.CREATED);
    }

    public static <T> ResponseEntity<ApiResponse<T>> ok(T data, String message) {
        return success(data, message, HttpStatus.OK);
    }

    // Static factory methods for error responses
    public static <T> ResponseEntity<ApiResponse<T>> error(String message, HttpStatus httpStatus) {
        ApiResponse<T> response = new ApiResponse<>();
        response.setStatus(httpStatus.value());
        response.setMessage(message);
        response.setData(null);
        return new ResponseEntity<>(response, httpStatus);
    }

    public static <T> ResponseEntity<ApiResponse<T>> conflict(String message) {
        return error(message, HttpStatus.CONFLICT);
    }

    public static <T> ResponseEntity<ApiResponse<T>> badRequest(String message) {
        return error(message, HttpStatus.BAD_REQUEST);
    }

    public static <T> ResponseEntity<ApiResponse<T>> internalServerError(String message) {
        return error(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Getters and setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
} 