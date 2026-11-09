import { Injectable, inject } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { Videojuego, VideojuegoRequest } from '../models/videojuego';

/**
 * Servicio de acceso a ms-videojuegos.
 * Su única responsabilidad es la comunicación HTTP: no guarda estado
 * ni maneja la lógica de pantalla.
 *
 * providedIn: 'root' → una sola instancia compartida (patrón Singleton).
 * Cada método devuelve un Observable (patrón Observer): la petición se
 * ejecuta cuando alguien se suscribe y notifica cuando llega la respuesta.
 */
@Injectable({ providedIn: 'root' })
export class VideojuegoApi {

  private readonly http = inject(HttpClient);
  private readonly url = environment.apiVideojuegos;

  /** GET /videojuego */
  listar(): Observable<Videojuego[]> {
    return this.http.get<Videojuego[]>(this.url);
  }

  /** GET /videojuego/{id} */
  buscarPorId(id: number): Observable<Videojuego> {
    return this.http.get<Videojuego>(`${this.url}/${id}`);
  }

  /** GET /videojuego/buscar?titulo=&plataforma=&genero= */
  buscar(filtros: { titulo?: string; plataforma?: string; genero?: string }): Observable<Videojuego[]> {
    let params = new HttpParams();
    if (filtros.titulo)     { params = params.set('titulo', filtros.titulo); }
    if (filtros.plataforma) { params = params.set('plataforma', filtros.plataforma); }
    if (filtros.genero)     { params = params.set('genero', filtros.genero); }
    return this.http.get<Videojuego[]>(`${this.url}/buscar`, { params });
  }

  /** POST /videojuego */
  crear(datos: VideojuegoRequest): Observable<Videojuego> {
    return this.http.post<Videojuego>(this.url, datos);
  }

  /** PUT /videojuego/{id} */
  actualizar(id: number, datos: VideojuegoRequest): Observable<Videojuego> {
    return this.http.put<Videojuego>(`${this.url}/${id}`, datos);
  }

  /** DELETE /videojuego/{id} */
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}