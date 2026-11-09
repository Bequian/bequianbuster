/**
 * Videojuego tal como lo devuelve ms-videojuegos (VideojuegoResponse).
 * Los nombres de los campos deben coincidir exactamente con el JSON.
 */
export interface Videojuego {
  id: number;
  titulo: string;
  desarrollador: string;
  fechaLanzamiento: string;   // formato dd-MM-yyyy
  plataforma: string;
  genero: string;
  descripcion: string;
  precio: number;
  stock: number;
  imagen: string | null;
}

/**
 * Datos que se envían en POST y PUT (VideojuegoRequest).
 * Es igual al modelo, pero sin el id: lo genera Oracle.
 */
export type VideojuegoRequest = Omit<Videojuego, 'id'>;

/** Plataformas permitidas por el CHECK de la base de datos. */
export const PLATAFORMAS = [
  'PC', 'PS4', 'PS5', 'Xbox One', 'Xbox Series X|S', 'Nintendo Switch'
] as const;

/**
 * Datos del formulario de crear/editar.
 * La fecha usa yyyy-MM-dd porque es el formato del <input type="date">.
 * La fachada la convierte a dd-MM-yyyy antes de enviarla a la API.
 */
export interface FormularioVideojuego {
  titulo: string;
  desarrollador: string;
  fechaLanzamiento: string;   // yyyy-MM-dd
  plataforma: string;
  genero: string;
  descripcion: string;
  precio: number | null;
  stock: number | null;
  imagen: string;
}