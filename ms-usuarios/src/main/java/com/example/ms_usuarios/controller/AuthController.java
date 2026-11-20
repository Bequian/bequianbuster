package com.example.ms_usuarios.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.ms_usuarios.dto.PerfilUpdateRequest;
import com.example.ms_usuarios.dto.ProfileResponse;
import com.example.ms_usuarios.dto.RegistroRequest;
import com.example.ms_usuarios.dto.UserResponse;
import com.example.ms_usuarios.service.UserService;

import jakarta.validation.Valid;

/**
 * Autenticación y operaciones sobre la cuenta propia.
 * GET /auth/perfil se mantiene sin cambios: lo usa ms-videojuegos para validar credenciales.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService servicio;

    public AuthController(UserService servicio) {
        this.servicio = servicio;
    }

    /** Comprueba credenciales (HTTP Basic) sin emitir token. */
    @PostMapping("/login")
    public ProfileResponse login(Authentication autenticado) {
        return perfil(autenticado);
    }

    /** Usuario y rol del autenticado. Usado por ms-videojuegos. */
    @GetMapping("/perfil")
    public ProfileResponse perfil(Authentication autenticado) {
        String rol = autenticado.getAuthorities().iterator().next().getAuthority()
            .replaceFirst("^ROLE_", "");
        return new ProfileResponse(autenticado.getName(), rol);
    }

    /** Registro público: crea una cuenta CLIENTE (201). */
    @PostMapping("/registro")
    public ResponseEntity<UserResponse> registrar(@Valid @RequestBody RegistroRequest datos) {
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.registrar(datos));
    }

    /** Datos completos de la cuenta propia. */
    @GetMapping("/cuenta")
    public UserResponse cuenta(Authentication autenticado) {
        return servicio.cuenta(autenticado.getName());
    }

    /** Actualiza el correo y, opcionalmente, la contraseña de la cuenta propia. */
    @PutMapping("/perfil")
    public UserResponse actualizarPerfil(Authentication autenticado,
                                         @Valid @RequestBody PerfilUpdateRequest datos) {
        return servicio.actualizarPerfil(autenticado.getName(), datos);
    }
}