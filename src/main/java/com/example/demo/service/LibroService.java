package com.example.demo.service;

import com.example.demo.entity.AutorEntidad;
import com.example.demo.entity.Libro;
import com.example.demo.model.DatosLibro;
import com.example.demo.repository.AutorRepository;
import com.example.demo.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;

    public void guardarLibroDesdeApi(DatosLibro datosLibro) {
        if (datosLibro.authors() == null || datosLibro.authors().isEmpty()) return;

        // Tomamos el primer autor como principal
        com.example.demo.model.Autor autorApi = datosLibro.authors().get(0);
        AutorEntidad autorEntidad = autorRepository.findByNombre(autorApi.name())
                .orElseGet(() -> {
                    AutorEntidad nuevoAutor = new AutorEntidad(
                            autorApi.name(),
                            autorApi.birth_year(),
                            autorApi.death_year()
                    );
                    return autorRepository.save(nuevoAutor);
                });

        Libro libro = new Libro();
        libro.setTitulo(datosLibro.title());
        libro.setAutor(autorEntidad);  // relación @ManyToOne
        libro.setDescargas(datosLibro.download_count());
        libro.setLenguaje(datosLibro.languages().isEmpty() ? "Desconocido" : datosLibro.languages().get(0));

        // Solo guarda nombres de autores como texto (no entidades)
        List<String> nombresAutores = datosLibro.authors().stream()
                .map(com.example.demo.model.Autor::name)
                .toList();
        libro.setAutores(nombresAutores);

        libroRepository.save(libro);
    }
}
