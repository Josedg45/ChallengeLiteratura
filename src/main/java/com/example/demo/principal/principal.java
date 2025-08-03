package com.example.demo.principal;

import com.example.demo.model.DatosLibro;
import com.example.demo.service.ConsultaGutendex;
import com.example.demo.service.ConvierteDatos;

import java.util.Scanner;

public class principal {
    private Scanner teclado = new Scanner(System.in);
    private final String URL_BASE = "gutendex.com/books/";
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

    private DatosLibro getDatosLibro(){
        System.out.println("Escribe el nombre del libro que deseas buscae");
        var nombreSerie = teclado.nextLine();
        var json = ConsultaGutendex.obtenerDatos(URL_BASE);
        System.out.println(json);
        DatosLibro datos = conversor.obtenerDatos(json, DatosLibro.class);
        return datos;
    }
}
