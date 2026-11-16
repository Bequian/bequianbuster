#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.error;

import java.time.Instant;
import java.util.Map;

import org.springframework.http.HttpStatus;

/**
 * Respuesta de error estándar de todos los microservicios BequianBuster.
 *
 * Patrón Builder (creacional): el objeto se construye paso a paso con
 * métodos encadenados, en vez de un constructor con cinco parámetros
 * donde es fácil confundir el orden. El builder además completa valores
 * por defecto (momento, datos) y valida que no falte información obligatoria.
 *
 * Ejemplo:
 *   ErrorResponse.builder()
 *       .estado(HttpStatus.NOT_FOUND)
 *       .mensaje("Recurso no encontrado")
 *       .ruta("/videojuego/99")
 *       .build();
 */
public record ErrorResponse(
    Instant momento,
    int estado,
    String mensaje,
    String ruta,
    Map<String, String> datos
) {

    public static Builder builder() {
        return new Builder();
    }

    /** Constructor paso a paso de ErrorResponse. */
    public static final class Builder {

        private Instant momento = Instant.now();
        private Integer estado;
        private String mensaje;
        private String ruta;
        private Map<String, String> datos = Map.of();

        private Builder() {
        }

        public Builder estado(HttpStatus status) {
            this.estado = status.value();
            return this;
        }

        public Builder mensaje(String mensaje) {
            this.mensaje = mensaje;
            return this;
        }

        public Builder ruta(String ruta) {
            this.ruta = ruta;
            return this;
        }

        public Builder datos(Map<String, String> datos) {
            this.datos = Map.copyOf(datos);
            return this;
        }

        /** Valida y crea el objeto final (inmutable). */
        public ErrorResponse build() {
            if (estado == null || mensaje == null) {
                throw new IllegalStateException("ErrorResponse requiere estado y mensaje");
            }
            return new ErrorResponse(momento, estado, mensaje, ruta, datos);
        }
    }
}