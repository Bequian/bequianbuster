import { Routes } from '@angular/router';
import { Catalogo } from './pages/catalogo/catalogo';
import { Detalle } from './pages/detalle/detalle';
import { Login } from './pages/login/login';
import { Formulario } from './pages/formulario/formulario';
import { adminGuard } from './guards/admin-guard';

export const routes: Routes = [
  { path: '', redirectTo: 'catalogo', pathMatch: 'full' },
  { path: 'catalogo', component: Catalogo },
  { path: 'videojuego/:id', component: Detalle },
  { path: 'login', component: Login },
  { path: 'admin/nuevo', component: Formulario, canActivate: [adminGuard] },
  { path: 'admin/editar/:id', component: Formulario, canActivate: [adminGuard] },
  { path: '**', redirectTo: 'catalogo' }
];