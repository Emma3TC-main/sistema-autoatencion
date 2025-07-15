/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author PC
 */
import com.grupo1.dao.UsuarioDAO;
import com.grupo1.dao.impl.UsuarioDAOImpl;
import com.grupo1.dto.UsuarioDTO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;
import java.util.List;
import vista.AdminUsuarios;

public class ControladorUsuarios {

    private final AdminUsuarios vista;
    private final UsuarioDAO usuarioDAO = new UsuarioDAOImpl();

    public ControladorUsuarios(AdminUsuarios vista) {
        this.vista = vista;
        inicializarEventos();
        cargarUsuariosEnTabla();
    }

    private void inicializarEventos() {
        vista.getBtnAgregarNuevo().addActionListener(e -> agregarUsuario());
        vista.getBtnBuscar().addActionListener(e -> buscarUsuarioPorCorreo());
        vista.getBtnEditar().addActionListener(e -> editarUsuario());
        vista.getBtnEliminar().addActionListener(e -> eliminarUsuario());

        // Buscar al presionar ENTER en campo de correo
        vista.getTxtCorreoBuscar().addActionListener(e -> buscarUsuarioPorCorreo());

        // Selección con clic (doble o simple) en la tabla
        vista.getJTableResultados().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int fila = vista.getJTableResultados().getSelectedRow();
                if (fila >= 0) {
                    llenarCamposDesdeFila(fila);
                    vista.getTxtUser().requestFocus(); // enfoque cómodo
                }
            }
        });

        // Validación de email en vivo (solo visual)
        vista.getTxtCorreo().addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                String correo = vista.getTxtCorreo().getText().trim();
                if (!correo.isEmpty() && !correo.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
                    mostrarError("Formato de correo inválido.");
                }
            }
        });
    }

    private void cargarUsuariosEnTabla() {
        try {
            List<UsuarioDTO> lista = usuarioDAO.listar();
            DefaultTableModel modelo = new DefaultTableModel(new Object[]{"ID", "Nombre", "Rol", "Correo"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (UsuarioDTO u : lista) {
                modelo.addRow(new Object[]{u.getIdUsuario(), u.getNombre(), u.getRol(), u.getCorreo()});
            }

            vista.getJTableResultados().setModel(modelo);
        } catch (SQLException ex) {
            mostrarError("Error al cargar usuarios: " + ex.getMessage());
        }
    }

    private void agregarUsuario() {
        if (!validarCampos()) {
            return;
        }

        try {
            UsuarioDTO usuario = obtenerUsuarioDesdeFormulario();
            usuarioDAO.insertar(usuario);
            mostrarMensaje("Usuario agregado correctamente.");
            cargarUsuariosEnTabla();
            limpiarCampos();

        } catch (SQLException e) {
            mostrarError("Error al agregar usuario: " + e.getMessage());
        }
    }

    private void buscarUsuarioPorCorreo() {
        String correo = vista.getTxtCorreoBuscar().getText().trim();
        if (correo.isEmpty()) {
            mostrarError("Ingrese un correo para buscar.");
            return;
        }

        try {
            DefaultTableModel modelo = (DefaultTableModel) vista.getJTableResultados().getModel();
            int filas = modelo.getRowCount();
            boolean encontrado = false;

            for (int i = 0; i < filas; i++) {
                String correoTabla = modelo.getValueAt(i, 3).toString();
                if (correoTabla.equalsIgnoreCase(correo)) {
                    vista.getJTableResultados().setRowSelectionInterval(i, i);
                    llenarCamposDesdeFila(i);
                    vista.getJTableResultados().scrollRectToVisible(new Rectangle(vista.getJTableResultados().getCellRect(i, 0, true)));
                    encontrado = true;
                    break;
                }
            }

            if (!encontrado) {
                mostrarError("Usuario no encontrado.");
            }

        } catch (Exception e) {
            mostrarError("Error al buscar usuario: " + e.getMessage());
        }
    }

    private void editarUsuario() {
        int fila = vista.getJTableResultados().getSelectedRow();
        if (fila == -1) {
            mostrarError("Seleccione un usuario en la tabla para editar.");
            return;
        }

        if (!validarCampos()) {
            return;
        }

        try {
            int id = Integer.parseInt(vista.getJTableResultados().getValueAt(fila, 0).toString());
            UsuarioDTO usuario = obtenerUsuarioDesdeFormulario();
            usuario.setIdUsuario(id);

            usuarioDAO.actualizar(usuario);
            mostrarMensaje("Usuario actualizado correctamente.");
            cargarUsuariosEnTabla();
            limpiarCampos();

        } catch (SQLException e) {
            mostrarError("Error al actualizar usuario: " + e.getMessage());
        }
    }

    private void eliminarUsuario() {
        int fila = vista.getJTableResultados().getSelectedRow();
        if (fila == -1) {
            mostrarError("Seleccione un usuario para eliminar.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                vista,
                "¿Deseas eliminar este usuario?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int id = Integer.parseInt(vista.getJTableResultados().getValueAt(fila, 0).toString());
                usuarioDAO.eliminar(id);
                mostrarMensaje("Usuario eliminado.");
                cargarUsuariosEnTabla();
                limpiarCampos();
            } catch (SQLException e) {
                mostrarError("Error al eliminar usuario: " + e.getMessage());
            }
        }
    }

    // === Métodos de ayuda ===
    private void llenarCamposDesdeFila(int fila) {
        JTable tabla = vista.getJTableResultados();
        if (fila >= 0 && fila < tabla.getRowCount()) {
            vista.getTxtUser().setText(tabla.getValueAt(fila, 1).toString());
            vista.getjComboBox1().setSelectedItem(tabla.getValueAt(fila, 2).toString());
            vista.getTxtCorreo().setText(tabla.getValueAt(fila, 3).toString());
            vista.getTxtPassword().setText(""); // seguridad

            // Aseguramos visibilidad
            tabla.setRowSelectionInterval(fila, fila);
            tabla.scrollRectToVisible(new Rectangle(tabla.getCellRect(fila, 0, true)));
        }
    }

    private UsuarioDTO obtenerUsuarioDesdeFormulario() {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNombre(vista.getTxtUser().getText().trim());
        usuario.setRol((String) vista.getjComboBox1().getSelectedItem());
        usuario.setCorreo(vista.getTxtCorreo().getText().trim());
        usuario.setPasswordHash(vista.getTxtPassword().getText().trim());
        return usuario;
    }

    private boolean validarCampos() {
        String user = vista.getTxtUser().getText().trim();
        String correo = vista.getTxtCorreo().getText().trim();
        String pass = vista.getTxtPassword().getText().trim();

        if (user.isEmpty() || correo.isEmpty() || pass.isEmpty()) {
            mostrarError("Todos los campos deben estar llenos.");
            return false;
        }

        if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.\\w+$")) {
            mostrarError("Ingrese un correo electrónico válido.");
            return false;
        }

        return true;
    }

    private void limpiarCampos() {
        vista.getTxtUser().setText("");
        vista.getTxtCorreo().setText("");
        vista.getTxtPassword().setText("");
        vista.getjComboBox1().setSelectedIndex(0);
    }

    private void mostrarMensaje(String msg) {
        JOptionPane.showMessageDialog(vista, msg, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String msg) {
        JOptionPane.showMessageDialog(vista, msg, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
