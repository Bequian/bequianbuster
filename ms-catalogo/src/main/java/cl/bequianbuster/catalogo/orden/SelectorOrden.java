package cl.bequianbuster.catalogo.orden;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import cl.bequianbuster.catalogo.error.SolicitudInvalidaException;

/**
 * Contexto del patrón Strategy.
 * Spring inyecta automáticamente todas las clases que implementan
 * EstrategiaOrden. El selector las indexa por su clave y entrega la
 * que corresponde al parámetro recibido, sin if/else ni switch.
 */
@Component
public class SelectorOrden {

    private final Map<String, EstrategiaOrden> estrategias;

    public SelectorOrden(List<EstrategiaOrden> disponibles) {
        this.estrategias = disponibles.stream()
            .collect(Collectors.toUnmodifiableMap(EstrategiaOrden::clave, Function.identity()));
    }

    public EstrategiaOrden obtener(String clave) {
        EstrategiaOrden estrategia = estrategias.get(clave.trim().toLowerCase());
        if (estrategia == null) {
            throw new SolicitudInvalidaException(
                "Orden no válido: '" + clave + "'. Valores permitidos: "
                + String.join(", ", new TreeSet<>(estrategias.keySet())));
        }
        return estrategia;
    }
}