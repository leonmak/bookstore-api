package com.example.bookstore.advice;

import com.example.bookstore.exception.BookNotFoundException;
import com.example.bookstore.exception.BookQueryFormatException;
import com.example.bookstore.exception.DuplicateBookException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<Object> handleBookNotFoundException(BookNotFoundException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateBookException.class)
    public ResponseEntity<Object> handleDuplicateBookException(DuplicateBookException ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BookQueryFormatException.class)
    public ResponseEntity<Object> invalidBookRequestException(Exception ex, WebRequest request) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        var errorsMap = Map.of(
                "errors",
                ex.getBindingResult().getFieldErrors().stream()
                        .map(field -> String.format("%s: %s", field.getField(), field.getDefaultMessage()))
                        .toList());
        return new ResponseEntity<>(errorsMap, new HttpHeaders(), HttpStatus.UNPROCESSABLE_ENTITY);
    }
}
