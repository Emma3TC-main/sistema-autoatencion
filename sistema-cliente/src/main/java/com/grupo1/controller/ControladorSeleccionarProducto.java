/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.controller;

import com.grupo1.dao.ProductoDAO;
import com.grupo1.dao.impl.ProductoDAOImpl;
import com.grupo1.dto.ProductoDTO;
import com.grupo1.modelo.ProductoSeleccionado;
import com.grupo1.vista.SeleccionarProducto;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class ControladorSeleccionarProducto {

    private final SeleccionarProducto vista;
    private final ProductoDAO productoDAO;
    private final List<ProductoSeleccionado> productosSeleccionados = new ArrayList<>();

    public ControladorSeleccionarProducto(SeleccionarProducto vista) {
        this.vista = vista;
        this.productoDAO = new ProductoDAOImpl();

        // Recupera si ya hay productos seleccionados en Principal
        List<ProductoSeleccionado> productosPrevios = vista.getPrincipal().getProductosSeleccionados();
        if (productosPrevios != null) {
            this.productosSeleccionados.addAll(productosPrevios);
        }

        cargarProductos();
        configurarBotones();
    }

    private void cargarProductos() {
        try {
            List<ProductoDTO> productos = productoDAO.listar();
            JPanel panel = vista.getPanelCatalogo();
            panel.removeAll(); // Limpiar antes de añadir

            for (ProductoDTO producto : productos) {
                if (!producto.isDisponible()) {
                    continue;
                }
                JPanel tarjeta = crearTarjetaProducto(producto);
                panel.add(tarjeta);
            }

            panel.revalidate();
            panel.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(vista, "❌ Error al cargar productos: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private JPanel crearTarjetaProducto(ProductoDTO producto) {
        JPanel tarjeta = new JPanel(new BorderLayout());
        tarjeta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        tarjeta.setPreferredSize(new Dimension(160, 220));

        JLabel imagen = new JLabel("", SwingConstants.CENTER);

        // Buscar imagen con .jpg o .png
        String rutaBase = "/Imagenes/" + producto.getIdProducto();
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

        if (icono != null && icono.getIconWidth() > 0) {
            Image imgEscalada = icono.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
            imagen.setIcon(new ImageIcon(imgEscalada));
        } else {
            imagen.setText("Sin Imagen");
            imagen.setHorizontalAlignment(SwingConstants.CENTER);
        }

        JLabel nombre = new JLabel(producto.getNombre(), SwingConstants.CENTER);
        JLabel precio = new JLabel("S/ " + producto.getPrecioUnitario(), SwingConstants.CENTER);

        // Buscar si ya fue seleccionado antes
        ProductoSeleccionado existente = productosSeleccionados.stream()
                .filter(p -> p.getProducto().getIdProducto() == producto.getIdProducto())
                .findFirst()
                .orElse(null);

        // Spinner de cantidad
        int cantidadInicial = (existente != null) ? existente.getCantidad() : 1;
        JSpinner spinner = new JSpinner(new SpinnerNumberModel(cantidadInicial, 1, producto.getStock(), 1));
        spinner.setPreferredSize(new Dimension(60, 20));

        JToggleButton boton = new JToggleButton("Agregar");
        if (existente != null) {
            boton.setSelected(true);
            boton.setText("Quitar");
            boton.setBackground(Color.ORANGE);
        }

        // Acción del botón toggle
        boton.addActionListener((ActionEvent e) -> {
            int cantidad = (int) spinner.getValue();
            if (boton.isSelected()) {
                productosSeleccionados.removeIf(p -> p.getProducto().getIdProducto() == producto.getIdProducto());
                productosSeleccionados.add(new ProductoSeleccionado(producto, cantidad));
                boton.setText("Quitar");
                boton.setBackground(Color.ORANGE);
            } else {
                productosSeleccionados.removeIf(p -> p.getProducto().getIdProducto() == producto.getIdProducto());
                boton.setText("Agregar");
                boton.setBackground(null);
            }
        });

        // Acción del spinner: si el producto está seleccionado, actualizar su cantidad
        spinner.addChangeListener(e -> {
            if (boton.isSelected()) {
                int nuevaCantidad = (int) spinner.getValue();
                productosSeleccionados.stream()
                        .filter(p -> p.getProducto().getIdProducto() == producto.getIdProducto())
                        .findFirst()
                        .ifPresent(p -> p.setCantidad(nuevaCantidad));
            }
        });

        tarjeta.add(imagen, BorderLayout.NORTH);
        tarjeta.add(nombre, BorderLayout.CENTER);

        JPanel panelAbajo = new JPanel(new GridLayout(3, 1));
        panelAbajo.add(precio);
        panelAbajo.add(spinner);
        panelAbajo.add(boton);

        tarjeta.add(panelAbajo, BorderLayout.SOUTH);
        return tarjeta;
    }

    private void configurarBotones() {
        vista.getContinuar().addActionListener(e -> continuar());
    }

    private void continuar() {
        if (productosSeleccionados.isEmpty()) {
            JOptionPane.showMessageDialog(vista, "Selecciona al menos un producto para continuar.");
            return;
        }

        vista.getPrincipal().setProductosSeleccionados(productosSeleccionados); // ← debe existir este método
        JOptionPane.showMessageDialog(vista, "✅ Productos seleccionados: " + productosSeleccionados.size());
        vista.getPrincipal().mostrarPanel("ParaLlevar_o_Aca");
    }

    private void regresar() {
        vista.getPrincipal().mostrarPanel("ParaLlevar_o_Aca");
    }
}
