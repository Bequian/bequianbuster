package com.example.ms_usuarios.error;
import java.time.Instant;
import java.util.Map;

/**
 * Representa la estructura de la respuesta de error devuelta por el manejador de errores.
 * Contiene información sobre el momento del error, el estado HTTP, el mensaje de error,
 * la ruta de la solicitud y los campos con errores de validación.
 */
public record ErrorResponse(Instant momento,int estado,String mensaje, String ruta,Map<String,String> campos) {}