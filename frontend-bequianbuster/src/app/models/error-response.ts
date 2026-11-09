/** Respuesta de error de los microservicios (ErrorResponse en Java). */
export interface ErrorResponse {
  momento: string;
  estado: number;
  mensaje: string;
  ruta: string;
  datos: Record<string, string>;
}