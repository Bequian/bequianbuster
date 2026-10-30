package com.example.ms_usuarios.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.example.ms_usuarios.model.Rol;

public record UserUpdateRequest(

    @NotBlank @Email @Size(max=120) String email,
    @Size(min=8,max=72) String password,
    @NotNull Rol rol

) {}
    