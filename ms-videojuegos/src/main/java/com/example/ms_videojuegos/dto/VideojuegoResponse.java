package com.example.ms_videojuegos.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import com.example.ms_videojuegos.model.Videojuego;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Separa el contrato publico de las anotaciones JPA.
 */
public record VideojuegoResponse(
    Long id,
    String titulo,
    String desarrollador,
    @JsonFormat(pattern = "dd-MM-yyyy") LocalDate fechaLanzamiento,
    String plataforma,
    String genero,
    String descripcion,
    BigDecimal precio,
    Integer stock,
    String imagen
) {
    public static VideojuegoResponse de(Videojuego videojuego) {
        
        return new VideojuegoResponse(
            videojuego.getId(),
            videojuego.getTitulo(),
            videojuego.getDesarrollador(),
            videojuego.getFechaLanzamiento(),
            videojuego.getPlataforma(),
            videojuego.getGenero(),
            videojuego.getDescripcion(),
            videojuego.getPrecio(),
            videojuego.getStock(),
            videojuego.getImagen()
        );

    }
}
