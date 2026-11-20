/** Cuenta propia (UserResponse de ms-usuarios). */
export interface Cuenta {
  id: number;
  username: string;
  email: string;
  rol: 'ADMIN' | 'CLIENTE';
}

/** Body de POST /auth/registro. */
export interface RegistroDatos {
  username: string;
  email: string;
  password: string;
}

/** Body de PUT /auth/perfil. Las contraseñas solo van si se quiere cambiarla. */
export interface PerfilDatos {
  email: string;
  passwordActual?: string;
  passwordNueva?: string;
}