import { Component, inject, signal } from '@angular/core';
import { CurrencyPipe, DatePipe } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { Carrito } from '../../services/carrito';
import { DatosDespacho, Orden } from '../../models/carrito';

import { HttpErrorResponse } from '@angular/common/http';

/** Comunas disponibles para el despacho (lista estática). */
const COMUNAS = [
  'Santiago', 'Providencia', 'Las Condes', 'Ñuñoa', 'Maipú', 'La Florida',
  'Puente Alto', 'Valparaíso', 'Viña del Mar', 'Concepción', 'Temuco', 'Otra'
];

/**
 * Compra SIMULADA: solo se piden datos de despacho, nunca datos de pago.
 * Al confirmar, se genera un comprobante y se vacía el carrito.
 */
@Component({
  selector: 'app-checkout',
  imports: [FormsModule, RouterLink, CurrencyPipe, DatePipe],
  templateUrl: './checkout.html'
})
export class Checkout {

  protected readonly carrito = inject(Carrito);
  protected readonly comunas = COMUNAS;

  protected readonly error = signal<string | null>(null);

  protected readonly patronNombre = '^[A-Za-zÁÉÍÓÚáéíóúÑñÜü ]+$';
  protected readonly patronTelefono = '^\\+?56\\s?9\\s?\\d{4}\\s?\\d{4}$';

  protected despacho: DatosDespacho = { nombre: '', direccion: '', comuna: '', telefono: '' };
  protected readonly procesando = signal(false);
  protected readonly orden = signal<Orden | null>(null);

    protected confirmar(f: NgForm): void {
    if (f.invalid) {
      f.control.markAllAsTouched();
      return;
    }
    this.procesando.set(true);
    this.error.set(null);

    this.carrito.confirmarCompra({
      ...this.despacho,
      nombre: this.despacho.nombre.trim(),
      direccion: this.despacho.direccion.trim()
    }).subscribe({
      next: orden => {
        this.orden.set(orden);
        this.procesando.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.procesando.set(false);
        this.error.set(
          err.status === 409 ? err.error?.mensaje + '. Ajusta la cantidad en tu carrito.'
          : err.status === 403 ? 'Solo los clientes pueden realizar compras.'
          : err.status === 0 ? 'No se pudo conectar con el servicio de videojuegos.'
          : err.error?.mensaje ?? `No se pudo completar la compra (${err.status}).`
        );
      }
    });
  }
}