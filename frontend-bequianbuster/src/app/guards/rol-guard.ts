import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

import { Auth } from '../services/auth';

/**
 * Patrón Factory: crea un guard que solo deja pasar al rol indicado.
 * Uso en las rutas: canActivate: [rolGuard('CLIENTE')]
 * Sin sesión → login. Con otro rol → catálogo.
 */
export function rolGuard(rol: 'ADMIN' | 'CLIENTE'): CanActivateFn {
  return () => {
    const auth = inject(Auth);
    const router = inject(Router);
    const sesion = auth.sesion();

    if (!sesion) {
      return router.createUrlTree(['/login']);
    }
    return sesion.rol === rol ? true : router.createUrlTree(['/catalogo']);
  };
}