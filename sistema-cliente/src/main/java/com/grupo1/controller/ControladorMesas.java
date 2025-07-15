/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.controller;

/**
 *
 * @author PC
 */
import com.grupo1.dto.MesaDTO;
import com.grupo1.dao.MesaDAO;
import com.grupo1.dao.impl.MesaDAOImpl; // Asegúrate que la implementación esté aquí
import com.grupo1.vista.Mesas;
import java.awt.Color;

import javax.swing.*;
import java.awt.event.ItemEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ControladorMesas {

    private final Mesas vista;
    private final MesaDAO mesaDAO;
    private final Map<Integer, JToggleButton> mapaBotones = new HashMap<>();
    private final List<MesaDTO> mesasSeleccionadas = new ArrayList<>();
    private final Map<Integer, String> estadoMesas = new HashMap<>();

    public ControladorMesas(Mesas vista) {
        this.vista = vista;
        this.mesaDAO = new MesaDAOImpl();
        inicializarMapaBotones();
        cargarMesasDesdeBD();
        configurarBotones();
        configurarBotonesAdicionales();
    }

    private void inicializarMapaBotones() {
        mapaBotones.put(1, vista.getJtbMesa1());
        mapaBotones.put(2, vista.getJtbMesa2());
        mapaBotones.put(3, vista.getJtbMesa3());
        mapaBotones.put(4, vista.getJtMesa4());
        mapaBotones.put(5, vista.getJtbMesa5());
        mapaBotones.put(6, vista.getJtbMesa6());
        mapaBotones.put(7, vista.getJtbMesa7());
        mapaBotones.put(8, vista.getJtbMesa8());
        mapaBotones.put(9, vista.getJtbMesa9());
        mapaBotones.put(10, vista.getJtbMesa10());
    }

    private void cargarMesasDesdeBD() {
        try {
            List<MesaDTO> listaMesas = mesaDAO.listar();

            for (MesaDTO mesa : listaMesas) {
                int numero = mesa.getNumero();
                String estado = mesa.getEstado().toLowerCase();

                JToggleButton boton = mapaBotones.get(numero);
                if (boton != null) {
                    if ("ocupada".equals(estado)) {
                        boton.setSelected(true);
                        boton.setBackground(Color.RED);
                        boton.setEnabled(true); // 👉 Habilitamos para poder mostrar mensaje
                        boton.addActionListener(evt -> {
                            JOptionPane.showMessageDialog(vista,
                                    "❌ La mesa " + numero + " está ocupada.\nPor favor, elija otra o seleccione para llevar.",
                                    "Mesa No Disponible", JOptionPane.WARNING_MESSAGE);
                            boton.setSelected(true); // Mantener como seleccionada
                            boton.setBackground(Color.RED);
                        });
                    } else {
                        boton.setSelected(false);
                        boton.setBackground(Color.GREEN);
                        boton.setEnabled(true); // Disponible
                    }
                }
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "Error al cargar las mesas: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void configurarBotones() {
        for (Map.Entry<Integer, JToggleButton> entry : mapaBotones.entrySet()) {
            int numeroMesa = entry.getKey();
            JToggleButton boton = entry.getValue();

            // Evita duplicar lógica en las mesas ocupadas que ya tienen listener de advertencia
            if ("ocupada".equalsIgnoreCase(boton.getBackground() == Color.RED ? "ocupada" : "libre")) {
                continue;
            }

            boton.addItemListener(e -> {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    boton.setBackground(Color.ORANGE);
                    agregarMesaSeleccionada(numeroMesa);
                } else {
                    boton.setBackground(Color.GREEN);
                    quitarMesaSeleccionada(numeroMesa);
                }
            });
        }
    }

    private void agregarMesaSeleccionada(int numeroMesa) {
        mesasSeleccionadas.removeIf(m -> m.getNumero() == numeroMesa);
        try {
            MesaDTO mesaReal = mesaDAO.buscarPorNumero(numeroMesa);
            if (mesaReal != null) {
                mesasSeleccionadas.add(mesaReal);
            } else {
                System.err.println("❌ No se encontró en BD la mesa con número: " + numeroMesa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void quitarMesaSeleccionada(int numeroMesa) {
        mesasSeleccionadas.removeIf(m -> m.getNumero() == numeroMesa);
    }

    public List<MesaDTO> obtenerMesasSeleccionadas() {
        return mesasSeleccionadas;
    }

    private void configurarBotonesAdicionales() {
        vista.getBtnVolver().addActionListener(e -> {
            vista.getPrincipal().mostrarPanel("ParaLlevar_o_Aca");
        });

        vista.getBtnContinuar().addActionListener(e -> {
            if (obtenerMesasSeleccionadas().isEmpty()) {
                JOptionPane.showMessageDialog(vista, "Selecciona al menos una mesa para continuar.");
                return;
            }

            // 👉 GUARDA LAS MESAS EN PRINCIPAL
            vista.getPrincipal().setMesasSeleccionadas(obtenerMesasSeleccionadas());

            // 👉 Llamamos a cargarResumen antes de mostrar el panel
            vista.getPrincipal().getControladorResumen().cargarResumen();
            vista.getPrincipal().mostrarPanel("Resumen");
        });
    }

}
