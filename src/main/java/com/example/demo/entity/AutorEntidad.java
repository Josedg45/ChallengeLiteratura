package com.example.demo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "autores")
public class AutorEntidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private Integer anioNacimiento;
    private Integer anioMuerte;

    public AutorEntidad() {}

    public AutorEntidad(String nombre, Integer anioNacimiento, Integer anioMuerte) {
        this.nombre = nombre;
        this.anioNacimiento = anioNacimiento;
        this.anioMuerte = anioMuerte;
    }

    // Getters y setters
    public Long getId() { return id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getAnioNacimiento() { return anioNacimiento; }
    public void setAnioNacimiento(Integer anioNacimiento) { this.anioNacimiento = anioNacimiento; }

    public Integer getAnioMuerte() { return anioMuerte; }
    public void setAnioMuerte(Integer anioMuerte) { this.anioMuerte = anioMuerte; }
}
