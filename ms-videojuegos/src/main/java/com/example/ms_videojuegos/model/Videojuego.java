package com.example.ms_videojuegos.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;


@Entity
@Table(name = "videojuego")
public class Videojuego {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name = "ID")
    private Long id;

    @Column (
        name = "TITULO",
        nullable = false,
        length = 100
    )
    private String titulo;

    @Column (
        name = "DESARROLLADOR",
        nullable = false,
        length = 100
    )
    private String desarrollador;

    @Column (
        name = "FECHA_LANZAMIENTO",
        nullable = false,
        length = 10
    )
    private String fechaLanzamiento;

    @Column (
        name = "PLATAFORMA",
        nullable = false,
        length = 50
    )
    private String plataforma;

    @Column (
        name = "GENERO",
        nullable = false,
        length = 50
    )
    private String genero;

    @Column (
        name = "DESCRIPCION",
        nullable = false,
        length = 500
    )
    private String descripcion;

    @Column (
        name = "PRECIO",
        nullable = false,
        precision = 12,
        scale = 2
    )
    private BigDecimal precio;

    @Column (
        name = "STOCK",
        nullable = false
    )
    private Integer stock;

    @Column (
        name = "IMAGEN",
        length = 255
    )
    private String imagen;


    public Videojuego() {
    }   

    public Videojuego(
            Long id, 
            String titulo, 
            String desarrollador, 
            String fechaLanzamiento, 
            String plataforma, 
            String genero, 
            String descripcion, 
            BigDecimal precio, 
            Integer stock, 
            String imagen) {

        this.id = id;
        this.titulo = titulo;
        this.desarrollador = desarrollador;
        this.fechaLanzamiento = fechaLanzamiento;
        this.plataforma = plataforma;
        this.genero = genero;
        this.descripcion = descripcion;
        this.precio = precio;
        this.stock = stock;
        this.imagen = imagen;
    }

    // Getters

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDesarrollador() {
        return desarrollador;
    }

    public String getFechaLanzamiento() {
        return fechaLanzamiento;
    }

    public String getPlataforma() {
        return plataforma;
    }

    public String getGenero() {
        return genero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public BigDecimal getPrecio() {
        return precio;
    }

    public Integer getStock() {
        return stock;
    }

    public String getImagen() {
        return imagen;
    }

    // Setters

    public void setId(Long id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDesarrollador(String desarrollador) {
        this.desarrollador = desarrollador;
    }

    public void setFechaLanzamiento(String fechaLanzamiento) {
        this.fechaLanzamiento = fechaLanzamiento;
    }

    public void setPlataforma(String plataforma) {
        this.plataforma = plataforma;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

}
