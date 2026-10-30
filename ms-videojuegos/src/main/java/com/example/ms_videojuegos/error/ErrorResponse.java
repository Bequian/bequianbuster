package com.example.ms_videojuegos.error;
import java.time.Instant;
import java.util.Map;

/**
 * Representa la estructura de la respuesta de error en la API.
 * ErrorResponse
 * @param momento El instante en que ocurrió el error.
 * @param estado El código de estado HTTP asociado al error.
 * @param mensaje El mensaje descriptivo del error.
 * @param ruta La ruta de la solicitud que provocó el error.
 * @param datos Información adicional sobre el error en forma de mapa clave-valor.
 */
public record ErrorResponse(
    Instant momento, int estado, String mensaje, String ruta, Map<String,String> datos
) {}
