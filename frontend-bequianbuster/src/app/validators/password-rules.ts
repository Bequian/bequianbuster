/**
 * Reglas de seguridad de contraseñas.
 * Son las mismas que valida ms-usuarios (ValidationRules.PASSWORD + @Size).
 * Las usan la directiva PasswordSegura y el componente ReglasPassword.
 */
export interface ReglaPassword {
  id: string;
  texto: string;
  cumple: (valor: string) => boolean;
}

export const REGLAS_PASSWORD: ReglaPassword[] = [
  { id: 'longitud',  texto: 'Entre 8 y 72 caracteres',             cumple: v => v.length >= 8 && v.length <= 72 },
  { id: 'mayuscula', texto: 'Al menos una letra mayúscula',        cumple: v => /[A-Z]/.test(v) },
  { id: 'minuscula', texto: 'Al menos una letra minúscula',        cumple: v => /[a-z]/.test(v) },
  { id: 'numero',    texto: 'Al menos un número',                  cumple: v => /\d/.test(v) },
  { id: 'especial',  texto: 'Al menos un carácter especial (!@#$...)', cumple: v => /[^A-Za-z0-9\s]/.test(v) },
  { id: 'espacios',  texto: 'Sin espacios en blanco',              cumple: v => v.length > 0 && !/\s/.test(v) }
];

/** Mismos patrones que ValidationRules en ms-usuarios. */
export const PATRON_USERNAME = '^[A-Za-z][A-Za-z0-9._-]*$';
export const PATRON_EMAIL = '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$';