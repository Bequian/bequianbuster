import { Routes } from '@angular/router';
import { Catalogo } from './pages/catalogo/catalogo';
import { Detalle } from './pages/detalle/detalle';
import { Login } from './pages/login/login';

export const routes: Routes = [
  { path: '', redirectTo: 'catalogo', pathMatch: 'full' },
  { path: 'catalogo', component: Catalogo },
  { path: 'videojuego/:id', component: Detalle },
  { path: 'login', component: Login },
  { path: '**', redirectTo: 'catalogo' }
];