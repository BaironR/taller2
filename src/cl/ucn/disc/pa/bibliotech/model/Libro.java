/*
 * Copyright (c) 2023. Programacion Avanzada, DISC, UCN.
 */

package cl.ucn.disc.pa.bibliotech.model;

/**
 * Clase que representa un Libro.
 *
 * @author Programacion Avanzada.
 */
public final class Libro {

    /**
     * The ISBN.
     */
    private String isbn;

    /**
     * The Titulo.
     */
    private String titulo;

    /**
     * The Author.
     */
    private String autor;

    /**
     * The Categoria
     */
    private String categoria;

    // TODO: Agregar la calificacion.
    /**
     * The Calificacion
     */
    private double calificacion;

    /**
     * The contador de calificaciones
     */
    private int contadorCalificaciones = 0;

    /**
     * The Constructor.
     *
     * @param isbn      del libro.
     * @param titulo    del libro.
     * @param autor     del libro
     * @param categoria del libro.
     */
    public Libro(final String isbn, final String titulo, final String autor, final String categoria) {

        // TODO: agregar validacion de ISBN
        if (isbn == null || isbn.length() == 0) {
            throw new IllegalArgumentException("Isbn no valido!");
        }
        this.isbn = isbn;

        // validacion del titulo
        if (titulo == null || titulo.length() == 0) {
            throw new IllegalArgumentException("Titulo no valido!");
        }
        this.titulo = titulo;

        // TODO: Agregar validacion
        if (autor == null || autor.length() == 0) {
            throw new IllegalArgumentException("Autor no valido!");
        }
        this.autor = autor;

        // TODO: Agregar validacion
        if (categoria == null || categoria.length() == 0) {
            throw new IllegalArgumentException("Categoria no valida!");
        }
        this.categoria = categoria;
    }

    /**
     * @return the ISBN.
     */
    public String getIsbn() {
        return this.isbn;
    }

    /**
     * @return the titulo.
     */
    public String getTitulo() {
        return this.titulo;
    }

    /**
     * @return the autor.
     */
    public String getAutor() {
        return this.autor;
    }

    /**
     * @return the categoria.
     */
    public String getCategoria() {
        return this.categoria;
    }

    /**
     * @return the calificacion
     */
    public double getCalificacion() {
        return calificacion;
    }

    /**
     * @return the contador de calificaciones
     */
    public int getContadorCalificaciones() {
        return this.contadorCalificaciones;
    }

    /**
     * @param contadorCalificaciones
     */
    public void setContador(int contadorCalificaciones) {
        this.contadorCalificaciones = contadorCalificaciones;
    }


    /**
     * Calculo de las calificaciones de los libros
     *
     * @param calificacion
     * @param contadorCalificaciones
     */
    public void setCalificacion(double calificacion, int contadorCalificaciones) {

        if (contadorCalificaciones != 0) {

            this.calificacion = (calificacion + this.calificacion) / contadorCalificaciones;
        }
    }
}
