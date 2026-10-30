package com.example.ms_usuarios.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/** Semana 2: Entidad para cuentas persistentes */

@Entity 
@Table(name="bb_usuarios")
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column (name="ID")
    private Long id;

    @Column (name="USERNAME", nullable = false, length = 50, unique = true)
    private String username;

    @Column (name="EMAIL", nullable = false, length = 120, unique = true)
    private String email;

    @Column (name="PASS_HASH", nullable = false, length = 100)
    private String passHash;

    @Enumerated(EnumType.STRING)
    @Column (name="ROL", nullable = false, length = 120)
    private Rol rol;

    public User() {

    }

    public User(
        Long id, String username, String email, String passHash, Rol rol
    ){
        this.id = id;
        this.username = username;
        this.email = email;
        this.passHash = passHash;
        this.rol = rol;
    }

    public Long getId(){return id;}
    public String getUsername(){return username;}
    public String getEmail(){return email;}
    public String getPassHash(){return passHash;}
    public Rol getRol(){return rol;}
    
    public void setId(Long id){this.id = id;}
    public void setUsername(String username){this.username = username;}
    public void setEmail(String email){this.email = email;}
    public void setPassHash(String passHash){this.passHash = passHash;}
    public void setRol(Rol rol){this.rol = rol;}
    
}
