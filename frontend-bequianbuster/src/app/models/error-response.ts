/**
 * Respuesta de error de los microservicios (ErrorResponse en Java).
 * El mapa de errores por campo se llama "datos" en ms-videojuegos
 * y "campos" en ms-usuarios.
 */
export interface ErrorResponse {
  momento: string;
  estado: number;
  mensaje: string;
  ruta: string;
  datos?: Record<string, string>;
  campos?: Record<string, string>;
}