/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author PC
 */
import com.grupo1.dao.MesaDAO;
import com.grupo1.dao.impl.MesaDAOImpl;
import com.grupo1.dto.MesaDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import vista.GestionMesas;
import vista.mesasVista;

public class ControladorGestionMesas {

    private final mesasVista vistaPrincipal;
    private final GestionMesas vista = new GestionMesas(); // El panel real

    private final MesaDAO mesaDAO = new MesaDAOImpl();

    public ControladorGestionMesas(mesasVista vistaPrincipal) {
        this.vistaPrincipal = vistaPrincipal;

        // Insertar el panel GestionMesas en el jPanelCambiante
        JPanel contenedor = vistaPrincipal.getPanelCambiante();
        contenedor.removeAll();
        contenedor.setLayout(new BorderLayout());
        contenedor.add(vista, BorderLayout.CENTER);
        contenedor.revalidate();
        contenedor.repaint();

        inicializarEventos();
        cargarTablaMesas();
    }

    private void inicializarEventos() {
        vista.getJTableMesas().getSelectionModel().addListSelectionListener(e -> {
            int fila = vista.getJTableMesas().getSelectedRow();
            if (fila >= 0) {
                String idMesa = vista.getJTableMesas().getValueAt(fila, 0).toString();
                vista.getTxtIDMesaInput().setText(idMesa);
            }
        });

        vista.getBtnCambiarEstado().addActionListener(e -> cambiarEstadoMesa());
    }

    private void cargarTablaMesas() {
        try {
            List<MesaDTO> mesas = mesaDAO.listar();
            DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID Mesa", "Número", "Estado"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (MesaDTO mesa : mesas) {
                modelo.addRow(new Object[]{mesa.getIdMesa(), mesa.getNumero(), mesa.getEstado()});
            }

            JTable tabla = vista.getJTableMesas();
            tabla.setModel(modelo);
            tabla.setDefaultRenderer(Object.class, new ColorEstadoRenderer());

        } catch (SQLException ex) {
            mostrarError("Error al cargar mesas: " + ex.getMessage());
        }
    }

    private void cambiarEstadoMesa() {
        String idTexto = vista.getTxtIDMesaInput().getText().trim();

        if (idTexto.isEmpty()) {
            mostrarError("Debes seleccionar o ingresar el ID de una mesa.");
            return;
        }

        int idMesa;
        try {
            idMesa = Integer.parseInt(idTexto);
        } catch (NumberFormatException e) {
            mostrarError("ID inválido.");
            return;
        }

        String nuevoEstado = (String) JOptionPane.showInputDialog(
                vista,
                "Selecciona el nuevo estado de la mesa:",
                "Cambiar estado",
                JOptionPane.QUESTION_MESSAGE,
                null,
                new String[]{"disponible", "ocupada"},
                "disponible"
        );

        if (nuevoEstado != null) {
            try {
                MesaDTO mesa = mesaDAO.buscarPorId(idMesa);
                if (mesa == null) {
                    mostrarError("No se encontró la mesa con ID " + idMesa);
                    return;
                }

                mesa.setEstado(nuevoEstado);
                mesaDAO.actualizar(mesa);
                mostrarMensaje("Estado actualizado correctamente.");
                cargarTablaMesas();

            } catch (SQLException e) {
                mostrarError("Error al actualizar estado: " + e.getMessage());
            }
        }
    }

    // === Renderizador para colorear filas ===
    private class ColorEstadoRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            String estado = table.getValueAt(row, 2).toString();

            if ("disponible".equalsIgnoreCase(estado)) {
                c.setBackground(new Color(198, 239, 206)); // verde claro
            } else if ("ocupada".equalsIgnoreCase(estado)) {
                c.setBackground(new Color(255, 199, 206)); // rojo claro
            } else {
                c.setBackground(Color.WHITE);
            }

            if (isSelected) {
                c.setBackground(Color.LIGHT_GRAY);
            }

            return c;
        }
    }

    private void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(vista, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(vista, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
