import { Injectable, inject, signal } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';

import { VideojuegoApi } from '../services/videojuego-api';

import { FormularioVideojuego, Videojuego, VideojuegoRequest } from '../models/videojuego';

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

  private readonly _seleccionado = signal<Videojuego | null>(null);
  readonly seleccionado = this._seleccionado.asReadonly();

  private readonly _guardando = signal(false);
  private readonly _erroresCampos = signal<Record<string, string>>({});

  readonly guardando = this._guardando.asReadonly();
  readonly erroresCampos = this._erroresCampos.asReadonly();

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

  /** Carga un videojuego por su ID (GET /videojuego/:id). */
  cargarDetalle(id: number): void {
    this._seleccionado.set(null);
    this.iniciarCarga();
    this.api.buscarPorId(id).subscribe({
      next: videojuego => {
        this._seleccionado.set(videojuego);
        this._cargando.set(false);
      },
      error: err => this.manejarError(err)
    });
  }

  /** Formulario vacío para crear un videojuego nuevo. */
  formularioVacio(): FormularioVideojuego {
    return {
      titulo: '', desarrollador: '', fechaLanzamiento: '', plataforma: '',
      genero: '', descripcion: '', precio: null, stock: null, imagen: ''
    };
  }

  /** Limpia los mensajes de error al entrar a una pantalla. */
  limpiarMensajes(): void {
    this._error.set(null);
    this._erroresCampos.set({});
  }

  /** Carga un videojuego y lo entrega convertido a formulario (para editar). */
  cargarParaEditar(id: number, alCargar: (form: FormularioVideojuego) => void): void {
    this.iniciarCarga();
    this.api.buscarPorId(id).subscribe({
      next: videojuego => {
        this._cargando.set(false);
        alCargar(this.aFormulario(videojuego));
      },
      error: err => this.manejarError(err)
    });
  }

  /**
   * Guarda el formulario: POST si id es null, PUT si trae id.
   * El componente no necesita saber qué método HTTP se usa.
   */
  guardar(id: number | null, form: FormularioVideojuego, alGuardar: (v: Videojuego) => void): void {
    this._guardando.set(true);
    this.limpiarMensajes();

    const datos = this.aRequest(form);
    const peticion = id === null
      ? this.api.crear(datos)
      : this.api.actualizar(id, datos);

    peticion.subscribe({
      next: videojuego => {
        this._guardando.set(false);
        alGuardar(videojuego);
      },
      error: err => {
        this._guardando.set(false);
        this.manejarError(err);
      }
    });
  }

  /** Elimina un videojuego (DELETE) y lo quita de la lista local. */
  eliminar(id: number): void {
    this.limpiarMensajes();
    this.api.eliminar(id).subscribe({
      next: () => this._videojuegos.update(lista => lista.filter(v => v.id !== id)),
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

    let mensaje: string;
    switch (err.status) {
      case 0:
        mensaje = 'No se pudo conectar con el servicio de videojuegos. Verifica que esté en ejecución.';
        break;
      case 400:
        // Errores de validación por campo (mapa "campos" del ErrorResponse)
        this._erroresCampos.set(err.error?.campos ?? {});
        mensaje = err.error?.mensaje ?? 'Los datos enviados no son válidos.';
        break;
      case 401:
        mensaje = 'Tu sesión no es válida. Inicia sesión nuevamente.';
        break;
      case 403:
        mensaje = 'No tienes permisos para realizar esta acción.';
        break;
      case 404:
        mensaje = err.error?.mensaje ?? 'El videojuego solicitado no existe.';
        break;
      default:
        mensaje = err.error?.mensaje ?? `Error inesperado (${err.status}).`;
    }
    this._error.set(mensaje);
  }

  /** API → formulario: dd-MM-yyyy pasa a yyyy-MM-dd. */
  private aFormulario(v: Videojuego): FormularioVideojuego {
    const [dia, mes, anio] = v.fechaLanzamiento.split('-');
    return {
      titulo: v.titulo,
      desarrollador: v.desarrollador,
      fechaLanzamiento: `${anio}-${mes}-${dia}`,
      plataforma: v.plataforma,
      genero: v.genero,
      descripcion: v.descripcion,
      precio: v.precio,
      stock: v.stock,
      imagen: v.imagen ?? ''
    };
  }

  /** Formulario → API: yyyy-MM-dd pasa a dd-MM-yyyy y se limpian los textos. */
  private aRequest(f: FormularioVideojuego): VideojuegoRequest {
    const [anio, mes, dia] = f.fechaLanzamiento.split('-');
    return {
      titulo: f.titulo.trim(),
      desarrollador: f.desarrollador.trim(),
      fechaLanzamiento: `${dia}-${mes}-${anio}`,
      plataforma: f.plataforma,
      genero: f.genero.trim(),
      descripcion: f.descripcion.trim(),
      precio: f.precio ?? 0,
      stock: f.stock ?? 0,
      imagen: f.imagen.trim() || null
    };
  }
}