/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.controller;

/**
 *
 * @author PC
 */
import com.grupo1.vista.Mesas;
import com.grupo1.vista.ParaLlevar_o_Aca;
import com.grupo1.vista.Principal;
import com.grupo1.vista.Registro;

import javax.swing.*;

public class ControladorFormaConsumo {

    private final ParaLlevar_o_Aca vista;
    private final Principal principal;
    private String modoSeleccionado = "";

    public ControladorFormaConsumo(ParaLlevar_o_Aca vista) {
        this.vista = vista;
        this.principal = vista.getPrincipal();
        inicializarEventos();
    }

    private void inicializarEventos() {
        vista.getJToggleButton1().addActionListener(e -> seleccionarAca());
        vista.getJToggleButton2().addActionListener(e -> seleccionarLlevar());

        vista.getContinuar().addActionListener(e -> continuar());
        vista.getRegresar().addActionListener(e -> principal.mostrarPanel("Botones"));
    }

    private void seleccionarAca() {
        vista.getJToggleButton1().setBackground(java.awt.Color.GREEN);
        vista.getJToggleButton2().setBackground(null);
        modoSeleccionado = "comer_aqui";
    }

    private void seleccionarLlevar() {
        vista.getJToggleButton2().setBackground(java.awt.Color.GREEN);
        vista.getJToggleButton1().setBackground(null);
        modoSeleccionado = "para_llevar";
    }

    private void continuar() {
        if (modoSeleccionado.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Por favor, selecciona una opción antes de continuar.");
            return;
        }

        // Guardar el modo de consumo en Principal (comer_aqui o para_llevar)
        principal.setModoConsumo(modoSeleccionado);

        // Mostrar mensaje para el usuario con texto visible
        String textoVisible = modoSeleccionado.equals("comer_aqui") ? "Para Comer Aquí" : "Para Llevar";
        JOptionPane.showMessageDialog(vista, "✅ Opción elegida: " + textoVisible);

        // Redirigir según el modo
        if ("comer_aqui".equalsIgnoreCase(modoSeleccionado)) {
            principal.mostrarPanel("Mesas");
        } else {
            principal.getControladorResumen().cargarResumen();
            principal.mostrarPanel("Resumen");
        }
    }

    public String getModoSeleccionado() {
        return modoSeleccionado;
    }

    private void regresar() {
        vista.getPrincipal().mostrarPanel("SeleccionarProducto"); // ← o el nombre con el que registraste el catálogo
    }

}
