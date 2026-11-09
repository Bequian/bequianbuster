import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';

import { environment } from '../../environments/environment';
import { Auth } from '../services/auth';

/**
 * Interceptor de autenticación.
 * Se ejecuta antes de cada petición HTTP y:
 *  1. Agrega X-Requested-With, para que Spring responda 401 sin abrir
 *     la ventana nativa de login del navegador.
 *  2. Agrega Authorization si hay sesión activa.
 * Solo actúa sobre las URLs de nuestros microservicios, para no enviar
 * credenciales a otros dominios.
 */
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const auth = inject(Auth);

  const esNuestraApi =
    req.url.startsWith(environment.apiVideojuegos) ||
    req.url.startsWith(environment.apiUsuarios);

  if (!esNuestraApi) {
    return next(req);
  }

  let headers = req.headers.set('X-Requested-With', 'XMLHttpRequest');

  // Si la petición ya trae Authorization (el login), no se reemplaza
  const sesion = auth.sesion();
  if (sesion && !headers.has('Authorization')) {
    headers = headers.set('Authorization', `Basic ${sesion.credenciales}`);
  }

  return next(req.clone({ headers })).pipe(
    catchError((err: HttpErrorResponse) => {
      // Credenciales guardadas ya no válidas (ej: se cambió la contraseña)
      if (err.status === 401 && sesion) {
        auth.cerrarSesion();
      }
      return throwError(() => err);
    })
  );
};