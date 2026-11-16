#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoint de verificación: confirma que el microservicio está en ejecución. */
@RestController
public class EstadoController {

    private final String servicio;

    public EstadoController(@Value("${symbol_dollar}{spring.application.name}") String servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/estado")
    public Map<String, Object> estado() {
        return Map.of(
            "servicio", servicio,
            "estado", "activo",
            "momento", Instant.now()
        );
    }
}