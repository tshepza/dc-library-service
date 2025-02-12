package com.digicert.library.api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final String ERROR_TYPE = "Error";

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(ERROR_TYPE, ex.getMetaData(), Collections.singletonList(ex.getMessage())));
    }

    @ExceptionHandler(InternalServerException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(InternalServerException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(ERROR_TYPE, ex.getMetaData(), Collections.singletonList(ex.getMessage())));
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(ERROR_TYPE,  ex.getMetaData(), Collections.singletonList(ex.getMessage())));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            String fieldName = error.getField();
            String objName = error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.add(buildErrorField(errorMessage,objName,fieldName));
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse("Validation",null, errors));
    }

    private String buildErrorField(String errorMsg,String obj,String fieldName){
        return "%s. Invalid Field: %s -> %s".formatted(errorMsg, obj, fieldName);
    }
}
