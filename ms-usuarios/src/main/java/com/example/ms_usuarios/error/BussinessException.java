package com.example.ms_usuarios.error;
import org.springframework.http.HttpStatus;

public class BussinessException extends RuntimeException {

    private final HttpStatus estado;

    public BussinessException(HttpStatus estado,String mensaje) {

        super(mensaje);this.estado=estado;
        
    }

    public HttpStatus estado(){return estado;}
}
