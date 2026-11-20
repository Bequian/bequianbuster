import { Injectable, computed, inject, signal } from '@angular/core';
import { Observable, map, tap } from 'rxjs';

import { Videojuego } from '../models/videojuego';
import { DatosDespacho, ItemCarrito, Orden } from '../models/carrito';
import { VideojuegoApi } from './videojuego-api';

const CLAVE_CARRITO = 'bb_carrito';

/**
 * Carrito de compras (Singleton, providedIn: 'root').
 * El estado es un arreglo de ItemCarrito dentro de una señal.
 * Cada cambio crea un arreglo nuevo (inmutable) para que las vistas
 * que leen la señal se actualicen. Se guarda en sessionStorage para
 * sobrevivir a una recarga de página.
 */
@Injectable({ providedIn: 'root' })
export class Carrito {

    private readonly _items = signal<ItemCarrito[]>(this.leer());
    private readonly api = inject(VideojuegoApi);

    readonly items = this._items.asReadonly();
    readonly cantidad = computed(() => this._items().reduce((suma, i) => suma + i.cantidad, 0));
    readonly total = computed(() => this._items().reduce((suma, i) => suma + i.cantidad * i.videojuego.precio, 0));

    /** Agrega una unidad. Devuelve false si ya no queda stock disponible. */
    agregar(videojuego: Videojuego): boolean {
        const existente = this._items().find(i => i.videojuego.id === videojuego.id);
        if ((existente?.cantidad ?? 0) >= videojuego.stock) {
        return false;
        }
        this.actualizar(existente
        ? this._items().map(i => i.videojuego.id === videojuego.id ? { ...i, cantidad: i.cantidad + 1 } : i)
        : [...this._items(), { videojuego, cantidad: 1 }]);
        return true;
    }

    /** Cambia la cantidad respetando el stock. Con 0 o menos, quita el producto. */
    cambiarCantidad(id: number, cantidad: number): void {
        if (cantidad <= 0) {
        this.quitar(id);
        return;
        }
        this.actualizar(this._items().map(i =>
        i.videojuego.id === id ? { ...i, cantidad: Math.min(cantidad, i.videojuego.stock) } : i));
    }

    quitar(id: number): void {
        this.actualizar(this._items().filter(i => i.videojuego.id !== id));
    }

    vaciar(): void {
        this.actualizar([]);
    }

    /**
    * Envía la compra a ms-videojuegos (descuenta el stock en Oracle).
    * Si responde bien, genera el comprobante y vacía el carrito.
    * Si falla (ej: 409 sin stock), el carrito se mantiene intacto.
    */
    confirmarCompra(despacho: DatosDespacho): Observable<Orden> {
        const items = this._items();
        const total = this.total();

        return this.api.comprar(items).pipe(
        map(() => ({
            numero: 'BB-' + Date.now().toString().slice(-8),
            fecha: new Date().toISOString(),
            items,
            total,
            despacho
        })),
        tap(() => this.vaciar())
        );
    }

    private actualizar(items: ItemCarrito[]): void {
        this._items.set(items);
        try { sessionStorage.setItem(CLAVE_CARRITO, JSON.stringify(items)); } catch { /* sin almacenamiento */ }
    }

    private leer(): ItemCarrito[] {
        try {
        const guardado = sessionStorage.getItem(CLAVE_CARRITO);
        return guardado ? JSON.parse(guardado) as ItemCarrito[] : [];
        } catch {
        return [];
        }
    }
}