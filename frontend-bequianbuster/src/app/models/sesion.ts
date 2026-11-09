/** Respuesta de ms-usuarios en /auth/login y /auth/perfil (ProfileResponse). */
export interface Perfil {
  user: string;
  rol: 'ADMIN' | 'CLIENTE';
}

/** Sesión activa en el frontend. */
export interface Sesion {
  usuario: string;
  rol: 'ADMIN' | 'CLIENTE';
  credenciales: string;   // usuario:contraseña en Base64 (HTTP Basic)
}