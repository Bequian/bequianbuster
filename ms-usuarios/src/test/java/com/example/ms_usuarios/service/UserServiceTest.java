package com.example.ms_usuarios.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.ms_usuarios.dto.PerfilUpdateRequest;
import com.example.ms_usuarios.dto.RegistroRequest;
import com.example.ms_usuarios.dto.UserResponse;
import com.example.ms_usuarios.dto.UserUpdateRequest;
import com.example.ms_usuarios.error.BussinessException;
import com.example.ms_usuarios.model.Rol;
import com.example.ms_usuarios.model.User;
import com.example.ms_usuarios.repository.UserRepository;

/**
 * Pruebas unitarias de las reglas de negocio de las cuentas.
 * El repositorio es un mock; el codificador es un BCrypt real (con costo bajo
 * para que las pruebas sean rápidas), así se verifica el hash de verdad.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repo;

    private final PasswordEncoder encoder = new BCryptPasswordEncoder(4);
    private UserService servicio;

    @BeforeEach
    void preparar() {
        servicio = new UserService(repo, encoder);
    }

    // ---------- Registro ----------

    @Test
    @DisplayName("El registro crea la cuenta como CLIENTE, en minúsculas y con la contraseña hasheada")
    void registroCreaCliente() {
        when(repo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        UserResponse creado = servicio.registrar(
            new RegistroRequest("Jugador1", "Jugador1@Duocplay.TEST", "Clave#2026"));

        ArgumentCaptor<User> guardado = ArgumentCaptor.forClass(User.class);
        verify(repo).save(guardado.capture());

        assertThat(creado.rol()).isEqualTo(Rol.CLIENTE);
        assertThat(guardado.getValue().getUsername()).isEqualTo("jugador1");
        assertThat(guardado.getValue().getEmail()).isEqualTo("jugador1@duocplay.test");
        assertThat(guardado.getValue().getPassHash())
            .isNotEqualTo("Clave#2026")
            .satisfies(hash -> assertThat(encoder.matches("Clave#2026", hash)).isTrue());
    }

    @Test
    @DisplayName("Registrar un usuario o correo existente responde 409 y no guarda")
    void registroDuplicado() {
        when(repo.existsByUsername("jugador1")).thenReturn(true);

        assertThatThrownBy(() -> servicio.registrar(
                new RegistroRequest("jugador1", "otro@duocplay.test", "Clave#2026")))
            .isInstanceOfSatisfying(BussinessException.class,
                e -> assertThat(e.estado()).isEqualTo(HttpStatus.CONFLICT));
        verify(repo, never()).save(any());
    }

    // ---------- Perfil propio ----------

    @Test
    @DisplayName("Cambiar la contraseña con la actual incorrecta responde 400 y no guarda")
    void perfilPasswordActualIncorrecta() {
        when(repo.findByUsername("jugador1")).thenReturn(Optional.of(cuenta("jugador1", "Clave#2026", Rol.CLIENTE)));

        assertThatThrownBy(() -> servicio.actualizarPerfil("jugador1",
                new PerfilUpdateRequest("jugador1@duocplay.test", "Equivocada#1", "Nueva#2026")))
            .isInstanceOfSatisfying(BussinessException.class,
                e -> assertThat(e.estado()).isEqualTo(HttpStatus.BAD_REQUEST));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("Cambiar la contraseña con la actual correcta guarda el nuevo hash")
    void perfilCambiaPassword() {
        User cuenta = cuenta("jugador1", "Clave#2026", Rol.CLIENTE);
        when(repo.findByUsername("jugador1")).thenReturn(Optional.of(cuenta));
        when(repo.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));

        servicio.actualizarPerfil("jugador1",
            new PerfilUpdateRequest("jugador1@duocplay.test", "Clave#2026", "Nueva#2026"));

        assertThat(encoder.matches("Nueva#2026", cuenta.getPassHash())).isTrue();
        assertThat(encoder.matches("Clave#2026", cuenta.getPassHash())).isFalse();
    }

    @Test
    @DisplayName("Cambiar el correo a uno de otra cuenta responde 409")
    void perfilCorreoDeOtraCuenta() {
        when(repo.findByUsername("jugador1")).thenReturn(Optional.of(cuenta("jugador1", "Clave#2026", Rol.CLIENTE)));
        when(repo.existsByEmail("admin@duocplay.test")).thenReturn(true);

        assertThatThrownBy(() -> servicio.actualizarPerfil("jugador1",
                new PerfilUpdateRequest("admin@duocplay.test", null, null)))
            .isInstanceOfSatisfying(BussinessException.class,
                e -> assertThat(e.estado()).isEqualTo(HttpStatus.CONFLICT));
    }

    // ---------- Protección del último ADMIN ----------

    @Test
    @DisplayName("No se puede quitar el rol al último ADMIN")
    void noQuitarUltimoAdmin() {
        when(repo.findById(1L)).thenReturn(Optional.of(cuenta("admin", "Clave#2026", Rol.ADMIN)));
        when(repo.countByRol(Rol.ADMIN)).thenReturn(1L);

        assertThatThrownBy(() -> servicio.actualizar(1L,
                new UserUpdateRequest("admin@duocplay.test", null, Rol.CLIENTE)))
            .isInstanceOfSatisfying(BussinessException.class,
                e -> assertThat(e.estado()).isEqualTo(HttpStatus.CONFLICT));
        verify(repo, never()).save(any());
    }

    @Test
    @DisplayName("No se puede eliminar al último ADMIN")
    void noEliminarUltimoAdmin() {
        when(repo.findById(1L)).thenReturn(Optional.of(cuenta("admin", "Clave#2026", Rol.ADMIN)));
        when(repo.countByRol(Rol.ADMIN)).thenReturn(1L);

        assertThatThrownBy(() -> servicio.eliminar(1L))
            .isInstanceOfSatisfying(BussinessException.class,
                e -> assertThat(e.estado()).isEqualTo(HttpStatus.CONFLICT));
        verify(repo, never()).delete(any());
    }

    @Test
    @DisplayName("Con más de un ADMIN, sí se puede eliminar uno")
    void eliminarAdminSiHayOtro() {
        User admin = cuenta("admin2", "Clave#2026", Rol.ADMIN);
        when(repo.findById(2L)).thenReturn(Optional.of(admin));
        when(repo.countByRol(Rol.ADMIN)).thenReturn(2L);

        servicio.eliminar(2L);

        verify(repo).delete(admin);
    }

    // ---------- Datos de prueba ----------

    private User cuenta(String username, String password, Rol rol) {
        return new User(1L, username, username + "@duocplay.test", encoder.encode(password), rol);
    }
}