package com.example.ms_usuarios.controller;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controlador que expone el estado del servicio de usuarios.
 * Devuelve un mapa con el estado actual del servicio.
 * Proporciona un endpoint para consultar el estado del servicio.
 */
@RestController
public class EstadoController {
    @GetMapping("/estado")
    public Map<String,String> estado(){return Map.of("servicio","usuarios","estado","activo");}
}