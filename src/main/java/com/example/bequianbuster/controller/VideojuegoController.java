package com.example.bequianbuster.controller;

import java.util.List;
import java.util.Optional;

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

import com.example.bequianbuster.model.Videojuego;
import com.example.bequianbuster.service.VideojuegoService;


@RestController 
@RequestMapping ("/videojuego")
public class VideojuegoController {

    private final VideojuegoService videojuegoService;

    public VideojuegoController(VideojuegoService videojuegoService) {
        this.videojuegoService = videojuegoService;
    }

    @GetMapping
    public List<Videojuego> listar() {
        return videojuegoService.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Videojuego> buscarPorId(
            @PathVariable("id") Long id ) {

        Optional<Videojuego> resultado = videojuegoService.buscarPorId(id);

        if (resultado.isPresent()){
            return ResponseEntity.ok(resultado.get());
        }
        return ResponseEntity.notFound().build();

    }

    @PostMapping
    public ResponseEntity<Videojuego> crear(
        @RequestBody Videojuego datos) {
            Videojuego nuevo = videojuegoService.crear(datos);

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(nuevo); 
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Videojuego> actualizar(
        @PathVariable("id") Long id,
        @RequestBody Videojuego datos){

            
        Optional<Videojuego> resultado = videojuegoService.actualizar(id, datos);

        if (resultado.isPresent()){
            return ResponseEntity.ok(resultado.get());
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
        @PathVariable("id") Long id){

        boolean eliminado = videojuegoService.eliminar(id);

        if (eliminado) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();

    }
    

}
