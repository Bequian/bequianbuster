package com.example.ms_usuarios.security;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.ms_usuarios.repository.UserRepository;

/**
 * Servicio de detalles de usuario para Spring Security.
 * Busca la cuenta en la base de datos y entrega su hash BCrypt a Spring Security.
 */
@Service
public class UserDetailService implements UserDetailsService {

    // Repositorio de usuarios para acceder a la base de datos.
    private final UserRepository repo;

    // Constructor que recibe el repositorio de usuarios.
    public UserDetailService(UserRepository repo){this.repo=repo;}

    // Método que carga los detalles del usuario por nombre de usuario.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String normalizado = username.toLowerCase(java.util.Locale.ROOT);
        var usuario=repo.findByUsername(normalizado).orElseThrow(() ->
            new UsernameNotFoundException("Usuario no encontrado"));
        return User.withUsername(usuario.getUsername())
            .password(usuario.getPassHash())
            .roles(usuario.getRol().name()).build();
    }
}
