import { Component, OnInit, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

import { UsuarioApi } from '../../services/usuario-api';
import { Auth } from '../../services/auth';
import { Cuenta } from '../../models/cuenta';

/**
 * Gestión de cuentas (solo ADMIN).
 * La lista completa se carga una vez; los filtros e indicadores
 * se calculan sobre el arreglo en memoria con computed().
 */
@Component({
  selector: 'app-usuarios',
  imports: [FormsModule],
  templateUrl: './usuarios.html',
  styleUrl: './usuarios.css'
})
export class Usuarios implements OnInit {

  private readonly api = inject(UsuarioApi);
  protected readonly auth = inject(Auth);

  protected readonly usuarios = signal<Cuenta[]>([]);
  protected readonly cargando = signal(true);
  protected readonly error = signal<string | null>(null);
  protected readonly exito = signal<string | null>(null);

  // Filtros
  protected readonly filtroTexto = signal('');
  protected readonly filtroRol = signal<'' | Cuenta['rol']>('');

  // Indicadores y lista filtrada: se recalculan solos al cambiar las señales
  protected readonly totalAdmins = computed(() => this.usuarios().filter(u => u.rol === 'ADMIN').length);
  protected readonly totalClientes = computed(() => this.usuarios().filter(u => u.rol === 'CLIENTE').length);

  protected readonly filtrados = computed(() => {
    const texto = this.filtroTexto().trim().toLowerCase();
    const rol = this.filtroRol();
    return this.usuarios().filter(u =>
      (!rol || u.rol === rol) &&
      (!texto || u.username.includes(texto) || u.email.includes(texto)));
  });

  ngOnInit(): void {
    this.cargar();
  }

  protected cargar(): void {
    this.cargando.set(true);
    this.error.set(null);
    this.api.listar().subscribe({
      next: lista => {
        this.usuarios.set(lista);
        this.cargando.set(false);
      },
      error: (err: HttpErrorResponse) => {
        this.cargando.set(false);
        this.mostrarError(err);
      }
    });
  }

  /** Evita que el admin modifique o elimine su propia cuenta. */
  protected esPropia(u: Cuenta): boolean {
    return u.username === this.auth.sesion()?.usuario;
  }

  protected cambiarRol(u: Cuenta, rol: Cuenta['rol']): void {
    if (rol === u.rol) {
      return;
    }
    this.limpiarMensajes();
    this.api.cambiarRol(u, rol).subscribe({
      next: actualizado => {
        this.usuarios.update(lista => lista.map(x => x.id === actualizado.id ? actualizado : x));
        this.exito.set(`"${u.username}" ahora es ${rol}.`);
      },
      error: (err: HttpErrorResponse) => {
        this.mostrarError(err);
        // Fuerza a la vista a volver al rol original
        this.usuarios.update(lista => [...lista]);
      }
    });
  }

  protected eliminar(u: Cuenta): void {
    if (!confirm(`¿Eliminar la cuenta "${u.username}"? Esta acción no se puede deshacer.`)) {
      return;
    }
    this.limpiarMensajes();
    this.api.eliminar(u.id).subscribe({
      next: () => {
        this.usuarios.update(lista => lista.filter(x => x.id !== u.id));
        this.exito.set(`Cuenta "${u.username}" eliminada.`);
      },
      error: (err: HttpErrorResponse) => this.mostrarError(err)
    });
  }

  private limpiarMensajes(): void {
    this.error.set(null);
    this.exito.set(null);
  }

  private mostrarError(err: HttpErrorResponse): void {
    this.error.set(
      err.status === 409 ? err.error?.mensaje ?? 'Operación no permitida.'
      : err.status === 403 ? 'No tienes permisos para gestionar usuarios.'
      : err.status === 0 ? 'No se pudo conectar con el servicio de usuarios.'
      : err.error?.mensaje ?? `Error inesperado (${err.status}).`
    );
  }
}