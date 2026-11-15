package cl.bequianbuster.base.error;

import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Manejo centralizado de errores.
 * Todas las respuestas de error se construyen con ErrorResponse.Builder,
 * por lo que tienen siempre el mismo formato JSON.
 */
@RestControllerAdvice
public class GlobalErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalErrorHandler.class);

    /** 400: datos que no cumplen las validaciones (@Valid). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validacion(MethodArgumentNotValidException ex, HttpServletRequest req) {
        Map<String, String> campos = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
            .forEach(e -> campos.putIfAbsent(e.getField(), e.getDefaultMessage()));

        return responder(ErrorResponse.builder()
            .estado(HttpStatus.BAD_REQUEST)
            .mensaje("Solicitud inválida")
            .ruta(req.getRequestURI())
            .datos(campos)
            .build());
    }

    /** 400: JSON mal formado o con tipos incorrectos. */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> jsonInvalido(HttpMessageNotReadableException ex, HttpServletRequest req) {
        return responder(ErrorResponse.builder()
            .estado(HttpStatus.BAD_REQUEST)
            .mensaje("El cuerpo JSON no tiene el formato esperado")
            .ruta(req.getRequestURI())
            .build());
    }

    /** 400: parámetro de la URL con tipo incorrecto (ej: /recurso/abc en vez de un número). */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> tipoInvalido(MethodArgumentTypeMismatchException ex, HttpServletRequest req) {
        return responder(ErrorResponse.builder()
            .estado(HttpStatus.BAD_REQUEST)
            .mensaje("Valor inválido para el parámetro '" + ex.getName() + "'")
            .ruta(req.getRequestURI())
            .build());
    }

    /** 404: recurso de negocio inexistente. */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> noEncontrado(ResourceNotFoundException ex, HttpServletRequest req) {
        return responder(ErrorResponse.builder()
            .estado(HttpStatus.NOT_FOUND)
            .mensaje(ex.getMessage())
            .ruta(req.getRequestURI())
            .build());
    }

    /** 404: ruta que no existe en la API. */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponse> rutaInexistente(NoResourceFoundException ex, HttpServletRequest req) {
        return responder(ErrorResponse.builder()
            .estado(HttpStatus.NOT_FOUND)
            .mensaje("La ruta solicitada no existe")
            .ruta(req.getRequestURI())
            .build());
    }

        /** 405: método HTTP no permitido en la ruta (ej: POST /recurso/1). */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> metodoNoPermitido(HttpRequestMethodNotSupportedException ex, HttpServletRequest req) {
        return responder(ErrorResponse.builder()
            .estado(HttpStatus.METHOD_NOT_ALLOWED)
            .mensaje("El método " + ex.getMethod() + " no está permitido en esta ruta")
            .ruta(req.getRequestURI())
            .build());
    }

    /** 415: el cuerpo no se envió como JSON. */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> tipoNoSoportado(HttpMediaTypeNotSupportedException ex, HttpServletRequest req) {
        return responder(ErrorResponse.builder()
            .estado(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
            .mensaje("Tipo de contenido no soportado. Use Content-Type: application/json")
            .ruta(req.getRequestURI())
            .build());
    }

    /** 500: cualquier error no previsto. El detalle va al log, no al cliente. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> inesperado(Exception ex, HttpServletRequest req) {
        log.error("Error inesperado en {}", req.getRequestURI(), ex);
        return responder(ErrorResponse.builder()
            .estado(HttpStatus.INTERNAL_SERVER_ERROR)
            .mensaje("Error interno del servidor")
            .ruta(req.getRequestURI())
            .build());
    }

    private ResponseEntity<ErrorResponse> responder(ErrorResponse error) {
        return ResponseEntity.status(error.estado()).body(error);
    }
}