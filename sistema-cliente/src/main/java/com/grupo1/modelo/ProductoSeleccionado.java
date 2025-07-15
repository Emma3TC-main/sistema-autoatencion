package com.grupo1.modelo;

import com.grupo1.dto.ProductoDTO;

public class ProductoSeleccionado {

    private ProductoDTO producto;
    private int cantidad;

    public ProductoSeleccionado(ProductoDTO producto, int cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
    }

    public ProductoDTO getProducto() {
        return producto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
