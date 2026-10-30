package com.example.ms_usuarios.service;

import java.util.List;
import java.util.Locale;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.ms_usuarios.dto.UserCreateRequest;
import com.example.ms_usuarios.dto.UserUpdateRequest;
import com.example.ms_usuarios.dto.UserResponse;
import com.example.ms_usuarios.error.BussinessException;
import com.example.ms_usuarios.model.Rol;
import com.example.ms_usuarios.model.User;
import com.example.ms_usuarios.repository.UserRepository;

/**
 * Servicio de gestión de usuarios.
 * Proporciona métodos para listar, buscar, crear, actualizar y eliminar usuarios.
 * Gestiona la lógica de negocio relacionada con los usuarios, incluyendo la validación de roles y la codificación de contraseñas.
 * 
 * Contiene la lógica de negocio para la gestión de usuarios en la aplicación.
 *
 */
@Service
public class UserService {

    // Logger para registrar eventos importantes del servicio.
    private static final Logger log=LoggerFactory.getLogger(UserService.class);

    // Repositorio de usuarios para acceder a la base de datos.
    private final UserRepository repo;

    // Codificador de contraseñas para almacenar hashes seguros.
    private final PasswordEncoder encoder;

    // Constructor que recibe el repositorio de usuarios y el codificador de contraseñas.
    public UserService(UserRepository repo,PasswordEncoder encoder) {
        this.repo=repo;this.encoder=encoder;
    }

    // Método para listar todos los usuarios.
    @Transactional(readOnly=true)
    public List<UserResponse> listar() {
        return repo.findAll().stream().map(UserResponse::de).toList();
    }

    // Método para buscar un usuario por su ID.
    @Transactional(readOnly=true)
    public UserResponse buscar(Long id) {
        return UserResponse.de(obtener(id));
    }

    // Método auxiliar para obtener un usuario por su ID o lanzar una excepción si no existe.
    private User obtener(Long id) {
        return repo.findById(id).orElseThrow(()->
            new BussinessException(HttpStatus.NOT_FOUND,"Usuario no encontrado: "+id));
    }

    // Método para crear un nuevo usuario.
    @Transactional
    public UserResponse crear(UserCreateRequest req) {
        String username=req.username().toLowerCase(Locale.ROOT);
        String email=req.email().toLowerCase(Locale.ROOT);
        if (repo.existsByUsername(username) || repo.existsByEmail(email)) {
            throw new BussinessException(HttpStatus.CONFLICT,"Username o correo ya registrado");
        }
        User creado=repo.save(new User(null,username,email,
            encoder.encode(req.password()),req.rol()));
        log.info("Cuenta creada id={} rol={}",creado.getId(),creado.getRol());
        return UserResponse.de(creado);
    }

    // Método para actualizar un usuario existente.
    @Transactional
    public UserResponse actualizar(Long id,UserUpdateRequest req) {
        User actual=obtener(id);
        String email=req.email().toLowerCase(Locale.ROOT);
        if(!actual.getEmail().equals(email) && repo.existsByEmail(email)) {
            throw new BussinessException(HttpStatus.CONFLICT,"Correo ya registrado");
        }
        if (actual.getRol()==Rol.ADMIN && req.rol()!=Rol.ADMIN
             && repo.countByRol(Rol.ADMIN)<=1) {
            throw new BussinessException(HttpStatus.CONFLICT,"No se puede quitar el ultimo ADMIN");
        }
        actual.setEmail(email);
        actual.setRol(req.rol());
        // Una clave omitida (null) conserva la anterior; una nueva se codifica.
        if(req.password()!=null) actual.setPassHash(encoder.encode(req.password()));
        User guardado=repo.save(actual);
        log.info("Cuenta actualizada id={}",id);
        return UserResponse.de(guardado);
    }
    
    // Método para eliminar un usuario por su ID.
    @Transactional
    public void eliminar(Long id) {
        User actual=obtener(id);
        if(actual.getRol()==Rol.ADMIN && repo.countByRol(Rol.ADMIN)<=1) {
            throw new BussinessException(HttpStatus.CONFLICT,"No se puede eliminar el ultimo ADMIN");
        }
        repo.delete(actual);
        log.info("Cuenta eliminada id={}",id);
    }
}
