package com.example.ms_videojuegos.error;

/**
 * Excepción lanzada cuando un recurso no es encontrado.
 * ResourceNotFoundException se utiliza para indicar que un recurso solicitado no existe.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
    
}
