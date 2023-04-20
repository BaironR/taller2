/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.services;

import cl.ucn.disc.pa.bibliotech.model.Libro;
import cl.ucn.disc.pa.bibliotech.model.Socio;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import edu.princeton.cs.stdlib.StdIn;
import edu.princeton.cs.stdlib.StdOut;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The Sistema.
 *
 * @author Programacion Avanzada.
 */
public final class Sistema {

    /**
     * Procesador de JSON.
     */
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    /**
     * The list of Socios.
     */
    private Socio[] socios;

    /**
     * The list of Libros.
     */
    private Libro[] libros;

    /**
     * Socio en el sistema.
     */
    private Socio socio;

    /**
     * Contador de libros que ya no están disponibles
     */
    private int contLibrosNoDisponibles = 0;

    /**
     * @return contador de libros que ya no están disponibles
     */
    public int getContLibrosNoDisponibles() {
        return contLibrosNoDisponibles;
    }

    /**
     * @param contLibrosNoDisponibles
     */
    public void setContLibrosNoDisponibles(int contLibrosNoDisponibles) {
        this.contLibrosNoDisponibles = contLibrosNoDisponibles;
    }

    /**
     * The Sistema.
     */
    public Sistema() throws IOException {

        // no hay socio logeado.
        this.socios = new Socio[0];
        this.libros = new Libro[0];
        this.socio = null;

        // carga de los socios y libros.
        try {
            this.cargarInformacion();
        } catch (FileNotFoundException ex) {
            // no se encontraron datos, se agregar los por defecto.

            // creo un socio
            this.socios = Utils.append(this.socios, new Socio("John", "Doe", "john.doe@ucn.cl", 1, "john123"));

            // creo un libro y lo agrego al arreglo de libros.
            this.libros = Utils.append(this.libros, new Libro("1491910771", "Head First Java: A Brain-Friendly Guide", " Kathy Sierra", "Programming Languages"));

            // creo otro libro y lo agrego al arreglo de libros.
            this.libros = Utils.append(this.libros, new Libro("1491910771", "Effective Java", "Joshua Bloch", "Programming Languages"));

        } finally {
            // guardo la informacion.
            this.guardarInformacion();
        }

    }

    /**
     * Activa (inicia sesion) de un socio en el sistema.
     *
     * @param numeroDeSocio a utilizar.
     * @param contrasenia   a validar.
     */
    public void iniciarSession(final int numeroDeSocio, final String contrasenia) {

        // El número de socio siempre es positivo.
        if (numeroDeSocio <= 0) {
            throw new IllegalArgumentException("El numero de socio no es valido!");
        }

        // TODO: buscar el socio dado su numero.

        int pos = -1;
        for (int i = 0; i < socios.length; i++) {

            if (numeroDeSocio == this.socios[i].getNumeroDeSocio()) {

                pos = i;
            }
        }

        if (pos == -1) {

            throw new IllegalArgumentException("El numero de socio no es valido!");
        }

        // TODO: verificar su clave.

        if (!this.socios[pos].getContrasenia().equals(contrasenia)) {

            throw new IllegalArgumentException("La contrasenia no es valida!");
        }

        // TODO: asignar al atributo socio el socio encontrado.

        this.socio = this.socios[pos];
    }

    /**
     * Cierra la session del Socio.
     */
    public void cerrarSession() {

        this.socio = null;
    }

    /**
     * Metodo que mueve un libro de los disponibles y lo ingresa a un Socio.
     *
     * @param isbn del libro a prestar.
     */
    public void realizarPrestamoLibro(final String isbn) throws IOException {
        // el socio debe estar activo.
        if (this.socio == null) {
            throw new IllegalArgumentException("Socio no se ha logeado!");
        }

        // busco el libro.
        Libro libro = this.buscarLibro(isbn);

        // si no lo encontre, lo informo.
        if (libro == null) {
            throw new IllegalArgumentException("Libro con isbn " + isbn + " no existe o no se encuentra disponible.");
        }

        // agrego el libro al socio.
        this.socio.agregarLibro(libro);

        // TODO: eliminar el libro de los disponibles

        int cont = this.libros.length;
        int contEliminados = this.getContLibrosNoDisponibles();

        for (int i = 0; i < cont; i++) {

            if (this.libros[i].getIsbn().equals(isbn)) {

                for (int k = i; k < (cont - 1); k++) {

                    this.libros[k] = this.libros[k + 1];
                }
                this.libros[(cont - 1) - contEliminados] = null;
                contEliminados++;
                this.setContLibrosNoDisponibles(contEliminados);
                break;
            }
        }

        // se actualiza la informacion de los archivos
        this.guardarInformacion();
    }

