package com.example.ms_usuarios.dto;

/**
 * Contiene las reglas de validación utilizadas en los DTOs y la carga inicial.
 * Contiene expresiones regulares para validar correos electrónicos, contraseñas y nombres de usuario.
 * Proporciona constantes que representan las expresiones regulares para la validación.

 */
public final class ValidationRules {

    public static final String EMAIL =
        "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

    public static final String PASSWORD =
        "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$";

    public static final String USERNAME =
        "^[A-Za-z][A-Za-z0-9._-]*$";

    private ValidationRules() {
    }
}