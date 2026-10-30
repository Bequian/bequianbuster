package com.example.ms_usuarios.dto;
import com.example.ms_usuarios.model.Rol;
import com.example.ms_usuarios.model.User;

public record UserResponse(Long id,String username,String email,Rol rol) {

    public static UserResponse de(User user) {

        return new UserResponse(user.getId(),user.getUsername(),user.getEmail(),user.getRol());
        
    }
}