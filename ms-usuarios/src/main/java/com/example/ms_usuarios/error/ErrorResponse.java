package com.example.ms_usuarios.error;
import java.time.Instant;
import java.util.Map;

public record ErrorResponse(Instant momento,int estado,String mensaje, String ruta,Map<String,String> campos) {}