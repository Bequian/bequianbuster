import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';

import { Auth } from '../../services/auth';

@Component({
  selector: 'app-login',
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class Login {

  private readonly auth = inject(Auth);
  private readonly router = inject(Router);

  protected usuario = '';
  protected password = '';
  protected readonly enviando = signal(false);
  protected readonly error = signal<string | null>(null);

  protected ingresar(): void {
    this.enviando.set(true);
    this.error.set(null);

    this.auth.login(this.usuario.trim(), this.password).subscribe({
      next: () => this.router.navigate(['/catalogo']),
      error: (err: HttpErrorResponse) => {
        this.enviando.set(false);
        this.error.set(
          err.status === 401 ? 'Usuario o contraseña incorrectos.'
          : err.status === 0 ? 'No se pudo conectar con el servicio de usuarios.'
          : `Error inesperado (${err.status}).`
        );
      }
    });
  }
}