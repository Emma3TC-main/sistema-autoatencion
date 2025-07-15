/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author PC
 */
import com.grupo1.dao.ProductoDAO;
import com.grupo1.dao.impl.ProductoDAOImpl;
import com.grupo1.dto.ProductoDTO;
import java.awt.*;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.table.*;
import util.ExportadorExcel;

import vista.AdminProducto;

public class ControladorMantenimientoProducto {

    private final AdminProducto vista;
    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    public ControladorMantenimientoProducto(AdminProducto vista) {
        this.vista = vista;
        inicializarEventos();
        bloquearCampos(true);
        cargarTablaProductos();
        this.vista.getJcbEstado().setRenderer(new EstadoComboRenderer());
    }

    private void inicializarEventos() {
        vista.getBtnAgregarNuevoProducto().addActionListener(e -> agregarProducto());
        vista.getBtnEditar().addActionListener(e -> editarProducto());
        vista.getBtnEliminar().addActionListener(e -> eliminarProducto());
        vista.getBtnBuscar().addActionListener(e -> buscarPorId());
        vista.getBtnExportar().addActionListener(e -> {
            ExportadorExcel.exportar(vista, vista.getJTableResultadosProducto(), "productos");
        });
        vista.getJTableResultadosProducto().getSelectionModel().addListSelectionListener(e -> {
            int fila = vista.getJTableResultadosProducto().getSelectedRow();
            if (fila >= 0) {
                vista.getTxtBuscarID().setText(vista.getJTableResultadosProducto().getValueAt(fila, 0).toString());
                vista.getTxtNombreProducto().setText(vista.getJTableResultadosProducto().getValueAt(fila, 1).toString());
                vista.getTxtPrecioUnitario().setText(vista.getJTableResultadosProducto().getValueAt(fila, 2).toString());
                vista.getTxtCantidad().setText(vista.getJTableResultadosProducto().getValueAt(fila, 3).toString());
                boolean disponible = (boolean) vista.getJTableResultadosProducto().getValueAt(fila, 4);
                vista.getJcbEstado().setSelectedItem(disponible ? "Disponible" : "No disponible");
                bloquearCampos(false);
            }
        });
    }

