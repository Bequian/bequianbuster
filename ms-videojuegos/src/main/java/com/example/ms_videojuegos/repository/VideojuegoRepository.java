package com.example.ms_videojuegos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ms_videojuegos.model.Videojuego;

/**
 * Repositorio JPA para la entidad Videojuego.
 * Proporciona métodos CRUD y consultas personalizadas para gestionar videojuegos en la base de datos.
 * Permite realizar operaciones como buscar todos los videojuegos, buscar por ID, guardar, actualizar y eliminar videojuegos.
 * Contiene los métodos heredados de JpaRepository como 
 * findAll()        -> Devuelve todos los videojuegos de la base de datos, 
 * findById()       -> Devuelve un videojuego por su ID, 
 * save()           -> Guarda un nuevo videojuego o actualiza uno existente, 
 * deleteById()     -> Elimina un videojuego por su ID, 
 * existsById()     -> Verifica si un videojuego existe por su ID. 
*/

public interface VideojuegoRepository 
    extends JpaRepository<Videojuego, Long> {

}
