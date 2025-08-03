package com.example.demo.principal;

import com.example.demo.model.Autor;
import com.example.demo.model.DatosLibro;
import com.example.demo.model.RespuestaGutendex;
import com.example.demo.service.ConsultaGutendex;
import com.example.demo.service.ConvierteDatos;

import java.util.*;

public class principal {
    private Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();

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
                    System.out.println("Funcionalidad no implementada aún.");
                    break;
                case 5:
                    System.out.println("Funcionalidad no implementada aún.");
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


}
