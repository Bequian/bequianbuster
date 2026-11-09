import { Component, inject } from '@angular/core';
import { Router, RouterLink, RouterOutlet } from '@angular/router';

import { Auth } from './services/auth';

@Component({
  selector: 'app-root',
  imports: [RouterOutlet, RouterLink],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {

  protected readonly auth = inject(Auth);
  private readonly router = inject(Router);

  protected salir(): void {
    this.auth.cerrarSesion();
    this.router.navigate(['/catalogo']);
  }
}