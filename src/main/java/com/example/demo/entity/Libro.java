package com.example.demo.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private AutorEntidad autor;

    private String titulo;

    @ElementCollection
    private List<String> autores;

    private Integer descargas;

    private String lenguaje;

    // Getters y setters
    public Long getId() { return id; }

    public AutorEntidad getAutor() { return autor; }
    public void setAutor(AutorEntidad autor) { this.autor = autor; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public List<String> getAutores() { return autores; }
    public void setAutores(List<String> autores) { this.autores = autores; }

    public Integer getDescargas() { return descargas; }
    public void setDescargas(Integer descargas) { this.descargas = descargas; }

    public String getLenguaje() { return lenguaje; }
    public void setLenguaje(String lenguaje) { this.lenguaje = lenguaje; }
}
