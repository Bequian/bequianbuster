package cl.bequianbuster.catalogo.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.bequianbuster.catalogo.dto.VideojuegoRequest;
import cl.bequianbuster.catalogo.dto.VideojuegoResponse;
import cl.bequianbuster.catalogo.service.VideojuegoService;

import jakarta.validation.Valid;

/** API REST del catálogo de videojuegos. */
@RestController
@RequestMapping("/videojuego")
public class VideojuegoController {

    private final VideojuegoService servicio;

    public VideojuegoController(VideojuegoService servicio) {
        this.servicio = servicio;
    }

    /** GET /videojuego  ·  GET /videojuego?orden=titulo|precio|fecha */
    @GetMapping
    public List<VideojuegoResponse> listar(
            @RequestParam(name = "orden", required = false) String orden) {
        return servicio.listar(orden).stream()
            .map(VideojuegoResponse::de)
            .toList();
    }

    /** GET /videojuego/{id} */
    @GetMapping("/{id}")
    public VideojuegoResponse buscarPorId(@PathVariable("id") Long id) {
        return VideojuegoResponse.de(servicio.buscarPorId(id));
    }

    /** POST /videojuego → 201 */
    @PostMapping
    public ResponseEntity<VideojuegoResponse> crear(@Valid @RequestBody VideojuegoRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED)
            .body(VideojuegoResponse.de(servicio.crear(datos)));
    }

    /** PUT /videojuego/{id} → 200 */
    @PutMapping("/{id}")
    public VideojuegoResponse actualizar(@PathVariable("id") Long id,
                                         @Valid @RequestBody VideojuegoRequest datos) {
        return VideojuegoResponse.de(servicio.actualizar(id, datos));
    }

    /** DELETE /videojuego/{id} → 204 */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable("id") Long id) {
        servicio.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}