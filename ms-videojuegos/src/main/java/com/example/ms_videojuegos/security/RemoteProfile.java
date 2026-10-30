package com.example.ms_videojuegos.security;

/**
 * Representa el perfil remoto de un usuario, incluyendo su nombre de usuario y rol.
 * @param user Nombre de usuario del perfil remoto.
 * @param rol Rol del usuario en el sistema.
 */
public record RemoteProfile(String user, String rol) {
}
