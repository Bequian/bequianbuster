import { Routes } from '@angular/router';
import { Catalogo } from './pages/catalogo/catalogo';
import { Detalle } from './pages/detalle/detalle';

export const routes: Routes = [
  { path: '', redirectTo: 'catalogo', pathMatch: 'full' },
  { path: 'catalogo', component: Catalogo },
  { path: 'videojuego/:id', component: Detalle },
  { path: '**', redirectTo: 'catalogo' }
];