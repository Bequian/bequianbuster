import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

import { Auth } from '../services/auth';

/**
 * Permite entrar a la ruta solo si el usuario es ADMIN.
 * Si no, lo envía al login.
 * Es solo para la experiencia de usuario: la seguridad real
 * la sigue aplicando el backend (responde 401/403).
 */
export const adminGuard: CanActivateFn = () => {
  const auth = inject(Auth);
  const router = inject(Router);

  return auth.esAdmin() ? true : router.createUrlTree(['/login']);
};