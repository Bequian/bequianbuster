package com.example.ms_videojuegos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.ms_videojuegos.model.Videojuego;

public interface VideojuegoRepository extends JpaRepository<Videojuego, Long> {

    /**
     * Búsqueda con filtros opcionales: si un parámetro es null, no se aplica.
     * El título busca coincidencias parciales sin distinguir mayúsculas.
     */
    @Query("""
        SELECT v FROM Videojuego v
        WHERE (:titulo IS NULL OR LOWER(v.titulo) LIKE LOWER(CONCAT('%', :titulo, '%')))
          AND (:plataforma IS NULL OR v.plataforma = :plataforma)
          AND (:genero IS NULL OR LOWER(v.genero) = LOWER(:genero))
        ORDER BY v.titulo
        """)
    List<Videojuego> buscar(
        @Param("titulo") String titulo,
        @Param("plataforma") String plataforma,
        @Param("genero") String genero
    );
}