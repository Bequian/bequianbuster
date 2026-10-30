package com.example.ms_usuarios.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_usuarios.model.Rol;
import com.example.ms_usuarios.model.User;


/**
 * Repositorio de usuarios.
 * Proporciona métodos para acceder y manipular los datos de los usuarios en la base de datos.
 * Contiene métodos para buscar usuarios por nombre de usuario, verificar la existencia de usuarios por nombre de usuario o correo electrónico, y contar usuarios por rol.
 * Proporciona la interfaz para realizar operaciones CRUD sobre la entidad User.
 */
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    long countByRol(Rol rol);
}
