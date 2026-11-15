package cl.bequianbuster.catalogo.orden;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import cl.bequianbuster.catalogo.model.Videojuego;

/** Estrategia: del precio más bajo al más alto. */
@Component
public class OrdenPorPrecio implements EstrategiaOrden {

    @Override
    public String clave() {
        return "precio";
    }

    @Override
    public Comparator<Videojuego> comparador() {
        return Comparator.comparing(Videojuego::getPrecio);
    }
}