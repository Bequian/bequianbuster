package cl.bequianbuster.catalogo.orden;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import cl.bequianbuster.catalogo.model.Videojuego;

/** Estrategia: lanzamientos más recientes primero. */
@Component
public class OrdenPorFecha implements EstrategiaOrden {

    @Override
    public String clave() {
        return "fecha";
    }

    @Override
    public Comparator<Videojuego> comparador() {
        return Comparator.comparing(Videojuego::getFechaLanzamiento).reversed();
    }
}