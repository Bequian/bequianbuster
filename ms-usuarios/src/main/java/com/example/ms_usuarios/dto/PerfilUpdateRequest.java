package com.example.ms_usuarios.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Cambios que un usuario puede hacer sobre su propia cuenta.
 * La contraseña nueva es opcional; si viene, se exige la contraseña actual.
 */
public record PerfilUpdateRequest(

    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 120, message = "El correo supera los 120 caracteres")
    @Email(regexp = ValidationRules.EMAIL, message = "El correo debe tener un formato válido (ej: usuario@dominio.cl)")
    String email,

    String passwordActual,

    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
    @Pattern(regexp = ValidationRules.PASSWORD,
             message = "La contraseña debe incluir mayúscula, minúscula, número y un carácter especial, sin espacios")
    String passwordNueva

) {}