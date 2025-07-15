/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import com.grupo1.dao.ComandaDAO;
import com.grupo1.dao.DetallePedidoDAO;
import com.grupo1.dao.MesaDAO;
import com.grupo1.dao.PedidoDAO;
import com.grupo1.dao.ProductoDAO;
import com.grupo1.dao.impl.ComandaDAOImpl;
import com.grupo1.dao.impl.DetallePedidoDAOImpl;
import com.grupo1.dao.impl.MesaDAOImpl;
import com.grupo1.dao.impl.PedidoDAOImpl;
import com.grupo1.dao.impl.ProductoDAOImpl;
import com.grupo1.dto.ComandaDTO;
import com.grupo1.dto.DetallePedidoDTO;
import com.grupo1.dto.MesaDTO;
import com.grupo1.dto.PedidoDTO;
import com.grupo1.dto.ProductoDTO;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableCellRenderer;
import vista.SistemaCocina;

/**
 *
 * @author PC
 */
public class ControladorSistemaCocina {

    private final SistemaCocina vista;
    private final ComandaDAO comandaDAO = new ComandaDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private final DetallePedidoDAO detalleDAO = new DetallePedidoDAOImpl();
    private final MesaDAO mesaDAO = new MesaDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();

    private String estadoSeleccionado = "";

    public ControladorSistemaCocina(SistemaCocina vista) {
        this.vista = vista;
        inicializarEventos();
        cargarComandasEnTabla();
    }

    private void inicializarEventos() {
        vista.getPanelCocinaPrincipal().getBtnEditarInput().addActionListener(e -> mostrarDetalleComanda());

        vista.getPanelCocinaPrincipal().getjTableComandas().getSelectionModel().addListSelectionListener(e -> {
            int row = vista.getPanelCocinaPrincipal().getjTableComandas().getSelectedRow();
            if (row != -1) {
                String idComanda = vista.getPanelCocinaPrincipal().getjTableComandas().getValueAt(row, 0).toString();
                vista.getPanelCocinaPrincipal().getTxtPedidoIDInput().setText(idComanda);
            }
        });

        vista.getDetalleComanda().getBtnStateListo().addActionListener(e -> estadoSeleccionado = "listo");
        vista.getDetalleComanda().getBtnStatePendiente().addActionListener(e -> estadoSeleccionado = "pendiente");
        vista.getDetalleComanda().getBtnEnPreparacion().addActionListener(e -> estadoSeleccionado = "en_preparacion");

        vista.getDetalleComanda().getBtnActualizar().addActionListener(e -> actualizarEstado());
        vista.getDetalleComanda().getBtnVolver().addActionListener(e -> volverAlPrincipal());
    }

