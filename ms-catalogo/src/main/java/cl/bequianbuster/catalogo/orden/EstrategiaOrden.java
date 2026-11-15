package cl.bequianbuster.catalogo.orden;

import java.util.Comparator;
import java.util.List;

import cl.bequianbuster.catalogo.model.Videojuego;

/**
 * Patrón Strategy (comportamiento).
 * Cada implementación define un criterio de orden intercambiable.
 * El servicio trabaja con esta interfaz y no conoce las clases concretas.
 */
public interface EstrategiaOrden {

    /** Valor del parámetro ?orden= que activa esta estrategia. */
    String clave();

    /** Criterio de comparación entre dos videojuegos. */
    Comparator<Videojuego> comparador();

    /** Ordena la lista según el criterio, sin modificar la original. */
    default List<Videojuego> ordenar(List<Videojuego> videojuegos) {
        return videojuegos.stream().sorted(comparador()).toList();
    }
}