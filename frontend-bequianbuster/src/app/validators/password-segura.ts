import { Directive } from '@angular/core';
import { AbstractControl, NG_VALIDATORS, ValidationErrors, Validator } from '@angular/forms';

import { REGLAS_PASSWORD } from './password-rules';

/**
 * Validador para formularios con ngModel.
 * Uso: <input ... appPasswordSegura [(ngModel)]="password">
 * Si alguna regla falla, el campo queda inválido con el error
 * { passwordInsegura: ['mayuscula', 'especial', ...] }.
 * Un campo vacío no se evalúa aquí: eso lo controla "required".
 */
@Directive({
  selector: '[appPasswordSegura]',
  providers: [{ provide: NG_VALIDATORS, useExisting: PasswordSegura, multi: true }]
})
export class PasswordSegura implements Validator {

  validate(control: AbstractControl): ValidationErrors | null {
    const valor: string = control.value ?? '';
    if (!valor) {
      return null;
    }
    const fallidas = REGLAS_PASSWORD.filter(r => !r.cumple(valor)).map(r => r.id);
    return fallidas.length > 0 ? { passwordInsegura: fallidas } : null;
  }
}