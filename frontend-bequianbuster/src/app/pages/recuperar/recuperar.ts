import { Component, signal } from '@angular/core';
import { FormsModule, NgForm } from '@angular/forms';
import { RouterLink } from '@angular/router';

import { PATRON_EMAIL } from '../../validators/password-rules';

/**
 * Recuperación de contraseña SIMULADA: no hay servicio de correo.
 * Valida el formulario y muestra el mensaje que vería el usuario.
 */
@Component({
  selector: 'app-recuperar',
  imports: [FormsModule, RouterLink],
  templateUrl: './recuperar.html'
})
export class Recuperar {

  protected readonly patronEmail = PATRON_EMAIL;
  protected email = '';
  protected readonly enviando = signal(false);
  protected readonly enviado = signal(false);

  protected enviar(f: NgForm): void {
    if (f.invalid) {
      f.control.markAllAsTouched();
      return;
    }
    this.enviando.set(true);
    // Simula la espera de un servicio de correo
    setTimeout(() => {
      this.enviando.set(false);
      this.enviado.set(true);
    }, 1200);
  }
}