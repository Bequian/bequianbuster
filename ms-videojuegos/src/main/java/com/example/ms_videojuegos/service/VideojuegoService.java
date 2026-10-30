package com.example.ms_videojuegos.service;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ms_videojuegos.dto.VideojuegoRequest;

import com.example.ms_videojuegos.error.ResourceNotFoundException;

import com.example.ms_videojuegos.model.Videojuego;
import com.example.ms_videojuegos.repository.VideojuegoRepository;

/**
 * Servicio para gestionar videojuegos.
 * Proporciona métodos para listar, buscar, crear, actualizar y eliminar videojuegos.
 */
@Service
public class VideojuegoService {

    /**
     * Logger para la clase VideojuegoService.
     */
    private static final Logger log = LoggerFactory.getLogger(VideojuegoService.class);

    /**
     * Repositorio de videojuegos.
     */
    private final VideojuegoRepository videojuegoRepository;

    /**
     * Constructor de la clase VideojuegoService.
     * @param videojuegoRepository Repositorio de videojuegos.
     */
    public VideojuegoService(VideojuegoRepository videojuegoRepository) {
        this.videojuegoRepository = videojuegoRepository;
    }
    
    /**
     * Lista todos los videojuegos.
     * @return Lista de videojuegos.
     */
    @Transactional(readOnly = true)
    public List<Videojuego> listar() {
        return videojuegoRepository.findAll();
    }

    /**
     * Busca un videojuego por su ID.
     * @param id ID del videojuego.
     * @return Videojuego encontrado.
     * @throws ResourceNotFoundException Si no se encuentra el videojuego.
     */
    @Transactional(readOnly = true)
    public Videojuego buscarPorId(Long id) {
        return videojuegoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Videojuego no encontrado con id: " + id));
    }

    /**
     * Crea un nuevo videojuego.
     * @param datos Datos del videojuego a crear.
     * @return Videojuego creado.
     */
    @Transactional
    public Videojuego crear(VideojuegoRequest datos) {
        Videojuego nuevo = new Videojuego(
            null,
            datos.titulo(),
            datos.desarrollador(),
            datos.fechaLanzamiento(),
            datos.plataforma(),
            datos.genero(),
            datos.descripcion(),
            datos.precio(),
            datos.stock(),
            datos.imagen()
        );
        
        Videojuego saved = videojuegoRepository.save(nuevo);
        return saved;
    }

    /**
     * Actualiza un videojuego existente.
     * @param id ID del videojuego a actualizar.
     * @param datos Nuevos datos del videojuego.
     * @return Videojuego actualizado.
     * @throws ResourceNotFoundException Si no se encuentra el videojuego.
     */
    @Transactional
    public Videojuego actualizar(Long id, VideojuegoRequest datos) {
        Videojuego actual = buscarPorId(id);

        actual.setTitulo(datos.titulo());
        actual.setDesarrollador(datos.desarrollador());
        actual.setFechaLanzamiento(datos.fechaLanzamiento());
        actual.setPlataforma(datos.plataforma());
        actual.setGenero(datos.genero());
        actual.setDescripcion(datos.descripcion());
        actual.setPrecio(datos.precio());
        actual.setStock(datos.stock());
        if(datos.imagen() != null) {
            actual.setImagen(datos.imagen().isBlank() ? null : datos.imagen());
        }

        Videojuego updated = videojuegoRepository.save(actual);
        return updated;
    }

    /**
     * Elimina un videojuego por su ID.
     * @param id ID del videojuego a eliminar.
     * @throws ResourceNotFoundException Si no se encuentra el videojuego.
     */
    @Transactional
    public void eliminar(Long id) {
        Videojuego actual = buscarPorId(id);
        videojuegoRepository.delete(actual);
        log.info("Videojuego eliminado con id: {}", id);
    }
    
}
