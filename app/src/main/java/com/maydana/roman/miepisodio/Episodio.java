package com.maydana.roman.miepisodio;

import java.io.Serializable;

/**
 * Created by Roman on 6/11/2017.
 */

public class Episodio implements Serializable {
    private int id;
    private String titulo;
    private String lugar;
    private String fecha;
    private String categoria;
    private String descripcion;
    private String rutaImagen;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getRutaImagen() {
        return rutaImagen;
    }

    public void setRutaImagen(String rutaImagen) {
        this.rutaImagen = rutaImagen;
    }

    public Episodio(int id, String titulo, String lugar, String fecha, String categoria, String descripcion, String rutaImagen) {

        this.id = id;
        this.titulo = titulo;
        this.lugar = lugar;
        this.fecha = fecha;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
    }
}
