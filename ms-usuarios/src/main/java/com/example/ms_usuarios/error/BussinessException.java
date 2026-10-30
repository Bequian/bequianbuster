package com.example.ms_usuarios.error;
import org.springframework.http.HttpStatus;

/**
 * Excepción personalizada para errores de negocio.
 * Contiene un estado HTTP y un mensaje de error.
 * BussinessException se lanza cuando ocurre un error de negocio en la aplicación.
 */
public class BussinessException extends RuntimeException {

    private final HttpStatus estado;

    public BussinessException(HttpStatus estado,String mensaje) {

        super(mensaje);this.estado=estado;
        
    }

    public HttpStatus estado(){return estado;}
}
