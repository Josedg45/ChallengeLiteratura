package com.example.demo.principal;

import com.example.demo.model.Autor;
import com.example.demo.model.DatosLibro;
import com.example.demo.model.RespuestaGutendex;
import com.example.demo.service.ConsultaGutendex;
import com.example.demo.service.ConvierteDatos;
import com.example.demo.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
@Component
public class principal {
    private Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();
    @Autowired
    private LibroService libroService;

    public void muestraElMenu() {
        int opcion = -1;
        while (opcion != 0) {
            System.out.println("\n-----------");
            System.out.println("Elija la opción a través de su número:");
            System.out.println("1- buscar libro por título");
            System.out.println("2- listar libros registrados");
            System.out.println("3- listar autores registrados");
            System.out.println("4- listar autores vivos en un determinado año");
            System.out.println("5- listar libros por idioma");
            System.out.println("0 - salir");
            System.out.print(">> ");
            opcion = teclado.nextInt();
            teclado.nextLine(); // limpiar buffer

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el título a buscar: ");
                    String titulo = teclado.nextLine();
                    buscarPorTitulo(titulo);
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    listarAutoresRegistrados();
                    break;
                case 4:
                    listarAutoresVivosEnAnio();
                    break;
                case 5:
                    buscarPorIdioma();
                    break;
                case 0:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }


        }
    }

    private void listarLibros() {
        String json = ConsultaGutendex.obtenerDatos(URL_BASE);
        RespuestaGutendex respuesta = conversor.obtenerDatos(json, RespuestaGutendex.class);
        List<DatosLibro> libros = respuesta.results();

        for (DatosLibro libro : libros) {
            System.out.println("\n📖 Título: " + libro.title());

            if (libro.authors() != null && !libro.authors().isEmpty()) {
                System.out.print("✍️  Autor(es): ");
                List<String> nombres = libro.authors().stream().map(Autor::name).toList();
                System.out.println(String.join(", ", nombres));
            } else {
                System.out.println("✍️  Autor(es): Desconocido");
            }

            System.out.println("⬇️  Descargas: " + libro.download_count());

            if (libro.languages() != null && !libro.languages().isEmpty()) {
                System.out.println("🌍 Idiomas: " + String.join(", ", libro.languages()));
            } else {
                System.out.println("🌍 Idiomas: No disponible");
            }

            System.out.println("--------------------------------------------------");
        }
    }

    private void buscarPorTitulo(String titulo) {
        String url = URL_BASE + "?search=" + titulo.replace(" ", "%20");
        String json = ConsultaGutendex.obtenerDatos(url);
        RespuestaGutendex respuesta = conversor.obtenerDatos(json, RespuestaGutendex.class);
        List<DatosLibro> libros = respuesta.results();
        for (DatosLibro libro : libros) {
            libroService.guardarLibroDesdeApi(libro);  // << GUARDADO AQUÍ
        }

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros con ese título.");
        } else {
            libros.forEach(libro -> {
                System.out.println("\nTítulo: " + libro.title());

                if (libro.authors() != null && !libro.authors().isEmpty()) {
                    System.out.println("Autor(es):");
                    libro.authors().forEach(autor -> System.out.println("  - " + autor.name()));
                } else {
                    System.out.println("Autor(es): Desconocido");
                }

                System.out.println("Descargas: " + libro.download_count());

                if (libro.languages() != null && !libro.languages().isEmpty()) {
                    System.out.println("Idioma(s): " + String.join(", ", libro.languages()));
                } else {
                    System.out.println("Idioma(s): No disponible");
                }

                System.out.println("--------------------------------------------------");
            });
        }
    }

    private void listarAutoresRegistrados() {
        String url = URL_BASE + "?page=1";
        String json = ConsultaGutendex.obtenerDatos(url);
        RespuestaGutendex respuesta = conversor.obtenerDatos(json, RespuestaGutendex.class);
        List<DatosLibro> libros = respuesta.results();

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros.");
            return;
        }

        Set<String> autoresImpresos = new HashSet<>();

        for (DatosLibro libro : libros) {
            if (libro.authors() != null) {
                for (Autor autor : libro.authors()) {
                    if (autoresImpresos.contains(autor.name())) continue;

                    autoresImpresos.add(autor.name());

                    System.out.println("Autor: " + autor.name());
                    System.out.println("Fecha de nacimiento: " + (autor.birth_year() != null ? autor.birth_year() : "Desconocida"));
                    System.out.println("Fecha de fallecimiento: " + (autor.death_year() != null ? autor.death_year() : "Desconocida"));

                    // Buscar libros escritos por ese autor en esta página
                    List<String> librosAutor = new ArrayList<>();
                    for (DatosLibro l : libros) {
                        if (l.authors() != null) {
                            for (Autor a : l.authors()) {
                                if (a.name().equals(autor.name())) {
                                    librosAutor.add(l.title());
                                    break;
                                }
                            }
                        }
                    }
                    System.out.println("Libros: " + librosAutor);
                    System.out.println();
                }
            }
        }

        if (autoresImpresos.isEmpty()) {
            System.out.println("No se encontraron autores.");
        }
    }

    private void buscarPorIdioma() {
        Map<Integer, String> opcionesIdioma = Map.of(
                1, "en", // Inglés
                2, "es", // Español
                3, "fr", // Francés
                4, "de"  // Alemán
        );

        System.out.println("Seleccione un idioma:");
        System.out.println("1 - Inglés");
        System.out.println("2 - Español");
        System.out.println("3 - Francés");
        System.out.println("4 - Alemán");
        System.out.print(">> ");
        int opcion = teclado.nextInt();
        teclado.nextLine();

        if (!opcionesIdioma.containsKey(opcion)) {
            System.out.println("Opción inválida.");
            return;
        }

        String codigoIdioma = opcionesIdioma.get(opcion);
        String url = URL_BASE + "?languages=" + codigoIdioma;

        String json = ConsultaGutendex.obtenerDatos(url);
        RespuestaGutendex respuesta = conversor.obtenerDatos(json, RespuestaGutendex.class);
        List<DatosLibro> libros = respuesta.results();

        if (libros.isEmpty()) {
            System.out.println("No se encontraron libros en ese idioma.");
            return;
        }

        for (DatosLibro libro : libros) {
            System.out.println("\n📖 Título: " + libro.title());

            if (libro.authors() != null && !libro.authors().isEmpty()) {
                System.out.print("✍️  Autor(es): ");
                List<String> nombres = libro.authors().stream().map(Autor::name).toList();
                System.out.println(String.join(", ", nombres));
            } else {
                System.out.println("✍️  Autor(es): Desconocido");
            }

            System.out.println("⬇️  Descargas: " + libro.download_count());

            if (libro.languages() != null && !libro.languages().isEmpty()) {
                System.out.println("🌍 Idiomas: " + String.join(", ", libro.languages()));
            } else {
                System.out.println("🌍 Idiomas: No disponible");
            }

            System.out.println("--------------------------------------------------");
        }
    }

    private void listarAutoresVivosEnAnio() {
        System.out.print("Ingrese el año: ");
        int anio = teclado.nextInt();
        teclado.nextLine(); // limpiar buffer

        String url = URL_BASE + "?page=1";
        String json = ConsultaGutendex.obtenerDatos(url);
        RespuestaGutendex respuesta = conversor.obtenerDatos(json, RespuestaGutendex.class);
        List<DatosLibro> libros = respuesta.results();

        if (libros == null || libros.isEmpty()) {
            System.out.println("No se encontraron libros.");
            return;
        }

        Set<String> autoresMostrados = new HashSet<>();

        for (DatosLibro libro : libros) {
            if (libro.authors() != null) {
                for (Autor autor : libro.authors()) {
                    Integer nacimiento = autor.birth_year();
                    Integer muerte = autor.death_year();

                    boolean vivoEnAnio = nacimiento != null && nacimiento <= anio &&
                            (muerte == null || muerte >= anio);

                    if (vivoEnAnio && autoresMostrados.add(autor.name())) {
                        System.out.println("Autor: " + autor.name());
                        System.out.println(" - Año de nacimiento: " + nacimiento);
                        System.out.println(" - Año de fallecimiento: " + (muerte != null ? muerte : "Desconocido"));
                        System.out.println();
                    }
                }
            }
        }

        if (autoresMostrados.isEmpty()) {
            System.out.println("No se encontraron autores vivos en el año " + anio + ".");
        }
    }



}