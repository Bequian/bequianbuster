package com.example.ms_videojuegos.error;

/** Se lanza cuando no hay stock suficiente para una compra (responde 409). */
public class StockInsuficienteException extends RuntimeException {

    public StockInsuficienteException(String mensaje) {
        super(mensaje);
    }
}