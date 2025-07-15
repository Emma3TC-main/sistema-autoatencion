/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import com.grupo1.dao.ProductoDAO;
import com.grupo1.dao.impl.ProductoDAOImpl;
import com.grupo1.dto.ProductoDTO;
import java.awt.Rectangle;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import vista.AdminProducto;

public class ControladorInventario {

    private final AdminProducto vista;
    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    public ControladorInventario(AdminProducto vista) {
        this.vista = vista;
        cargarTablaProductos();
        inicializarEventos();
    }

    private void inicializarEventos() {
        // Buscar producto por ID (al presionar Enter)
        vista.getTxtIDProductoInv().addActionListener(e -> buscarYSeleccionarPorID());

        // Selección en tabla
        vista.getJTableProducto().getSelectionModel().addListSelectionListener(e -> seleccionarDesdeTabla());

        // Botón Confirmar
        vista.getBtnConfirmar().addActionListener(e -> actualizarCantidad());
    }

    public void recargarTablaInventario() {
        cargarTablaProductos();
    }

    private void cargarTablaProductos() {
        try {
            List<ProductoDTO> productos = productoDAO.listar();
            DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Precio", "Stock", "Disponible"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
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

            vista.getJTableProducto().setModel(modelo);

        } catch (SQLException e) {
            mostrarError("Error al cargar productos: " + e.getMessage());
        }
    }

    private void buscarYSeleccionarPorID() {
        String idTexto = vista.getTxtIDProductoInv().getText().trim();
        if (idTexto.isEmpty()) {
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);
            JTable tabla = vista.getJTableProducto();
            DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();

            for (int i = 0; i < modelo.getRowCount(); i++) {
                if ((int) modelo.getValueAt(i, 0) == id) {
                    tabla.setRowSelectionInterval(i, i);
                    tabla.scrollRectToVisible(new Rectangle(tabla.getCellRect(i, 0, true)));
                    vista.getTxtActualCant().setText(modelo.getValueAt(i, 3).toString()); // columna 3: stock
                    return;
                }
            }

            mostrarError("Producto no encontrado.");
        } catch (NumberFormatException e) {
            mostrarError("ID inválido.");
        }
    }

    private void seleccionarDesdeTabla() {
        int fila = vista.getJTableProducto().getSelectedRow();
        if (fila >= 0) {
            JTable tabla = vista.getJTableProducto();
            vista.getTxtIDProductoInv().setText(tabla.getValueAt(fila, 0).toString());
            vista.getTxtActualCant().setText(tabla.getValueAt(fila, 3).toString()); // stock
        }
    }

    private void actualizarCantidad() {
        String idTexto = vista.getTxtIDProductoInv().getText().trim();
        String cantidadTexto = vista.getTxtCantidadAgregar().getText().trim();

        if (idTexto.isEmpty() || cantidadTexto.isEmpty()) {
            mostrarError("Complete el ID y la cantidad a agregar.");
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);
            int cantidadNueva = Integer.parseInt(cantidadTexto);

            if (cantidadNueva <= 0) {
                mostrarError("La cantidad debe ser un entero mayor a 0.");
                return;
            }

            ProductoDTO producto = productoDAO.buscarPorId(id);
            if (producto == null) {
                mostrarError("Producto no encontrado.");
                return;
            }

            int nuevaCantidad = producto.getStock() + cantidadNueva;
            producto.setStock(nuevaCantidad);
            productoDAO.actualizar(producto); // solo stock puede cambiar, lo demás permanece

            mostrarMensaje("Stock actualizado a " + nuevaCantidad);
            cargarTablaProductos();
            limpiarCampos();

        } catch (NumberFormatException e) {
            mostrarError("Cantidad inválida (debe ser entero).");
        } catch (SQLException e) {
            mostrarError("Error al actualizar producto: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        vista.getTxtIDProductoInv().setText("");
        vista.getTxtActualCant().setText("");
        vista.getTxtCantidadAgregar().setText("");
        vista.getJTableProducto().clearSelection();
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(vista, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(vista, msg, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
}
