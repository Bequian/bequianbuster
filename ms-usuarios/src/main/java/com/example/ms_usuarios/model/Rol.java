package com.example.ms_usuarios.model;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Rol {
    ADMIN, CLIENTE;

    /**
     * Convierte un texto en un valor del enum Rol.
     * @param valor El texto a convertir en un valor del enum Rol.
     * @return El valor del enum Rol correspondiente al texto, o null si no coincide con ningún valor.
     */
    @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
    public static Rol desdeTexto(String valor) {
        if (valor == null) {
            return null;
        }
        for (Rol rol : values()) {
            if (rol.name().equalsIgnoreCase(valor.trim())) {
                return rol;
            }
        }
        return null;
    }

}
