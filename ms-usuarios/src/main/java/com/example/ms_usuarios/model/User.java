package com.example.ms_usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * Representa la entidad de usuario en la base de datos.
 * Contiene información sobre el nombre de usuario, correo electrónico, hash de contraseña y rol del usuario.
 * Contiene los métodos getter y setter para acceder y modificar los atributos del usuario.
 * Esta clase se mapea a la tabla "bb_usuarios" en la base de datos.
 * User representa un usuario en el sistema con sus atributos y rol correspondiente.
 */
@Entity 
@Table(name="bb_usuarios")
public class User {

    /** Identificador único del usuario. */
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="ID")
    private Long id;

    /** Nombre de usuario único del usuario. */
    @Column (name="USERNAME", nullable = false, length = 50, unique = true)
    private String username;

    /** Correo electrónico único del usuario. */
    @Column (name="EMAIL", nullable = false, length = 120, unique = true)
    private String email;

    /** Hash de la contraseña del usuario. */
    @Column (name="PASS_HASH", nullable = false, length = 100)
    private String passHash;

    /** Rol del usuario en el sistema. */
    @Enumerated(EnumType.STRING)
    @Column (name="ROL", nullable = false, length = 120)
    private Rol rol;

    /** Constructor por defecto. */
    public User() {

    }

    /** Constructor con todos los atributos del usuario. */
    public User(
        Long id, String username, String email, String passHash, Rol rol
    ){
        this.id = id;
        this.username = username;
        this.email = email;
        this.passHash = passHash;
        this.rol = rol;
    }

     /** Getters de los atributos del usuario. */
    public Long getId(){return id;}
    public String getUsername(){return username;}
    public String getEmail(){return email;}
    public String getPassHash(){return passHash;}
    public Rol getRol(){return rol;}

    /** Setters de los atributos del usuario. */
    public void setId(Long id){this.id = id;}
    public void setUsername(String username){this.username = username;}
    public void setEmail(String email){this.email = email;}
    public void setPassHash(String passHash){this.passHash = passHash;}
    public void setRol(Rol rol){this.rol = rol;}

}
