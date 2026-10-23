package com.example.bequianbuster.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.bequianbuster.model.Videojuego;

/**
 * findAll() - Devuelve todos los videojuegos de la base de datos.
 * findById(Long id) - Devuelve un videojuego por su ID.
 * save(Videojuego videojuego) - Guarda un nuevo videojuego en la base de datos.
 * deleteById(Long id) - Elimina un videojuego por su ID.
 * existsById(Long id) - Verifica si un videojuego existe por su ID.
 *
*/

public interface VideojuegoRepository 
    extends JpaRepository<Videojuego, Long> {

}
