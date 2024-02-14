package com.example.pulperiamaritza.Modelos;

public class CTProductosItemsModel {
    private String nombre;
    private String tipo;
    private String precio;
    private int imagen;

    public CTProductosItemsModel(String nombre, String tipo, String precio, int imagen) {
        this.nombre = nombre;
        this.tipo = tipo;
        this.precio = precio;
        this.imagen = imagen;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }
}
