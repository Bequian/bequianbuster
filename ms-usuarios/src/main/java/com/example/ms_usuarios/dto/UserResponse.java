package com.example.ms_usuarios.dto;
import com.example.ms_usuarios.model.Rol;
import com.example.ms_usuarios.model.User;

/**
 * Representa la respuesta de un usuario.
 * Contiene la información que se devuelve al cliente después de realizar operaciones con usuarios.
 * UserResponse
 * @param id ID del usuario.
 * @param username Nombre de usuario.
 * @param email Correo electrónico del usuario.
 * @param rol Rol del usuario.
 */
public record UserResponse(Long id,String username,String email,Rol rol) {

    public static UserResponse de(User user) {

        return new UserResponse(user.getId(),user.getUsername(),user.getEmail(),user.getRol());
        
    }
}