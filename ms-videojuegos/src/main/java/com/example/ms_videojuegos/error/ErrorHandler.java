package com.example.ms_videojuegos.error;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class ErrorHandler {

    /** Maneja las excepciones de validación de argumentos de método.
     * @param ex La excepción lanzada durante la validación.
     * @param req La solicitud HTTP que provocó la excepción.
     * @return Una respuesta con el estado BAD_REQUEST y los detalles de los errores de validación.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException ex, HttpServletRequest req) {

        Map<String,String> campos = new LinkedHashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> campos.putIfAbsent(error.getField(), error.getDefaultMessage()));

        return response(HttpStatus.BAD_REQUEST,"Solicitud invalida",req,campos);

    }

    /**
     * Maneja las excepciones cuando un recurso no es encontrado.
     * @param ex La excepción lanzada cuando el recurso no existe.
     * @param req La solicitud HTTP que provocó la excepción.
     * @return Una respuesta con el estado NOT_FOUND y el mensaje de error correspondiente.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> dontexist(ResourceNotFoundException ex, HttpServletRequest req) {

        return response(HttpStatus.NOT_FOUND,ex.getMessage(),req,Map.of());

    }

    /**
     * Maneja las excepciones cuando el cuerpo de la solicitud JSON no tiene el formato esperado.
     * @param ex La excepción lanzada durante la lectura del mensaje HTTP.
     * @param req La solicitud HTTP que provocó la excepción.
     * @return Una respuesta con el estado BAD_REQUEST y el mensaje de error correspondiente.
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> wrongjson(Exception ex,HttpServletRequest req) {

        return response(HttpStatus.BAD_REQUEST,"El cuerpo JSON no tiene el formato esperado",req,Map.of());

    }

    private ResponseEntity<ErrorResponse> response(HttpStatus status,String msg, HttpServletRequest req,Map<String,String> datos) {

        return ResponseEntity.status(status).body(new ErrorResponse(Instant.now(),status.value(),msg,req.getRequestURI(),datos));

    }
}
