package com.example.ms_usuarios.error;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Manejador global de errores para la aplicación.
 * Captura excepciones específicas y genera respuestas de error adecuadas.
 */
@RestControllerAdvice
public class ErrorHandler {

    /**
     * Maneja las excepciones de tipo BussinessException.
     * @param e La excepción de negocio capturada.
     * @param r La solicitud HTTP que generó la excepción.
     * @return Una respuesta HTTP con el estado y mensaje de error correspondiente.
     */
    @ExceptionHandler(BussinessException.class)
    public ResponseEntity<ErrorResponse> negocio(BussinessException e, HttpServletRequest r) {

        return response(e.estado(),e.getMessage(),r,Map.of());

    }

    /**
     * Maneja las excepciones de validación de argumentos.
     * @param e La excepción de validación capturada.
     * @param r La solicitud HTTP que generó la excepción.
     * @return Una respuesta HTTP con el estado y los errores de validación correspondientes.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> validacion(MethodArgumentNotValidException e, HttpServletRequest r) {

        Map<String,String> errores=new LinkedHashMap<>();
        e.getBindingResult().getFieldErrors().forEach(f ->
            errores.putIfAbsent(f.getField(),f.getDefaultMessage()));
        return response(HttpStatus.BAD_REQUEST,"Solicitud invalida",r,errores);

    }

    /**
     * Maneja las excepciones de JSON incorrecto.
     * @param e La excepción capturada al intentar leer un mensaje HTTP no legible.
     * @param r La solicitud HTTP que generó la excepción.
     * @return Una respuesta HTTP con el estado y mensaje de error correspondiente.
     */
    @ExceptionHandler(org.springframework.http.converter.HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> wrongjson(Exception e,HttpServletRequest r) {

        return response(HttpStatus.BAD_REQUEST,"JSON incorrecto",r,Map.of());

    }
    private ResponseEntity<ErrorResponse> response(HttpStatus status,String msj, HttpServletRequest r,Map<String,String> campos) {

        return ResponseEntity.status(status).body(new ErrorResponse(Instant.now(),status.value(),msj,r.getRequestURI(),campos));

    }
}