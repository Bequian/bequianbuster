import { Routes } from '@angular/router';
import { Catalogo } from './pages/catalogo/catalogo';

export const routes: Routes = [
  { path: '', redirectTo: 'catalogo', pathMatch: 'full' },
  { path: 'catalogo', component: Catalogo },
  { path: '**', redirectTo: 'catalogo' }
];