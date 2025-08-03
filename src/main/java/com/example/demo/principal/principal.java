package com.example.demo.principal;

import com.example.demo.model.Autor;
import com.example.demo.model.DatosLibro;
import com.example.demo.model.RespuestaGutendex;
import com.example.demo.service.ConsultaGutendex;
import com.example.demo.service.ConvierteDatos;

import java.util.List;
import java.util.Scanner;

public class principal {
    private Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "https://gutendex.com/books/";
    private ConvierteDatos conversor = new ConvierteDatos();


    public void muestraElMenu() {
        var opcion = -1;
        while (opcion != 0) {
            var menu = """
                    1 - Buscar series 
                    2 - Buscar episodios
                                        
                    0 - Salir
                    """;
            System.out.println(menu);
            opcion = teclado.nextInt();
            teclado.nextLine();

            switch (opcion) {
                case 1:
                    getDatosLibro();
                    break;
                case 2:

                    break;
                case 0:
                    System.out.println("Cerrando la aplicación...");
                    break;
                default:
                    System.out.println("Opción inválida");
            }


        }
    }

    private void getDatosLibro() {
        System.out.println("Los libros que hay son:");
        var json = ConsultaGutendex.obtenerDatos(URL_BASE);

        RespuestaGutendex respuesta = conversor.obtenerDatos(json, RespuestaGutendex.class);
        List<DatosLibro> libros = respuesta.results();

        for (DatosLibro libro : libros) {
            System.out.println("Título: " + libro.title());

            if (libro.authors() != null && !libro.authors().isEmpty()) {
                System.out.println("Autor(es):");
                for (Autor autor : libro.authors()) {
                    System.out.println("   - " + autor.name() + " (" + autor.birth_year() + " - " + autor.death_year() + ")");
                }
            } else {
                System.out.println("Autor(es): Desconocido");
            }

            if (libro.summaries() != null && !libro.summaries().isEmpty()) {
                System.out.println("Resumen: " + libro.summaries().get(0));
            } else {
                System.out.println("Resumen: No disponible");
            }

            System.out.println("--------------------------------------------------");
        }
    }


}
