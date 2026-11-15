package cl.bequianbuster.base.controller;

import java.time.Instant;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/** Endpoint de verificación: confirma que el microservicio está en ejecución. */
@RestController
public class EstadoController {

    private final String servicio;

    public EstadoController(@Value("${spring.application.name}") String servicio) {
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