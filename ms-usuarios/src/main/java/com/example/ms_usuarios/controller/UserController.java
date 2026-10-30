package com.example.ms_usuarios.controller;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.ms_usuarios.dto.UserCreateRequest;
import com.example.ms_usuarios.dto.UserUpdateRequest;
import com.example.ms_usuarios.dto.UserResponse;
import com.example.ms_usuarios.service.UserService;

/**
 * Controlador que maneja las operaciones CRUD para los usuarios.
 * Proporciona endpoints para listar, buscar, crear, actualizar y eliminar usuarios.
 * 
 * UserController maneja las solicitudes HTTP relacionadas con los usuarios.
 */
@RestController
@RequestMapping("/usuarios")
public class UserController {

    private final UserService servicio;

    public UserController(UserService servicio){this.servicio=servicio;}

    /**
     * Lista todos los usuarios.
     * @return Lista de usuarios.
     */
    @GetMapping public List<UserResponse> listar(){
        return servicio.listar();
    }

    /**
     * Busca un usuario por su ID.
     * @param id ID del usuario.
     * @return Usuario encontrado.
     */
    @GetMapping("/{id}") public UserResponse buscar(@PathVariable Long id){
        return servicio.buscar(id);
    }

    /**
     * Crea un nuevo usuario.
     * @param datos Datos del usuario a crear.
     * @return Usuario creado.
     */
    @PostMapping public ResponseEntity<UserResponse> crear(
            @Valid @RequestBody UserCreateRequest datos){
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio.crear(datos));
    }

    /**
     * Actualiza un usuario existente.
     * @param id ID del usuario a actualizar.
     * @param datos Datos actualizados del usuario.
     * @return Usuario actualizado.
     */
    @PutMapping("/{id}") public UserResponse actualizar(@PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest datos){
        return servicio.actualizar(id,datos);
    }

    /**
     * Elimina un usuario por su ID.
     * @param id ID del usuario a eliminar.
     * @return Respuesta sin contenido.
     */
    @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Long id){
        servicio.eliminar(id);return ResponseEntity.noContent().build();
    }
}
