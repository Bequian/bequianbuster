package cl.bequianbuster.catalogo.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.bequianbuster.catalogo.dto.VideojuegoRequest;
import cl.bequianbuster.catalogo.error.ResourceNotFoundException;
import cl.bequianbuster.catalogo.model.Videojuego;
import cl.bequianbuster.catalogo.orden.SelectorOrden;
import cl.bequianbuster.catalogo.repository.VideojuegoRepository;

/** Lógica de negocio del catálogo de videojuegos. */
@Service
public class VideojuegoService {

    private static final Logger log = LoggerFactory.getLogger(VideojuegoService.class);

    private final VideojuegoRepository repositorio;
    private final SelectorOrden selectorOrden;

    public VideojuegoService(VideojuegoRepository repositorio, SelectorOrden selectorOrden) {
        this.repositorio = repositorio;
        this.selectorOrden = selectorOrden;
    }

    /** Lista el catálogo. Si se indica un orden, delega en la estrategia correspondiente. */
    @Transactional(readOnly = true)
    public List<Videojuego> listar(String orden) {
        List<Videojuego> videojuegos = repositorio.findAll();
        if (orden == null || orden.isBlank()) {
            return videojuegos;
        }
        return selectorOrden.obtener(orden).ordenar(videojuegos);
    }

    @Transactional(readOnly = true)
    public Videojuego buscarPorId(Long id) {
        return repositorio.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Videojuego no encontrado con id: " + id));
    }

    @Transactional
    public Videojuego crear(VideojuegoRequest datos) {
        Videojuego nuevo = new Videojuego();
        aplicarDatos(nuevo, datos);
        Videojuego guardado = repositorio.save(nuevo);
        log.info("Videojuego creado con id: {}", guardado.getId());
        return guardado;
    }

    @Transactional
    public Videojuego actualizar(Long id, VideojuegoRequest datos) {
        Videojuego actual = buscarPorId(id);
        aplicarDatos(actual, datos);
        return repositorio.save(actual);
    }

    @Transactional
    public void eliminar(Long id) {
        Videojuego actual = buscarPorId(id);
        repositorio.delete(actual);
        log.info("Videojuego eliminado con id: {}", id);
    }

    /** Copia los datos del DTO a la entidad (usado al crear y al actualizar). */
    private void aplicarDatos(Videojuego destino, VideojuegoRequest datos) {
        destino.setTitulo(datos.titulo().trim());
        destino.setDesarrollador(datos.desarrollador().trim());
        destino.setFechaLanzamiento(datos.fechaLanzamiento());
        destino.setPlataforma(datos.plataforma());
        destino.setGenero(datos.genero().trim());
        destino.setDescripcion(datos.descripcion().trim());
        destino.setPrecio(datos.precio());
        destino.setStock(datos.stock());
        destino.setImagen(datos.imagen() == null || datos.imagen().isBlank() ? null : datos.imagen().trim());
    }
}