package com.example.bequianbuster.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import com.example.bequianbuster.model.Videojuego;
import com.example.bequianbuster.repository.VideojuegoRepository;

@Service
public class VideojuegoService {

    private final VideojuegoRepository videojuegoRepository;

    public VideojuegoService(VideojuegoRepository videojuegoRepository) {
        this.videojuegoRepository = videojuegoRepository;
    }

    public List<Videojuego> listar() {
        return videojuegoRepository.findAll();
    }

    public Optional<Videojuego> buscarPorId(Long id) {
        return videojuegoRepository.findById(id);
    }

    public Videojuego crear(Videojuego datos) {
        
        Videojuego nuevo = new Videojuego(
            null,
            datos.getTitulo(),
            datos.getDesarrollador(),
            datos.getFechaLanzamiento(),
            datos.getPlataforma(),
            datos.getGenero(),
            datos.getDescripcion(),
            datos.getPrecio(),
            datos.getStock(),
            datos.getImagen()
        );
        return videojuegoRepository.save(nuevo);
    }

    public Optional<Videojuego> actualizar(Long id, Videojuego datos) {

        Optional<Videojuego> existente = videojuegoRepository.findById(id);
        if(existente.isEmpty()) {
            return Optional.empty();
        }

        Videojuego actual = existente.get();

        actual.setTitulo(datos.getTitulo());
        actual.setDesarrollador(datos.getDesarrollador());
        actual.setFechaLanzamiento(datos.getFechaLanzamiento());
        actual.setPlataforma(datos.getPlataforma());
        actual.setGenero(datos.getGenero());
        actual.setDescripcion(datos.getDescripcion());
        actual.setPrecio(datos.getPrecio());
        actual.setStock(datos.getStock());
        actual.setImagen(datos.getImagen());

        

        Videojuego guardado = videojuegoRepository.save(actual);
        return Optional.of(guardado);

    }

    public boolean eliminar(Long id) {
        if(!videojuegoRepository.existsById(id)) {
            return false;
        }
        videojuegoRepository.deleteById(id);
        return true;
    }

}
