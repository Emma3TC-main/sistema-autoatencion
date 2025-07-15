/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.controller;

import com.grupo1.modelo.ProductoSeleccionado;
import com.grupo1.vista.Resumen;
import com.grupo1.vista.Principal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ControladorResumen {

    private final Resumen vista;
    private final Principal principal;

    public ControladorResumen(Resumen vista, Principal principal) {
        this.vista = vista;
        this.principal = principal;

        // Establece layout vertical por fila para ResumenPanel
        vista.getResumenPanel().setLayout(new BoxLayout(vista.getResumenPanel(), BoxLayout.Y_AXIS));

        configurarEventos();
    }

    public void cargarResumen() {
        List<ProductoSeleccionado> productos = principal.getProductosSeleccionados();
        JPanel panel = vista.getResumenPanel();
        panel.removeAll();

        double total = 0;

        if (productos == null || productos.isEmpty()) {
            JLabel vacio = new JLabel("No hay productos seleccionados.", SwingConstants.CENTER);
            vacio.setAlignmentX(Component.CENTER_ALIGNMENT);
            panel.add(vacio);
        } else {
            for (ProductoSeleccionado ps : productos) {
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new BoxLayout(itemPanel, BoxLayout.X_AXIS));
                itemPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
                itemPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100)); // altura fija, ancho dinámico

                // Ícono
                JLabel iconoLabel = new JLabel();
                try {
                    String rutaBase = "/Imagenes/" + ps.getProducto().getIdProducto();
                    String[] extensiones = {".jpg", ".png"};
                    ImageIcon icono = null;

                    for (String ext : extensiones) {
                        try {
                            icono = new ImageIcon(getClass().getResource(rutaBase + ext));
                            if (icono.getIconWidth() > 0) {
                                break;
                            }
                        } catch (Exception ignored) {
                        }
                    }

                    if (icono != null) {
                        Image img = icono.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                        iconoLabel.setIcon(new ImageIcon(img));
                    } else {
                        iconoLabel.setText("Sin imagen");
                    }
                } catch (Exception ex) {
                    iconoLabel.setText("Img Err");
                }

                iconoLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
                itemPanel.add(iconoLabel);

                // Info
                JPanel infoPanel = new JPanel(new GridLayout(4, 1));
                infoPanel.add(new JLabel("Nombre: " + ps.getProducto().getNombre()));
                infoPanel.add(new JLabel("Cantidad: " + ps.getCantidad()));
                infoPanel.add(new JLabel("Precio unitario: S/ " + ps.getProducto().getPrecioUnitario()));
                double subtotal = ps.getCantidad() * ps.getProducto().getPrecioUnitario().doubleValue();
                total += subtotal;
                infoPanel.add(new JLabel("Subtotal: S/ " + String.format("%.2f", subtotal)));

                itemPanel.add(infoPanel);
                panel.add(itemPanel);
                panel.add(Box.createRigidArea(new Dimension(0, 10))); // separación entre filas
            }
        }

        // Total con IGV
        double totalConIgv = total * 1.18;
        vista.getTxtTotalAPagar().setText(String.format("%.2f", totalConIgv));
        vista.getTxtConsumo().setText(principal.getModoConsumo());

        panel.revalidate();
        panel.repaint();
    }

    private void configurarEventos() {
        vista.getBtnCancelar().addActionListener((ActionEvent e) -> {
            int op = JOptionPane.showConfirmDialog(vista, "¿Deseas cancelar el pedido?", "Confirmación", JOptionPane.YES_NO_OPTION);
            if (op == JOptionPane.YES_OPTION) {
                // ❌ Limpiar selección y estado
                principal.setProductosSeleccionados(new ArrayList<>());
                principal.setModoConsumo(null);
                vista.getResumenPanel().removeAll();
                vista.getTxtConsumo().setText("");
                vista.getTxtTotalAPagar().setText("");
                vista.getResumenPanel().revalidate();
                vista.getResumenPanel().repaint();
                principal.mostrarPanel("Botones");
            }
        });

        vista.getBtnContinuar().addActionListener((ActionEvent e) -> {
            JOptionPane.showMessageDialog(vista, "✅ Pedido confirmado. Ahora falta registrar datos del cliente y el pago.");
            // Aquí se mantiene todo para el siguiente paso (registro/pago)
            // Ejemplo: principal.mostrarPanel("RegistroCliente");
            principal.mostrarPanel("Registro");
        });
    }
}
