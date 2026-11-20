import { Component, OnInit, inject, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';

import { Auth } from '../../services/auth';
import { Cuenta, PerfilDatos } from '../../models/cuenta';
import { PasswordSegura } from '../../validators/password-segura';
import { PATRON_EMAIL } from '../../validators/password-rules';
import { ReglasPassword } from '../../components/reglas-password/reglas-password';

/** Modificación del perfil propio: correo y, opcionalmente, contraseña. */
@Component({
  selector: 'app-perfil',
  imports: [FormsModule, PasswordSegura, ReglasPassword],
  templateUrl: './perfil.html',
  styleUrl: './perfil.css'
})
export class Perfil implements OnInit {

  private readonly auth = inject(Auth);

  protected readonly patronEmail = PATRON_EMAIL;
  protected readonly cuenta = signal<Cuenta | null>(null);

  protected email = '';
  protected cambiarPassword = false;
  protected passwordActual = '';
  protected passwordNueva = '';
  protected confirmacion = '';

  protected readonly cargando = signal(true);
  protected readonly guardando = signal(false);
  protected readonly exito = signal<string | null>(null);
  protected readonly error = signal<string | null>(null);
  protected readonly erroresCampos = signal<Record<string, string>>({});

  ngOnInit(): void {
    this.auth.cuenta().subscribe({
      next: cuenta => {
        this.cuenta.set(cuenta);
        this.email = cuenta.email;
        this.cargando.set(false);
      },
      error: () => {
        this.cargando.set(false);
        this.error.set('No se pudieron cargar los datos de tu cuenta.');
      }
    });
  }

  protected get coinciden(): boolean {
    return this.passwordNueva === this.confirmacion;
  }

  protected guardar(f: NgForm): void {
    if (f.invalid || (this.cambiarPassword && !this.coinciden)) {
      f.control.markAllAsTouched();
      return;
    }

    const datos: PerfilDatos = { email: this.email.trim() };
    if (this.cambiarPassword) {
      datos.passwordActual = this.passwordActual;
      datos.passwordNueva = this.passwordNueva;
    }

    this.guardando.set(true);
    this.exito.set(null);
    this.error.set(null);
    this.erroresCampos.set({});

    this.auth.actualizarPerfil(datos).subscribe({
      next: cuenta => {
        this.cuenta.set(cuenta);
        this.guardando.set(false);
        this.exito.set(this.cambiarPassword
          ? 'Perfil y contraseña actualizados correctamente.'
          : 'Perfil actualizado correctamente.');
        this.cambiarPassword = false;
        this.passwordActual = this.passwordNueva = this.confirmacion = '';
        f.resetForm({ email: cuenta.email });
      },
      error: (err: HttpErrorResponse) => {
        this.guardando.set(false);
        this.erroresCampos.set(err.error?.campos ?? {});
        this.error.set(
          err.status === 409 ? 'Ese correo ya está registrado en otra cuenta.'
          : err.status === 0 ? 'No se pudo conectar con el servicio de usuarios.'
          : err.error?.mensaje ?? `Error inesperado (${err.status}).`
        );
      }
    });
  }
}