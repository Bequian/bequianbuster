package com.example.ms_usuarios.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de seguridad para la aplicación.
 * Define las reglas de acceso a las rutas, la gestión de sesiones y el cifrado de contraseñas.
 * Utiliza autenticación básica y desactiva CSRF para APIs sin cookies.
 * Solo los usuarios con rol ADMIN pueden acceder a las rutas de administración de usuarios.
 * El perfil autenticado queda disponible para otros servicios.
 */
@Configuration
public class SecurityConfig {
    
    // Bean para el cifrado de contraseñas usando BCrypt.
    @Bean PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // Bean para la configuración de la cadena de filtros de seguridad.
    @Bean SecurityFilterChain filtros(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(a -> a
                .requestMatchers("/estado").permitAll()
                .requestMatchers("/auth/perfil","/auth/login").authenticated()
                .requestMatchers("/usuarios","/usuarios/**").hasRole("ADMIN")
                .anyRequest().denyAll())
            .httpBasic(Customizer.withDefaults());
        return http.build();
    }
}
