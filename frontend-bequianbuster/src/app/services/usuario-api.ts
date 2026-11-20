import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { environment } from '../../environments/environment';
import { Cuenta } from '../models/cuenta';

/**
 * Acceso a la administración de cuentas de ms-usuarios (solo ADMIN).
 * El interceptor agrega las credenciales automáticamente.
 */
@Injectable({ providedIn: 'root' })
export class UsuarioApi {

  private readonly http = inject(HttpClient);
  private readonly url = `${environment.apiUsuarios}/usuarios`;

  /** GET /usuarios */
  listar(): Observable<Cuenta[]> {
    return this.http.get<Cuenta[]>(this.url);
  }

  /** PUT /usuarios/{id}: el backend exige email y rol; la contraseña no se envía. */
  cambiarRol(usuario: Cuenta, rol: Cuenta['rol']): Observable<Cuenta> {
    return this.http.put<Cuenta>(`${this.url}/${usuario.id}`, { email: usuario.email, rol });
  }

  /** DELETE /usuarios/{id} */
  eliminar(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}