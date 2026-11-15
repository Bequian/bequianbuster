package cl.bequianbuster.catalogo.error;

/** Se lanza cuando la solicitud tiene un valor no permitido (responde 400). */
public class SolicitudInvalidaException extends RuntimeException {

    public SolicitudInvalidaException(String mensaje) {
        super(mensaje);
    }
}