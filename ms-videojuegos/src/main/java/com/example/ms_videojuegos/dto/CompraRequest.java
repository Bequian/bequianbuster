package com.example.ms_videojuegos.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

/** Compra con uno o más productos. */
public record CompraRequest(

    @NotEmpty(message = "La compra debe tener al menos un producto")
    List<@Valid ItemCompra> items

) {}