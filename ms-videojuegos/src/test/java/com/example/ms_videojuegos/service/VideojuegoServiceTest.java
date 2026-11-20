package com.example.ms_videojuegos.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.ms_videojuegos.dto.CompraRequest;
import com.example.ms_videojuegos.dto.ItemCompra;
import com.example.ms_videojuegos.dto.VideojuegoRequest;
import com.example.ms_videojuegos.error.ResourceNotFoundException;
import com.example.ms_videojuegos.error.StockInsuficienteException;
import com.example.ms_videojuegos.model.Videojuego;
import com.example.ms_videojuegos.repository.VideojuegoRepository;

/**
 * Pruebas unitarias de la lógica de negocio del catálogo.
 * El repositorio es un mock de Mockito: no se usa Oracle.
 */
@ExtendWith(MockitoExtension.class)
class VideojuegoServiceTest {

    @Mock
    private VideojuegoRepository repositorio;

    private VideojuegoService servicio;

    @BeforeEach
    void preparar() {
        servicio = new VideojuegoService(repositorio);
    }

    // ---------- Búsquedas ----------

    @Test
    @DisplayName("Buscar un ID inexistente lanza ResourceNotFoundException")
    void buscarInexistente() {
        when(repositorio.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicio.buscarPorId(99L))
            .isInstanceOf(ResourceNotFoundException.class)
            .hasMessageContaining("99");
    }

    @Test
    @DisplayName("La búsqueda convierte filtros vacíos en null y recorta espacios")
    void buscarNormalizaFiltros() {
        servicio.buscar("   ", " PC ", "");

        verify(repositorio).buscar(null, "PC", null);
    }

    // ---------- Actualizar y eliminar ----------

    @Test
    @DisplayName("Actualizar conserva el ID y reemplaza los datos")
    void actualizarConservaId() {
        when(repositorio.findById(5L)).thenReturn(Optional.of(videojuego(5L, "Halo", 18)));
        when(repositorio.save(any(Videojuego.class))).thenAnswer(inv -> inv.getArgument(0));

        Videojuego resultado = servicio.actualizar(5L, request("Halo Infinite", "img.jpg"));

        assertThat(resultado.getId()).isEqualTo(5L);
        assertThat(resultado.getTitulo()).isEqualTo("Halo Infinite");
        assertThat(resultado.getImagen()).isEqualTo("img.jpg");
    }

    @Test
    @DisplayName("Actualizar con imagen en blanco la deja en null")
    void actualizarImagenEnBlanco() {
        Videojuego existente = videojuego(5L, "Halo", 18);
        existente.setImagen("covers/halo.jpg");
        when(repositorio.findById(5L)).thenReturn(Optional.of(existente));
        when(repositorio.save(any(Videojuego.class))).thenAnswer(inv -> inv.getArgument(0));

        Videojuego resultado = servicio.actualizar(5L, request("Halo", "   "));

        assertThat(resultado.getImagen()).isNull();
    }

    @Test
    @DisplayName("Eliminar borra exactamente el registro encontrado")
    void eliminarRegistroCorrecto() {
        Videojuego existente = videojuego(3L, "Zelda", 15);
        when(repositorio.findById(3L)).thenReturn(Optional.of(existente));

        servicio.eliminar(3L);

        verify(repositorio).delete(existente);
    }

    @Test
    @DisplayName("Eliminar un ID inexistente no borra nada")
    void eliminarInexistente() {
        when(repositorio.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicio.eliminar(99L))
            .isInstanceOf(ResourceNotFoundException.class);
        verify(repositorio, never()).delete(any());
    }

    // ---------- Compra ----------

    @Test
    @DisplayName("Comprar descuenta el stock de cada producto y guarda los cambios")
    void comprarDescuentaStock() {
        Videojuego witcher = videojuego(1L, "The Witcher 3", 35);
        Videojuego terraria = videojuego(9L, "Terraria", 50);
        when(repositorio.findById(1L)).thenReturn(Optional.of(witcher));
        when(repositorio.findById(9L)).thenReturn(Optional.of(terraria));
        when(repositorio.saveAll(any())).thenAnswer(inv -> inv.getArgument(0));

        servicio.comprar(new CompraRequest(List.of(new ItemCompra(1L, 2), new ItemCompra(9L, 5))));

        assertThat(witcher.getStock()).isEqualTo(33);
        assertThat(terraria.getStock()).isEqualTo(45);
        verify(repositorio).saveAll(List.of(witcher, terraria));
    }

    @Test
    @DisplayName("Comprar más que el stock disponible lanza StockInsuficienteException")
    void comprarSinStock() {
        when(repositorio.findById(1L)).thenReturn(Optional.of(videojuego(1L, "The Witcher 3", 3)));

        assertThatThrownBy(() ->
                servicio.comprar(new CompraRequest(List.of(new ItemCompra(1L, 5)))))
            .isInstanceOf(StockInsuficienteException.class)
            .hasMessageContaining("disponible 3")
            .hasMessageContaining("solicitado 5");
        verify(repositorio, never()).saveAll(any());
    }

    @Test
    @DisplayName("Compra todo o nada: si un producto falla, no se guarda ninguno")
    void comprarTodoONada() {
        when(repositorio.findById(1L)).thenReturn(Optional.of(videojuego(1L, "The Witcher 3", 35)));
        when(repositorio.findById(2L)).thenReturn(Optional.of(videojuego(2L, "Elden Ring", 1)));

        assertThatThrownBy(() -> servicio.comprar(new CompraRequest(List.of(
                new ItemCompra(1L, 1),       // este sí tiene stock
                new ItemCompra(2L, 10)))))   // este no
            .isInstanceOf(StockInsuficienteException.class);

        // No se llegó a guardar nada; en la aplicación real, @Transactional revierte la transacción
        verify(repositorio, never()).saveAll(any());
    }

    // ---------- Datos de prueba ----------

    private static Videojuego videojuego(Long id, String titulo, int stock) {
        Videojuego v = new Videojuego();
        v.setId(id);
        v.setTitulo(titulo);
        v.setPrecio(new BigDecimal("19990"));
        v.setStock(stock);
        return v;
    }

    private static VideojuegoRequest request(String titulo, String imagen) {
        return new VideojuegoRequest(titulo, "Desarrollador", LocalDate.of(2021, 12, 8),
            "PC", "Shooter", "Descripción de prueba", new BigDecimal("29990"), 10, imagen);
    }
}