import { Routes } from '@angular/router';
import { Catalogo } from './pages/catalogo/catalogo';
import { Detalle } from './pages/detalle/detalle';
import { Login } from './pages/login/login';
import { Formulario } from './pages/formulario/formulario';
import { adminGuard } from './guards/admin-guard';
import { Registro } from './pages/registro/registro';
import { Recuperar } from './pages/recuperar/recuperar';
import { Perfil } from './pages/perfil/perfil';
import { authGuard } from './guards/auth-guard';
import { Carrito } from './pages/carrito/carrito';
import { Checkout } from './pages/checkout/checkout';
import { rolGuard } from './guards/rol-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'catalogo', pathMatch: 'full' },
  { path: 'catalogo', component: Catalogo },
  { path: 'videojuego/:id', component: Detalle },
  { path: 'login', component: Login },
  { path: 'admin/nuevo', component: Formulario, canActivate: [adminGuard] },
  { path: 'admin/editar/:id', component: Formulario, canActivate: [adminGuard] },
  { path: 'registro', component: Registro },
  { path: 'recuperar', component: Recuperar },
  { path: 'perfil', component: Perfil, canActivate: [authGuard] },
  { path: 'carrito', component: Carrito },
  { path: 'checkout', component: Checkout, canActivate: [authGuard] },
  { path: '**', redirectTo: 'catalogo' }
];