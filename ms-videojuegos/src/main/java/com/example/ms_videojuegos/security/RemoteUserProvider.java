package com.example.ms_videojuegos.security;

import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Proveedor de autenticación que valida usuarios de manera remota.
 * Utiliza un servicio externo para obtener el perfil del usuario y su rol.
 * Proporciona métodos para autenticar usuarios y verificar sus credenciales de manera remota.
 * Contiene la lógica para interactuar con el servicio remoto y manejar errores de autenticación.
 * RemoteUserProvider se integra con Spring Security para la autenticación basada en usuarios remotos.
 */
@Component 
public class RemoteUserProvider implements AuthenticationProvider {

    /**
     * Cliente REST utilizado para comunicarse con el servicio remoto de usuarios.
     */
    private final RestClient restClient;

    /**
     * Constructor de RemoteUserProvider.
     * @param remoteUrl URL del servicio remoto de usuarios.
     */
    public RemoteUserProvider(@Value("${bequianbuster.users.url}") String remoteUrl) {
        this.restClient = RestClient.builder().baseUrl(remoteUrl).build();
    }

    /**
     * Autentica un usuario utilizando el servicio remoto.
     * @param authentication Objeto de autenticación que contiene las credenciales del usuario.
     * @return Objeto de autenticación autenticado si las credenciales son válidas.
     * @throws AuthenticationException Si ocurre un error durante la autenticación.
     * @throws BadCredentialsException Si las credenciales son inválidas.
     * @throws AuthenticationServiceException Si el servicio de usuarios no está disponible.
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // Implementación de la autenticación remota
        String user = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        try {
            RemoteProfile profile = restClient.get().uri("auth/perfil")
            .headers(header -> header.setBasicAuth(user, password))
            .retrieve().body(RemoteProfile.class);

            if (profile == null || profile.rol() == null || profile.user() == null || !user.equals(profile.user())) {
                throw new BadCredentialsException("Perfil remoto inválido");
            }

            return UsernamePasswordAuthenticationToken.authenticated(user, null, List.of(new SimpleGrantedAuthority("ROLE_"+profile.rol())));

        } catch (HttpClientErrorException.Unauthorized | HttpClientErrorException.Forbidden ex) {

            throw new BadCredentialsException("Credenciales inválidas");

        } catch (RestClientException ex) {

            throw new AuthenticationServiceException("Servicio de Usuarios no disponible", ex);

        }
    }

    /**
     * Indica si este proveedor de autenticación soporta el tipo de autenticación especificado.
     * @param tipo Clase del objeto de autenticación.
     * @return true si el tipo de autenticación es soportado, false en caso contrario.
     */
    @Override
    public boolean supports(Class<?> tipo) {

        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(tipo);
        
    }
}
