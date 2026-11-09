import { Injectable, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { VideojuegoApi } from '../services/videojuego-api';
import { Videojuego } from '../models/videojuego';

/**
 * Patrón Facade.
 * Ofrece a los componentes una interfaz simple (cargar, buscar)
 * y oculta los detalles internos: llamadas HTTP, estado de carga
 * y traducción de errores a mensajes para el usuario.
 *
 * Los componentes solo leen las señales y llaman métodos;
 * no conocen URLs, HttpClient ni códigos HTTP.
 */
@Injectable({ providedIn: 'root' })
export class CatalogoFacade {

  private readonly api = inject(VideojuegoApi);

  // Estado expuesto a las vistas (solo lectura desde fuera)
  private readonly _videojuegos = signal<Videojuego[]>([]);
  private readonly _cargando = signal(false);
  private readonly _error = signal<string | null>(null);

  readonly videojuegos = this._videojuegos.asReadonly();
  readonly cargando = this._cargando.asReadonly();
  readonly error = this._error.asReadonly();

  /** Carga el catálogo completo (GET /videojuego). */
  cargar(): void {
    this.iniciarCarga();
    this.api.listar().subscribe({
      next: datos => this.finalizarCarga(datos),
      error: err => this.manejarError(err)
    });
  }

  /** Busca con filtros opcionales (GET /videojuego/buscar). */
  buscar(filtros: { titulo?: string; plataforma?: string; genero?: string }): void {
    this.iniciarCarga();
    this.api.buscar(filtros).subscribe({
      next: datos => this.finalizarCarga(datos),
      error: err => this.manejarError(err)
    });
  }

  private iniciarCarga(): void {
    this._cargando.set(true);
    this._error.set(null);
  }

  private finalizarCarga(datos: Videojuego[]): void {
    this._videojuegos.set(datos);
    this._cargando.set(false);
  }

  /** Traduce el error HTTP a un mensaje entendible para el usuario. */
  private manejarError(err: HttpErrorResponse): void {
    this._cargando.set(false);
    this._error.set(
      err.status === 0
        ? 'No se pudo conectar con el servicio de videojuegos. Verifica que esté en ejecución.'
        : err.error?.mensaje ?? `Error inesperado (${err.status}).`
    );
  }
}