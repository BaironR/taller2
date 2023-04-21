/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech;

import cl.ucn.disc.pa.bibliotech.services.Sistema;
import edu.princeton.cs.stdlib.StdIn;
import edu.princeton.cs.stdlib.StdOut;

import java.io.IOException;
import java.util.Objects;

/**
 * The Main.
 *
 * @author Programacion Avanzada.
 */
public final class Main {

    /**
     * The main.
     *
     * @param args to use.
     * @throws IOException en caso de un error.
     */
    public static void main(final String[] args) throws IOException {

        // inicializacion del sistema.
        Sistema sistema = new Sistema();

        StdOut.println(sistema.obtegerCatalogoLibros());

        String opcion = null;
        while (!Objects.equals(opcion, "2")) {

            StdOut.println("""
                    [*] Bienvenido a BiblioTech [*]
                                    
                    [1] Iniciar Sesion
                    [2] Salir
                    """);
            StdOut.print("Escoja una opcion: ");
            opcion = StdIn.readLine();

            switch (opcion) {
                case "1" -> iniciarSesion(sistema);
                case "2" -> StdOut.println("¡Hasta Pronto!");
                default -> StdOut.println("Opcion no valida, intente nuevamente");
            }
        }
    }

    /**
     * Inicia la sesion del Socio en el Sistema.
     *
     * @param sistema a utilizar.
     */
    private static void iniciarSesion(final Sistema sistema) {
        StdOut.println("[*] Iniciar sesion en BiblioTech [*]");
        int numeroSocio;

        do {
            try {
                StdOut.println("Ingrese su numero de socio: ");
                String numeroSocioString = StdIn.readLine();
                numeroSocio = Integer.parseInt(numeroSocioString);
                break;

            } catch (Exception e) {

                StdOut.println("El numero de socio no es valido!");
            }
        } while (true);

        StdOut.print("Ingrese su contrasenia: ");
        String contrasenia = StdIn.readLine();

        // intento el inicio de session
        try {
            sistema.iniciarSession(numeroSocio, contrasenia);
        } catch (IllegalArgumentException ex) {
            StdOut.println("Ocurrio un error: " + ex.getMessage());
            return;
        }

        // mostrar menu principal
        menuPrincipal(sistema);
    }

    private static void menuPrincipal(final Sistema sistema) {
        String opcion = null;
        while (!Objects.equals(opcion, "4")) {
            StdOut.println("""
                    [*] BiblioTech [*]
                                        
                    [1] Prestamo de un libro
                    [2] Editar información
                    [3] Calificar libro
                                        
                    [4] Cerrar sesion
                    """);

            StdOut.print("Escoja una opcion: ");
            opcion = StdIn.readLine();

            switch (opcion) {
                case "1" -> menuPrestamo(sistema);
                case "2" -> editarInformacion(sistema);
                //TODO: Crear metodo de calificar libro.
                case "3" -> calificacionLibro(sistema);
                case "4" -> sistema.cerrarSession();
                default -> StdOut.println("Opcion no valida, intente nuevamente");
            }
        }
    }

    private static void menuPrestamo(Sistema sistema) {
        StdOut.println("[*] Préstamo de un Libro [*]");
        StdOut.println(sistema.obtegerCatalogoLibros());

        StdOut.print("Ingrese el ISBN del libro a tomar prestado: ");
        String isbn = StdIn.readLine();

        try {
            sistema.realizarPrestamoLibro(isbn);
        } catch (IllegalArgumentException | IOException ex) {
            StdOut.println("Ocurrio un error, intente nuevamente: " + ex.getMessage());
        }
    }

    private static void editarInformacion(Sistema sistema) {

        String opcion = null;
        while (!Objects.equals(opcion, "3")) {

            StdOut.println("[*] Editar Perfil [*]");
            StdOut.println(sistema.obtenerDatosSocioLogeado());
            StdOut.println("""               
                    [1] Editar correo Electronico
                    [2] Editar Contraseña
                                        
                    [3] Volver atrás
                    """);
            StdOut.print("Escoja una opción: ");
            opcion = StdIn.readLine();

            switch (opcion) {
                case "1" -> editarCorreo(sistema);
                case "2" -> cambiarContrasenia(sistema);
                case "3" -> StdOut.println("Volviendo al menú anterior...");
                default -> StdOut.println("Opcion no valida, intente nuevamente");
            }
        }
    }

    private static void calificacionLibro(Sistema sistema) {

        StdOut.println("[*] Calificar libro [*]");
        StdOut.println(sistema.obtegerCatalogoLibros());
        StdOut.println("Ingrese el ISBN del libro que desea calificar");
        String isbn = StdIn.readLine();

        try {

            sistema.calificarLibro(isbn);

        } catch (IllegalArgumentException | IOException ex) {
            StdOut.println("Ocurrio un error, intente nuevamente: " + ex.getMessage());
        }
    }

    private static void cambiarContrasenia(Sistema sistema) {
        // TODO: implementar este metodo
        StdOut.println("[*] Editar Contrasenia [*]");
        StdOut.println("Ingrese su contrasenia anterior");
        String contrasenia = StdIn.readLine();

        try {
            sistema.cambiarContrasenia(contrasenia);

        } catch (IllegalArgumentException | IOException ex) {
            StdOut.println("Ocurrio un error, intente nuevamente: " + ex.getMessage());
        }
    }

    private static void editarCorreo(Sistema sistema) {
        // TODO: implementar este metodo
        StdOut.println("[*] Editar Correo Electrónico [*]");
        StdOut.println("Ingrese su correo electrónico nuevo");
        String email = StdIn.readLine();
        sistema.cambiarCorreo(email);
    }
}