    /**
     * Obtiene un String que representa el listado completo de libros disponibles.
     *
     * @return the String con la informacion de los libros disponibles.
     */
    public String obtegerCatalogoLibros() {

        StringBuilder sb = new StringBuilder();
        for (Libro libro : this.libros) {

            if (libro == null) {
                break;
            }

            sb.append("Titulo    : ").append(libro.getTitulo()).append("\n");
            sb.append("Autor     : ").append(libro.getAutor()).append("\n");
            sb.append("ISBN      : ").append(libro.getIsbn()).append("\n");
            sb.append("Categoria : ").append(libro.getCategoria()).append("\n");
            sb.append("\n");
        }

        return sb.toString();
    }

    /**
     * Metodo que busca un libro en los libros disponibles.
     *
     * @param isbn a buscar.
     * @return el libro o null si no fue encontrado
     */
    private Libro buscarLibro(final String isbn) {
        // recorro el arreglo de libros.
        for (Libro libro : this.libros) {

            if (libro == null) {
                break;
            }
            // si lo encontre, retorno el libro.
            if (libro.getIsbn().equals(isbn)) {
                return libro;
            }
        }
        // no lo encontre, retorno null.
        return null;
    }

    /**
     * Lee los archivos libros.json y socios.json.
     *
     * @throws FileNotFoundException si alguno de los archivos no se encuentra.
     */
    private void cargarInformacion() throws FileNotFoundException {

        // trato de leer los socios y los libros desde el archivo.
        this.socios = GSON.fromJson(new FileReader("socios.json"), Socio[].class);
        this.libros = GSON.fromJson(new FileReader("libros.json"), Libro[].class);
    }

    /**
     * Guarda los arreglos libros y socios en los archivos libros.json y socios.json.
     *
     * @throws IOException en caso de algun error.
     */
    private void guardarInformacion() throws IOException {

        // guardo los socios.
        try (FileWriter writer = new FileWriter("socios.json")) {
            GSON.toJson(this.socios, writer);
        }

        // guardo los libros.
        try (FileWriter writer = new FileWriter("libros.json")) {
            GSON.toJson(this.libros, writer);
        }
    }

    /**
     * @return
     */
    public String obtenerDatosSocioLogeado() {
        if (this.socio == null) {
            throw new IllegalArgumentException("No hay un Socio logeado");
        }

        return "Nombre: " + this.socio.getNombreCompleto() + "\n"
                + "Correo Electronico: " + this.socio.getCorreoElectronico();
    }

    /**
     * Cambia la contrasenia asociada al Socio
     *
     * @param contrasenia
     */
    public void cambiarContrasenia(String contrasenia) throws IOException {

        if (!this.socio.getContrasenia().equals(contrasenia)) {

            throw new IllegalArgumentException("La contrasenia es invalida");

        } else {

            StdOut.println("Ingrese la contrasenia nueva");
            String contraseniaNueva = StdIn.readLine();
            this.socio.setContrasenia(contraseniaNueva);
        }
    }

    /**
     * Cambia el email asociado al Socio
     *
     * @param email
     */
    public void cambiarCorreo(String email) {

        this.socio.setCorreoElectronico(email);
    }

    public void calificarLibro(String isbn) throws IOException {

        int cont = this.libros.length;
        int pos = -1;

        for (int i = 0; i < cont; i++) {

            if (this.libros[i] == null) {

                throw new IllegalArgumentException("El libro que desea calificar no existe o no está disponible");

            }
            if (this.libros[i].getIsbn().equals(isbn)) {

                pos = i;
                break;
            }
        }

        if (pos == -1) {
            throw new IllegalArgumentException("El libro que desea calificar no existe o no está disponible");
        }

        double calificacion;

        do {
            try {

                StdOut.println("Ingrese su calificación del libro: " + this.libros[pos].getTitulo());
                String calificacionString = StdIn.readLine();
                calificacion = Double.parseDouble(calificacionString);

                if (calificacion > 5 || calificacion < 0) {
                    throw new Exception();
                }
                break;

            } catch (Exception e) {
                StdOut.println("Ingrese una calificacion numerica o dentro del rango (0-5)");
            }
        } while (true);

        int contCalificaciones = this.libros[pos].getContadorCalificaciones();
        contCalificaciones++;
        this.libros[pos].setContador(contCalificaciones);
        this.libros[pos].setCalificacion(calificacion, this.libros[pos].getContadorCalificaciones());
        StdOut.println("La calificación actual del libro es: " + this.libros[pos].getCalificacion());

    }
}
