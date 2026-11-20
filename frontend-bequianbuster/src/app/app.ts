import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

import { Auth } from './services/auth';

import { Carrito } from './services/carrito';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  protected readonly auth = inject(Auth);
  private readonly router = inject(Router);

  /** Menú desplegable en pantallas pequeñas (hamburguesa). */
  protected readonly menuAbierto = signal(false);
  protected readonly anio = new Date().getFullYear();

  /** Carrito de compras. */
  protected readonly carrito = inject(Carrito);

  protected alternarMenu(): void {
    this.menuAbierto.update(abierto => !abierto);
  }

  protected cerrarMenu(): void {
    this.menuAbierto.set(false);
  }

  protected salir(): void {
    this.cerrarMenu();
    this.carrito.vaciar();
    this.auth.cerrarSesion();
    this.router.navigate(['/catalogo']);
  }
}