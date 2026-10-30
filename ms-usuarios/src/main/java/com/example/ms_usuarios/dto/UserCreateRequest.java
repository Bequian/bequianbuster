package com.example.ms_usuarios.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import com.example.ms_usuarios.model.Rol;


public record UserCreateRequest(

    /**
     * El nombre de usuario del nuevo usuario.
     */
    @NotBlank(message = "El username es obligatorio")
    @Size(min = 3, max = 50, message = "El username debe tener entre 3 y 50 caracteres")
    @Pattern(regexp = ValidationRules.USERNAME, message = "El username debe comenzar con una letra y solo admite letras, números, punto, guion y guion bajo") 
    String username,


    /**
     * El correo electrónico del nuevo usuario.
     */
    @NotBlank(message = "El correo es obligatorio")
    @Size(max = 120, message = "El correo supera los 120 caracteres")
    @Email(regexp = ValidationRules.EMAIL, message = "El correo debe tener un formato válido (ej: usuario@dominio.cl)") 
    String email,


    /**
     * La contraseña del nuevo usuario.
     */
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 72, message = "La contraseña debe tener entre 8 y 72 caracteres")
    @Pattern(regexp = ValidationRules.PASSWORD, message = "La contraseña debe incluir al menos una mayúscula, una minúscula y un número") 
    String password,


    /**
     * El rol del nuevo usuario (ADMIN o CLIENTE).
     */
    @NotNull(message = "El rol es obligatorio y debe ser ADMIN o CLIENTE") Rol rol
    
) {}