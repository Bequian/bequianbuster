import { Component, input } from '@angular/core';

import { REGLAS_PASSWORD } from '../../validators/password-rules';

/**
 * Muestra la lista de reglas de contraseña y marca las que se cumplen
 * a medida que el usuario escribe.
 * Uso: <app-reglas-password [password]="miPassword" />
 */
@Component({
  selector: 'app-reglas-password',
  template: `
    <ul class="list-unstyled small mb-0 bb-reglas">
      @for (regla of reglas; track regla.id) {
        <li [class.cumple]="regla.cumple(password())">
          <i class="bi" [class.bi-check-circle-fill]="regla.cumple(password())"
             [class.bi-circle]="!regla.cumple(password())"></i>
          {{ regla.texto }}
        </li>
      }
    </ul>
  `,
  styles: `
    .bb-reglas li { color: var(--bb-texto-suave); margin-bottom: .15rem; }
    .bb-reglas li.cumple { color: #22c55e; }
    .bb-reglas i { margin-right: .4rem; }
  `
})
export class ReglasPassword {
  readonly password = input<string>('');
  protected readonly reglas = REGLAS_PASSWORD;
}