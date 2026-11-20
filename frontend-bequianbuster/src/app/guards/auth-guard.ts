import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';

import { Auth } from '../services/auth';

/** Permite entrar solo con sesión activa (cualquier rol). */
export const authGuard: CanActivateFn = () => {
  const auth = inject(Auth);
  const router = inject(Router);
  return auth.estaAutenticado() ? true : router.createUrlTree(['/login']);
};