package com.example.ms_videojuegos.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import jakarta.validation.Valid;

import com.example.ms_videojuegos.dto.VideojuegoResponse;
import com.example.ms_videojuegos.dto.VideojuegoRequest;
import com.example.ms_videojuegos.service.VideojuegoService;

/**
 * Controlador REST para gestionar los videojuegos.
 * Proporciona endpoints para listar, buscar, crear, actualizar y eliminar videojuegos.
 */
@RestController 
@RequestMapping ("/videojuego")
public class VideojuegoController {

    /**
     * Servicio de videojuegos utilizado por el controlador.
     * VideojuegoService proporciona la lógica de negocio para gestionar los videojuegos.
     */
    private final VideojuegoService videojuegoService;

    /**
     * Constructor del controlador de videojuegos.
     * @param videojuegoService El servicio de videojuegos que será utilizado por el controlador.
     */
    public VideojuegoController(VideojuegoService videojuegoService) {
        this.videojuegoService = videojuegoService;
    }

    /**
     * Lista todos los videojuegos disponibles.
     * @return Una lista de respuestas de videojuegos.
     */
    @GetMapping
    public List<VideojuegoResponse> listar() {
        return videojuegoService.listar().stream()
            .map(VideojuegoResponse::de)
            .toList();
    }

    /**
     * Busca un videojuego por su ID.
     * @param id El ID del videojuego a buscar.
     * @return La respuesta del videojuego correspondiente al ID proporcionado.
     */
    @GetMapping("/{id}")
    public VideojuegoResponse buscarPorId(@PathVariable ("id") Long id) {
        return VideojuegoResponse.de(videojuegoService.buscarPorId(id));
    }
    
    /**
     * Crea un nuevo videojuego.
     * @param datos Los datos del videojuego a crear.
     * @return La respuesta del videojuego creado con el estado HTTP CREATED.
     */
    @PostMapping
    public ResponseEntity<VideojuegoResponse> crear(
        @Valid @RequestBody VideojuegoRequest datos) {
            return ResponseEntity.status(HttpStatus.CREATED)
            .body(VideojuegoResponse.de(videojuegoService.crear(datos)))
        ;
    }
    
    /**
     * Actualiza un videojuego existente.
     * @param id El ID del videojuego a actualizar.
     * @param datos Los datos actualizados del videojuego.
     * @return La respuesta del videojuego actualizado.
     */
    @PutMapping("/{id}")
    public VideojuegoResponse actualizar(
        @PathVariable("id") Long id,
        @Valid @RequestBody VideojuegoRequest datos){

            return VideojuegoResponse.de(videojuegoService.actualizar(id, datos));
    }

    /**
     * Elimina un videojuego por su ID.
     * @param id El ID del videojuego a eliminar.
     * @return Una respuesta con el estado HTTP NO_CONTENT.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @PathVariable("id") Long id){

        videojuegoService.eliminar(id);
        return ResponseEntity.noContent().build();

    }

}
