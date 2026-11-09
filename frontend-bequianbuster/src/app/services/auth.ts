import { Injectable, computed, inject, signal } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map, tap } from 'rxjs';

import { environment } from '../../environments/environment';
import { Perfil, Sesion } from '../models/sesion';

const CLAVE_SESION = 'bb_sesion';

/**
 * Maneja la sesión del usuario (Singleton: una sola sesión compartida).
 * Valida las credenciales contra ms-usuarios y guarda el perfil obtenido.
 *
 * La sesión se guarda en sessionStorage para que sobreviva a una recarga
 * de página y se borre al cerrar la pestaña.
 */
@Injectable({ providedIn: 'root' })
export class Auth {

  private readonly http = inject(HttpClient);
  private readonly _sesion = signal<Sesion | null>(this.leerSesionGuardada());

  readonly sesion = this._sesion.asReadonly();
  readonly estaAutenticado = computed(() => this._sesion() !== null);
  readonly esAdmin = computed(() => this._sesion()?.rol === 'ADMIN');

  /** POST /auth/login con HTTP Basic. Si responde 200, guarda la sesión. */
  login(usuario: string, password: string): Observable<Sesion> {
    const headers = new HttpHeaders({
      Authorization: `Basic ${this.codificar(usuario, password)}`
    });

    return this.http.post<Perfil>(`${environment.apiUsuarios}/auth/login`, null, { headers }).pipe(
      // Se usa el nombre normalizado que devuelve el backend (en minúsculas)
      map(perfil => ({
        usuario: perfil.user,
        rol: perfil.rol,
        credenciales: this.codificar(perfil.user, password)
      })),
      tap(sesion => this.guardarSesion(sesion))
    );
  }

  cerrarSesion(): void {
    this._sesion.set(null);
    try { sessionStorage.removeItem(CLAVE_SESION); } catch { /* sin almacenamiento */ }
  }

  /**
   * Codifica usuario:contraseña en Base64.
   * Se pasa por TextEncoder para soportar tildes y ñ,
   * que btoa() no acepta directamente.
   */
  private codificar(usuario: string, password: string): string {
    const bytes = new TextEncoder().encode(`${usuario}:${password}`);
    return btoa(String.fromCharCode(...bytes));
  }

  private guardarSesion(sesion: Sesion): void {
    this._sesion.set(sesion);
    try { sessionStorage.setItem(CLAVE_SESION, JSON.stringify(sesion)); } catch { /* sin almacenamiento */ }
  }

  private leerSesionGuardada(): Sesion | null {
    try {
      const guardada = sessionStorage.getItem(CLAVE_SESION);
      return guardada ? JSON.parse(guardada) as Sesion : null;
    } catch {
      return null;
    }
  }
}