package com.example.ms_videojuegos.dto;
import jakarta.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;

/**
 * Contrato de entrada para la creación o actualización de un videojuego.
 */
public record VideojuegoRequest(

    /** El título del videojuego */
    @NotBlank(message = "El titulo es obligatorio")
    @Size(max = 100, message = "El titulo supera los 100 caracteres") String titulo,

    /** El nombre del desarrollador del videojuego */
    @NotBlank (message = "El nombre del desarrollador es obligatorio")
    @Size(max = 100, message = "El nombre del desarrollador supera los 100 caracteres") String desarrollador,

    /** La fecha de lanzamiento del videojuego */
    @NotNull(message = "La fecha de lanzamiento es obligatoria")
    @JsonFormat(pattern = "dd-MM-yyyy") LocalDate fechaLanzamiento,

    /** La plataforma en la que está disponible el videojuego */
    @NotBlank(message = "La plataforma es obligatoria")
    @Pattern(
    regexp = "PC|PS4|PS5|Xbox One|Xbox Series X\\|S|Nintendo Switch",
    message = "Plataforma no válida. Valores permitidos: PC, PS4, PS5, Xbox One, Xbox Series X|S, Nintendo Switch"
    ) String plataforma,

    /** El género del videojuego */
    @NotBlank(message = "El genero es obligatorio")
    @Size(max = 50, message = "El genero supera los 50 caracteres") String genero,

    /** La descripción del videojuego */
    @NotBlank(message = "La descripcion es obligatoria")
    @Size(max = 500, message = "La descripcion supera los 500 caracteres") String descripcion,
    
    /** El precio del videojuego */
    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.00", inclusive = false, message = "El precio debe ser mayor a 0") 
    @Digits(integer = 10, fraction = 2, message = "Precio fuera de formato") BigDecimal precio,

    /** La cantidad de stock disponible del videojuego */
    @NotNull(message = "El Stock es obligatorio")
    @Min(value = 0, message = "El Stock debe ser mayor o igual a 0") Integer stock,

    /** La dirección de la imagen del videojuego */
    @Size (max = 255, message = "La dirección de la imagen supera los 255 caracteres") String imagen

) { }