    private void cargarComandasEnTabla() {
        try {
            List<ComandaDTO> comandas = comandaDAO.listar();
            DefaultTableModel modelo = new DefaultTableModel(
                    new Object[]{"ID Comanda", "ID Pedido", "Estado Comanda", "Tipo Consumo", "N° Mesa", "Total Pedido", "Estado Pedido"},
                    0
            ) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            for (ComandaDTO comanda : comandas) {
                PedidoDTO pedido = pedidoDAO.buscarPorId(comanda.getIdPedido());

                String tipoConsumo = pedido.getTipoConsumo().equalsIgnoreCase("comer_aqui") ? "Para Comer Aquí" : "Para Llevar";
                String nroMesa = "—";
                if ("comer_aqui".equalsIgnoreCase(pedido.getTipoConsumo())) {
                    MesaDTO mesa = mesaDAO.buscarPorId(pedido.getIdMesa());
                    nroMesa = (mesa != null) ? String.valueOf(mesa.getNumero()) : "No encontrada";
                }

                BigDecimal total = detalleDAO.calcularTotalPorPedido(pedido.getIdPedido());

                modelo.addRow(new Object[]{
                    comanda.getIdComanda(),
                    pedido.getIdPedido(),
                    comanda.getEstado(),
                    tipoConsumo,
                    nroMesa,
                    "S/ " + total.setScale(2, RoundingMode.HALF_UP),
                    pedido.getEstado()
                });
            }

            JTable tabla = vista.getPanelCocinaPrincipal().getjTableComandas();
            tabla.setModel(modelo);
            tabla.getTableHeader().setReorderingAllowed(false);
            tabla.setRowHeight(25);
            tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

            tabla.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                    String estado = table.getValueAt(row, 2).toString().toLowerCase();
                    if (isSelected) {
                        c.setBackground(new Color(0xA9C1FF)); // color para fila seleccionada
                    } else {
                        switch (estado) {
                            case "pendiente":
                                c.setBackground(new Color(255, 204, 204)); // rosa suave
                                break;
                            case "en_preparacion":
                                c.setBackground(new Color(255, 255, 153)); // amarillo claro
                                break;
                            case "listo":
                                c.setBackground(new Color(204, 255, 204)); // verde claro
                                break;
                            default:
                                c.setBackground(Color.WHITE);
                        }
                    }
                    return c;
                }
            });

        } catch (SQLException e) {
            mostrarError("Error al cargar comandas: " + e.getMessage());
        }
    }

    private void mostrarDetalleComanda() {
        try {
            String idTexto = vista.getPanelCocinaPrincipal().getTxtPedidoIDInput().getText().trim();
            if (idTexto.isEmpty()) {
                mostrarError("Debes ingresar un ID de comanda.");
                return;
            }

            int idComanda = Integer.parseInt(idTexto);
            ComandaDTO comanda = comandaDAO.buscarPorId(idComanda);

            if (comanda != null) {
                PedidoDTO pedido = pedidoDAO.buscarPorId(comanda.getIdPedido());
                List<DetallePedidoDTO> detalles = detalleDAO.listarPorPedido(pedido.getIdPedido());

                StringBuilder sb = new StringBuilder();
                sb.append("🧾 RESUMEN DEL PEDIDO\n");
                sb.append("════════════════════════════════════════════════════════════\n");
                sb.append("📦 ID Pedido     : ").append(pedido.getIdPedido()).append("\n");
                sb.append("🍽️ Tipo Consumo : ")
                        .append("comer_aqui".equalsIgnoreCase(pedido.getTipoConsumo()) ? "Para Comer Aquí" : "Para Llevar")
                        .append("\n");

                if ("comer_aqui".equalsIgnoreCase(pedido.getTipoConsumo())) {
                    MesaDTO mesa = mesaDAO.buscarPorId(pedido.getIdMesa());
                    sb.append("🪑 Mesa Número   : ").append(mesa != null ? mesa.getNumero() : "No encontrada").append("\n");
                }

                sb.append("📌 Estado Pedido : ").append(pedido.getEstado()).append("\n");
                sb.append("📋 Estado Comanda: ").append(comanda.getEstado()).append("\n");

                sb.append("\n🍽️ DETALLES DEL PEDIDO\n");
                sb.append("════════════════════════════════════════════════════════════\n");
                sb.append(String.format("%-45s│ %8s\n", "Producto", "Cantidad"));
                sb.append("────────────────────────────────────────────────────────────\n");

                for (DetallePedidoDTO detalle : detalles) {
                    ProductoDTO producto = productoDAO.buscarPorId(detalle.getIdProducto());
                    String nombreProducto = (producto != null) ? producto.getNombre() : "Producto no encontrado";

                    // Truncar si es muy largo
                    if (nombreProducto.length() > 45) {
                        nombreProducto = nombreProducto.substring(0, 42) + "...";
                    }

                    sb.append(String.format("%-45s│ %8d\n", nombreProducto, detalle.getCantidad()));
                }

                sb.append("════════════════════════════════════════════════════════════");

                vista.getDetalleComanda().getTxtIDPedido().setText(String.valueOf(comanda.getIdComanda()));
                vista.getDetalleComanda().getTxtEntrega().setText(comanda.getEstado());
                vista.getDetalleComanda().getTxaDetallesPedidos().setText(sb.toString());
                estadoSeleccionado = comanda.getEstado();
                vista.cambiarPanel("detalleComanda");

            } else {
                mostrarError("No se encontró la comanda con ID: " + idComanda);
            }

        } catch (Exception ex) {
            mostrarError("Error al mostrar detalles: " + ex.getMessage());
        }
    }

    private static final Logger LOGGER = Logger.getLogger(ControladorSistemaCocina.class.getName());

    private void actualizarEstado() {
        try {
            int idComanda = Integer.parseInt(vista.getDetalleComanda().getTxtIDPedido().getText());

            ComandaDTO comanda = comandaDAO.buscarPorId(idComanda);
            if (comanda == null) {
                mostrarError("No se encontró la comanda.");
                return;
            }

            String estadoActual = comanda.getEstado();
            LOGGER.info("Intentando actualizar estado para Comanda ID: " + idComanda);
            LOGGER.info("Estado actual: " + estadoActual + ", nuevo estado seleccionado: " + estadoSeleccionado);

            if (estadoActual.equalsIgnoreCase(estadoSeleccionado)) {
                mostrarError("⚠️ El pedido ya se encuentra en el estado '" + estadoSeleccionado + "'.");
                LOGGER.warning("Intento de cambio al mismo estado: " + estadoSeleccionado);
                return;
            }

            // Validaciones de transición válidas
            boolean transicionValida = false;
            switch (estadoActual.toLowerCase()) {
                case "pendiente":
                    transicionValida = estadoSeleccionado.equals("en_preparacion");
                    break;
                case "en_preparacion":
                    transicionValida = estadoSeleccionado.equals("listo");
                    break;
                case "listo":
                    transicionValida = false; // entregado se maneja desde Entrega
                    break;
            }

            if (!transicionValida) {
                mostrarError("⚠️ Transición inválida de '" + estadoActual + "' a '" + estadoSeleccionado + "'.");
                LOGGER.warning("Transición inválida de '" + estadoActual + "' a '" + estadoSeleccionado + "'");
                return;
            }

            // ✅ Actualizar Comanda
            comanda.setEstado(estadoSeleccionado);
            comandaDAO.actualizar(comanda);

            // ✅ Actualizar solo estado del Pedido
            pedidoDAO.actualizarEstado(comanda.getIdPedido(), estadoSeleccionado);

            mostrarMensaje("✅ Estado actualizado correctamente.");
            LOGGER.info("Estado actualizado correctamente para Comanda ID: " + idComanda);

            mostrarDetalleComanda(); // refrescar

        } catch (Exception e) {
            mostrarError("Error al actualizar estado: " + e.getMessage());
            LOGGER.log(Level.SEVERE, "Error en actualizarEstado()", e);
        }
    }

    private void volverAlPrincipal() {
        cargarComandasEnTabla();
        vista.cambiarPanel("panelCocinaPrincipal");
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
