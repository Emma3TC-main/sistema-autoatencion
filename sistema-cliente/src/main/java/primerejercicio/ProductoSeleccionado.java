package primerejercicio;

public class ProductoSeleccionado {
    private String nombre;
    private int cantidad;

    public ProductoSeleccionado(String nombre, int cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}
