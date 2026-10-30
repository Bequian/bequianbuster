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

@Component 
public class RemoteUserProvider implements AuthenticationProvider {

    private final RestClient restClient;

    public RemoteUserProvider(@Value("${bequianbuster.users.url}") String remoteUrl) {
        this.restClient = RestClient.builder().baseUrl(remoteUrl).build();
    }

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

    @Override
    public boolean supports(Class<?> tipo) {

        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(tipo);
        
    }
}
