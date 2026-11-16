package cl.bequianbuster.catalogo.orden;

import java.util.Comparator;

import org.springframework.stereotype.Component;

import cl.bequianbuster.catalogo.model.Videojuego;

/** Estrategia: orden alfabético por título, sin distinguir mayúsculas. */
@Component
public class OrdenPorTitulo implements EstrategiaOrden {

    @Override
    public String clave() {
        return "titulo";
    }

    @Override
    public Comparator<Videojuego> comparador() {
        return Comparator.comparing(Videojuego::getTitulo, String.CASE_INSENSITIVE_ORDER);
    }
}