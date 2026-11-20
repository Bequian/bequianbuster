/**
 * URLs de los microservicios en PRODUCCIÓN (contenedor Docker).
 * Son rutas relativas: el navegador llama al mismo Nginx que sirve
 * la aplicación, y Nginx las reenvía a cada microservicio por la red Docker.
 * En desarrollo (ng serve) se usa environment.development.ts.
 */
export const environment = {
  apiVideojuegos: '/api/videojuegos/videojuego',
  apiUsuarios: '/api/usuarios'
};