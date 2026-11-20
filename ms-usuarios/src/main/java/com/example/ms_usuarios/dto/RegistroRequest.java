package com.example.ms_usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Datos para el registro público de una cuenta.
 * No incluye el rol: toda cuenta registrada por esta vía es CLIENTE.
 */
public record RegistroRequest(

    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = ValidationRules.USERNAME,
             message = "El username debe comenzar con una letra y solo admite letras, números, punto, guion y guion bajo")
    String username,

    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 120, message = "El correo supera los 120 caracteres")
    @Email(regexp = ValidationRules.EMAIL, message = "El correo debe tener un formato válido (ej: usuario@dominio.cl)")
    String email,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
    @Pattern(regexp = ValidationRules.PASSWORD,
             message = "La contraseña debe incluir mayúscula, minúscula, número y un carácter especial, sin espacios")
    String password

) {}