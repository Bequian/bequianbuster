import { Component, inject, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { Router, RouterLink } from '@angular/router';
import { switchMap } from 'rxjs';

import { Auth } from '../../services/auth';
import { PasswordSegura } from '../../validators/password-segura';
import { PATRON_EMAIL, PATRON_USERNAME } from '../../validators/password-rules';
import { ReglasPassword } from '../../components/reglas-password/reglas-password';

/** Registro público: crea una cuenta CLIENTE e inicia sesión automáticamente. */
@Component({
  selector: 'app-registro',
  imports: [FormsModule, RouterLink, PasswordSegura, ReglasPassword],
  templateUrl: './registro.html',
  styleUrl: './registro.css'
})
export class Registro {

  private readonly auth = inject(Auth);
  private readonly router = inject(Router);

  protected readonly patronUsername = PATRON_USERNAME;
  protected readonly patronEmail = PATRON_EMAIL;

  protected datos = { username: '', email: '', password: '' };
  protected confirmacion = '';
  protected aceptaTerminos = false;

  protected readonly verPassword = signal(false);
  protected readonly enviando = signal(false);
  protected readonly error = signal<string | null>(null);
  protected readonly erroresCampos = signal<Record<string, string>>({});

  protected get coinciden(): boolean {
    return this.datos.password === this.confirmacion;
  }

  protected registrar(f: NgForm): void {
    if (f.invalid || !this.coinciden) {
      f.control.markAllAsTouched();
      return;
    }

    const username = this.datos.username.trim();
    this.enviando.set(true);
    this.error.set(null);
    this.erroresCampos.set({});

    // 1) Registrar la cuenta  2) Iniciar sesión con las mismas credenciales
    this.auth.registrar({ username, email: this.datos.email.trim(), password: this.datos.password })
      .pipe(switchMap(() => this.auth.login(username, this.datos.password)))
      .subscribe({
        next: () => this.router.navigate(['/catalogo']),
        error: (err: HttpErrorResponse) => {
          this.enviando.set(false);
          if (err.status === 400) {
            this.erroresCampos.set(err.error?.campos ?? {});
            this.error.set(err.error?.mensaje ?? 'Revisa los datos ingresados.');
          } else if (err.status === 409) {
            this.error.set('El nombre de usuario o el correo ya están registrados.');
          } else if (err.status === 0) {
            this.error.set('No se pudo conectar con el servicio de usuarios.');
          } else {
            this.error.set(`Error inesperado (${err.status}).`);
          }
        }
      });
  }
}