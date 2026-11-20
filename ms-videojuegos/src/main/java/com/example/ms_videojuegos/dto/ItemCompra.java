package com.example.ms_videojuegos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

/** Un producto de la compra: qué videojuego y cuántas unidades. */
public record ItemCompra(

    @NotNull(message = "El id del videojuego es obligatorio")
    Long id,

    @NotNull(message = "La cantidad es obligatoria")
    @Min(value = 1, message = "La cantidad debe ser al menos 1")
    Integer cantidad

) {}