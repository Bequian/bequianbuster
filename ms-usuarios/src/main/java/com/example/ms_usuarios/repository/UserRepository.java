package com.example.ms_usuarios.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.ms_usuarios.model.Rol;
import com.example.ms_usuarios.model.User;


/** JpaRepository: la persistencia de usuarios corresponde a ESTE microservicio. */
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    long countByRol(Rol rol);
}
