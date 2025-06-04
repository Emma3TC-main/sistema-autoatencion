package Polleria;


import java.util.Date;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author User
 */
public class Pedido {
   private int id;
private String descripcion;
private String estado;
private Date fecha;

// Constructor completo
public Pedido(int id, String descripcion, String estado, Date fecha) {
    this.id = id;
    this.descripcion = descripcion;
    this.estado = estado;
    this.fecha = fecha;
}

// Constructor que recibía (String producto, String en_preparación)
// Lo implementamos para inicializar descripcion y estado, sin id ni fecha
public Pedido(String descripcion, String estado) {
    this.descripcion = descripcion;
    this.estado = estado;
    // Puedes inicializar id y fecha con valores por defecto si quieres
    this.id = 0; // o algún valor predeterminado
    this.fecha = new Date(); // fecha actual como ejemplo
}

// Getters
public int getId() { return id; }
public String getDescripcion() { return descripcion; }
public String getEstado() { return estado; }
public Date getFecha() { return fecha; }
}
