package com.example.ms_usuarios.controller;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EstadoController {
    @GetMapping("/estado")
    public Map<String,String> estado(){return Map.of("servicio","usuarios","estado","activo");}
}