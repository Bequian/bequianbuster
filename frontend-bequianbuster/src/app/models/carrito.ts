import { Videojuego } from './videojuego';

/** Un producto dentro del carrito. */
export interface ItemCarrito {
  videojuego: Videojuego;
  cantidad: number;
}

/** Datos de despacho del formulario de compra (sin datos de pago). */
export interface DatosDespacho {
  nombre: string;
  direccion: string;
  comuna: string;
  telefono: string;
}

/** Comprobante de una compra simulada. */
export interface Orden {
  numero: string;
  fecha: string;
  items: ItemCarrito[];
  total: number;
  despacho: DatosDespacho;
}