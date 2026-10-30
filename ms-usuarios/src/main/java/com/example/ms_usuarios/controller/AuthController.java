package com.example.ms_usuarios.controller;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ms_usuarios.dto.ProfileResponse;
/**
 * GET /api/auth/perfil es una comprobacion de login mediante HTTP Basic.
 * La clave no forma parte del JSON de respuesta.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {
    /** Comprueba credenciales como operacion de inicio de sesion sin emitir token. */
    @PostMapping("/login")
    public ProfileResponse login(Authentication autenticado) { return perfil(autenticado); }
    @GetMapping("/perfil")
    public ProfileResponse perfil(Authentication autenticado) {
        // La primera autoridad procede del rol persistido en Oracle.
        String rol=autenticado.getAuthorities().iterator().next().getAuthority()
            .replaceFirst("^ROLE_","");
        return new ProfileResponse(autenticado.getName(),rol);
    }
}