    private void cargarTablaProductos() {
        try {
            List<ProductoDTO> productos = productoDAO.listar();
            DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock", "Disponible"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }

                @Override
                public Class<?> getColumnClass(int columnIndex) {
                    return columnIndex == 4 ? Boolean.class : Object.class;
                }
            };

            for (ProductoDTO p : productos) {
                modelo.addRow(new Object[]{
                    p.getIdProducto(),
                    p.getNombre(),
                    p.getPrecioUnitario(),
                    p.getStock(),
                    p.isDisponible()
                });
            }

            JTable tabla = vista.getJTableResultadosProducto();
            tabla.setModel(modelo);
            tabla.setDefaultRenderer(Object.class, new ProductoEstadoRenderer());

        } catch (SQLException e) {
            mostrarError("Error al cargar productos: " + e.getMessage());
        }
    }

    private void buscarPorId() {
        String idTexto = vista.getTxtBuscarID().getText().trim();
        if (idTexto.isEmpty()) {
            mostrarError("Ingrese un ID para buscar.");
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);
            ProductoDTO producto = productoDAO.buscarPorId(id);
            if (producto == null) {
                mostrarError("Producto no encontrado.");
                return;
            }

            // Autocompletar campos
            vista.getTxtNombreProducto().setText(producto.getNombre());
            vista.getTxtPrecioUnitario().setText(producto.getPrecioUnitario().toString());
            vista.getTxtCantidad().setText(String.valueOf(producto.getStock()));
            vista.getJcbEstado().setSelectedItem(producto.isDisponible() ? "Disponible" : "No disponible");
            bloquearCampos(false);

            // Resaltar fila
            JTable tabla = vista.getJTableResultadosProducto();
            for (int i = 0; i < tabla.getRowCount(); i++) {
                if ((int) tabla.getValueAt(i, 0) == id) {
                    tabla.setRowSelectionInterval(i, i);
                    tabla.scrollRectToVisible(new Rectangle(tabla.getCellRect(i, 0, true)));
                    break;
                }
            }

        } catch (NumberFormatException e) {
            mostrarError("ID inválido.");
        } catch (SQLException e) {
            mostrarError("Error al buscar producto: " + e.getMessage());
        }
    }

    private void agregarProducto() {
        try {
            ProductoDTO producto = obtenerProductoDesdeFormulario(false);
            if (producto == null) {
                return;
            }

            productoDAO.insertar(producto);
            mostrarMensaje("Producto agregado correctamente.");
            limpiarCampos();
            cargarTablaProductos();
        } catch (SQLException e) {
            mostrarError("Error al agregar producto: " + e.getMessage());
        }
    }

    private void editarProducto() {
        try {
            ProductoDTO producto = obtenerProductoDesdeFormulario(true);
            if (producto == null) {
                return;
            }

            productoDAO.actualizar(producto);
            mostrarMensaje("Producto actualizado correctamente.");
            limpiarCampos();
            cargarTablaProductos();
        } catch (SQLException e) {
            mostrarError("Error al actualizar producto: " + e.getMessage());
        }
    }

    private void eliminarProducto() {
        String idTexto = vista.getTxtBuscarID().getText().trim();
        if (idTexto.isEmpty()) {
            mostrarError("Ingrese el ID del producto a eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(vista, "¿Está seguro de eliminar este producto?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(idTexto);
                productoDAO.eliminar(id);
                mostrarMensaje("Producto eliminado.");
                limpiarCampos();
                cargarTablaProductos();
            } catch (NumberFormatException e) {
                mostrarError("ID inválido.");
            } catch (SQLException e) {
                mostrarError("Error al eliminar producto: " + e.getMessage());
            }
        }
    }

    private ProductoDTO obtenerProductoDesdeFormulario(boolean incluirId) {
        try {
            String nombre = vista.getTxtNombreProducto().getText().trim();
            String precioStr = vista.getTxtPrecioUnitario().getText().trim();
            String cantidadStr = vista.getTxtCantidad().getText().trim();
            String estado = vista.getJcbEstado().getSelectedItem().toString();

            if (nombre.isEmpty() || precioStr.isEmpty() || cantidadStr.isEmpty()) {
                mostrarError("Todos los campos deben estar completos.");
                return null;
            }

            BigDecimal precio = new BigDecimal(precioStr);
            int cantidad = Integer.parseInt(cantidadStr);

            if (precio.compareTo(BigDecimal.ZERO) < 0 || cantidad < 0) {
                mostrarError("El precio y la cantidad deben ser mayores o iguales a cero.");
                return null;
            }

            boolean disponible = estado.equalsIgnoreCase("Disponible");

            ProductoDTO producto = new ProductoDTO();
            if (incluirId) {
                int id = Integer.parseInt(vista.getTxtBuscarID().getText().trim());
                producto.setIdProducto(id);
            }

            producto.setNombre(nombre);
            producto.setPrecioUnitario(precio);
            producto.setStock(cantidad);
            producto.setDisponible(disponible);
            return producto;

        } catch (NumberFormatException e) {
            mostrarError("Datos numéricos inválidos.");
            return null;
        }
    }

    private void bloquearCampos(boolean bloquear) {
        vista.getTxtNombreProducto().setEnabled(!bloquear);
        vista.getTxtPrecioUnitario().setEnabled(!bloquear);
        vista.getTxtCantidad().setEnabled(!bloquear);
        vista.getJcbEstado().setEnabled(!bloquear);
        vista.getBtnEditar().setEnabled(!bloquear);
        vista.getBtnEliminar().setEnabled(!bloquear);
    }

    private void limpiarCampos() {
        vista.getTxtBuscarID().setText("");
        vista.getTxtNombreProducto().setText("");
        vista.getTxtPrecioUnitario().setText("");
        vista.getTxtCantidad().setText("");
        vista.getJcbEstado().setSelectedIndex(0);
        vista.getJTableResultadosProducto().clearSelection();
        bloquearCampos(true);
    }

    private void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(vista, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(vista, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    // === Combo con colores en items ===
    private static class EstadoComboRenderer extends DefaultListCellRenderer {

        @Override
        public Component getListCellRendererComponent(JList<?> list, Object value,
                int index, boolean isSelected,
                boolean cellHasFocus) {
            JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
            String estado = value.toString();

            if ("Disponible".equalsIgnoreCase(estado)) {
                label.setForeground(new Color(0, 128, 0));
            } else if ("No disponible".equalsIgnoreCase(estado)) {
                label.setForeground(Color.RED);
            } else {
                label.setForeground(Color.BLACK);
            }

            return label;
        }
    }

    public void recargarTablaProductos() {
        cargarTablaProductos();
    }

    // === Colores por estado en tabla ===
    private static class ProductoEstadoRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus,
                int row, int column) {
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            boolean disponible = (boolean) table.getValueAt(row, 4);

            if (isSelected) {
                c.setBackground(Color.LIGHT_GRAY);
            } else {
                c.setBackground(disponible ? new Color(198, 239, 206) : new Color(255, 199, 206));
            }

            return c;
        }
    }

}
