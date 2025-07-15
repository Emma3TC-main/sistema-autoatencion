/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import com.grupo1.dao.ClienteDAO;
import com.grupo1.dao.DetallePedidoDAO;
import com.grupo1.dao.MesaDAO;
import com.grupo1.dao.PedidoDAO;
import com.grupo1.dao.ProductoDAO;
import com.grupo1.dao.RegistroEntregaDAO;
import com.grupo1.dao.impl.ClienteDAOImpl;
import com.grupo1.dao.impl.ComandaDAOImpl;
import com.grupo1.dao.impl.DetallePedidoDAOImpl;
import com.grupo1.dao.impl.MesaDAOImpl;
import com.grupo1.dao.impl.PedidoDAOImpl;
import com.grupo1.dao.impl.ProductoDAOImpl;
import com.grupo1.dao.impl.RegistroEntregaDAOImpl;
import com.grupo1.dto.ClienteDTO;
import com.grupo1.dto.ComandaDTO;
import com.grupo1.dto.DetallePedidoDTO;
import com.grupo1.dto.MesaDTO;
import com.grupo1.dto.PedidoDTO;
import com.grupo1.dto.ProductoDTO;
import com.grupo1.dto.RegistroEntregaDTO;
import java.awt.Color;
import java.awt.Component;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

import vista.entregaVista;

/**
 *
 * @author PC
 */
public class ControladorEntrega {

    private final entregaVista vista;
    private final RegistroEntregaDAO entregaDAO = new RegistroEntregaDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final DetallePedidoDAO detalleDAO = new DetallePedidoDAOImpl();
    private final ProductoDAO productoDAO = new ProductoDAOImpl();
    private final MesaDAO mesaDAO = new MesaDAOImpl();
    private static final Logger logger = Logger.getLogger(ControladorEntrega.class.getName());

    public ControladorEntrega(entregaVista vista) {
        this.vista = vista;
        inicializarEventos();
        cargarEntregasEnTabla();
    }

    private void inicializarEventos() {
        vista.getEntregaPanel().getBtnEditar().addActionListener(e -> mostrarDetalleEntrega());
        vista.getDetalleEntrega().getBtnEntregarConfirmar().addActionListener(e -> confirmarEntrega());
        vista.getDetalleEntrega().getBtnVolver().addActionListener(e -> volverAPrincipal());

        vista.getEntregaPanel().getJTableEntrega().getSelectionModel().addListSelectionListener(e -> {
            int fila = vista.getEntregaPanel().getJTableEntrega().getSelectedRow();
            if (fila >= 0) {
                Object idPedido = vista.getEntregaPanel().getJTableEntrega().getValueAt(fila, 1);
                if (idPedido != null && !"-".equals(idPedido.toString())) {
                    vista.getEntregaPanel().getTxtIDPedidoInput().setText(idPedido.toString());
                }
            }
        });
    }

    private void cargarEntregasEnTabla() {
        try {
            List<PedidoDTO> pedidos = pedidoDAO.listar();
            List<RegistroEntregaDTO> entregas = entregaDAO.listar();
            List<ComandaDTO> comandas = new ComandaDAOImpl().listar();

            Map<Integer, RegistroEntregaDTO> entregaMap = new HashMap<>();
            for (RegistroEntregaDTO e : entregas) {
                entregaMap.put(e.getIdPedido(), e);
            }

            Map<Integer, ComandaDTO> comandaMap = new HashMap<>();
            for (ComandaDTO c : comandas) {
                comandaMap.put(c.getIdPedido(), c);
            }

            DefaultTableModel modelo = new DefaultTableModel(
                    new Object[]{"ID Entrega", "ID Pedido", "Método", "Firma", "Usuario", "Descripción", "Estado"}, 0) {
                @Override
                public boolean isCellEditable(int row, int col) {
                    return false;
                }
            };

            for (PedidoDTO pedido : pedidos) {
                String descripcion = pedido.getTipoConsumo().equals("comer_aqui")
                        ? "Mesa " + pedido.getIdMesa()
                        : "Para llevar";

                RegistroEntregaDTO entrega = entregaMap.get(pedido.getIdPedido());
                ComandaDTO comanda = comandaMap.get(pedido.getIdPedido());

                boolean comandaLista = comanda != null && "listo".equalsIgnoreCase(comanda.getEstado());
                boolean pedidoEntregado = "entregado".equalsIgnoreCase(pedido.getEstado());

                String estadoFinal;

                if ("comer_aqui".equalsIgnoreCase(pedido.getTipoConsumo())) {
                    if (pedidoEntregado) {
                        estadoFinal = "entregado";
                    } else if (!comandaLista) {
                        estadoFinal = pedido.getEstado(); // puede ser "en_preparacion"
                    } else {
                        estadoFinal = "inconsistente";
                    }
                } else if ("para_llevar".equalsIgnoreCase(pedido.getTipoConsumo())) {
                    if (pedidoEntregado) {
                        estadoFinal = "entregado";
                    } else {
                        estadoFinal = "pendiente";
                    }
                } else {
                    estadoFinal = "inconsistente";
                }

                modelo.addRow(new Object[]{
                    (entrega != null ? entrega.getIdRegistro() : "-"),
                    pedido.getIdPedido(),
                    (entrega != null ? entrega.getMetodoEntrega() : "-"),
                    (entrega != null ? entrega.getFirmaCliente() : "-"),
                    (entrega != null ? entrega.getIdUsuario() : "-"),
                    descripcion,
                    estadoFinal
                });

                // Logging de depuración
                logger.info("Verificando pedido ID " + pedido.getIdPedido());
                logger.info(" - Pedido estado: " + pedido.getEstado());
                logger.info(" - Comanda estado: " + (comanda != null ? comanda.getEstado() : "null"));
                logger.info(" - Tipo consumo: " + pedido.getTipoConsumo());
                logger.info(" - Estado calculado: " + estadoFinal);
            }

            JTable tabla = vista.getEntregaPanel().getJTableEntrega();
            tabla.setModel(modelo);
            tabla.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                    Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    switch (value.toString().toLowerCase()) {
                        case "pendiente" ->
                            c.setBackground(Color.PINK);
                        case "en_preparacion" ->
                            c.setBackground(Color.YELLOW);
                        case "listo" ->
                            c.setBackground(Color.GREEN);
                        case "entregado" ->
                            c.setBackground(Color.CYAN);
                        default ->
                            c.setBackground(Color.LIGHT_GRAY);
                    }
                    return c;
                }
            });

        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al cargar entregas", ex);
            mostrarError("Error al cargar entregas: " + ex.getMessage());
        }
    }

    private void mostrarDetalleEntrega() {
        try {
            String idTexto = vista.getEntregaPanel().getTxtIDPedidoInput().getText().trim();
            if (idTexto.isEmpty()) {
                mostrarError("Ingresa un ID de Pedido.");
                return;
            }

            int idPedido = Integer.parseInt(idTexto);
            PedidoDTO pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                mostrarError("No se encontró el pedido.");
                return;
            }

            ClienteDTO cliente = clienteDAO.buscarPorId(pedido.getIdCliente());
            MesaDTO mesa = "comer_aqui".equalsIgnoreCase(pedido.getTipoConsumo()) ? mesaDAO.buscarPorId(pedido.getIdMesa()) : null;
            List<DetallePedidoDTO> detalles = detalleDAO.listarPorPedido(idPedido);
            ComandaDTO comanda = new ComandaDAOImpl().buscarPorId(idPedido);

            StringBuilder detalleTexto = new StringBuilder();
            detalleTexto.append("📦 ID Pedido: ").append(idPedido).append("\n")
                    .append("👤 Cliente: ").append(cliente != null ? cliente.getNombre() : "Desconocido").append("\n")
                    .append("🍽️ Tipo Consumo: ").append(
                    "comer_aqui".equals(pedido.getTipoConsumo()) ? "Para Comer Aquí" : "Para Llevar"
            ).append("\n");

            if (mesa != null) {
                detalleTexto.append("🪑 Mesa: ").append(mesa.getNumero()).append("\n");
            }

            detalleTexto.append("📋 Estado Pedido: ").append(pedido.getEstado()).append("\n")
                    .append("📋 Estado Comanda: ").append(comanda != null ? comanda.getEstado() : "No registrada").append("\n")
                    .append("\n📄 Productos:\n");

            BigDecimal total = BigDecimal.ZERO;
            for (DetallePedidoDTO detalle : detalles) {
                ProductoDTO prod = productoDAO.buscarPorId(detalle.getIdProducto());
                BigDecimal subtotal = detalle.getPrecioUnitario().multiply(BigDecimal.valueOf(detalle.getCantidad()));
                detalleTexto.append("• ").append(prod.getNombre()).append(" x").append(detalle.getCantidad())
                        .append(" - S/").append(subtotal).append("\n");
                total = total.add(subtotal);
            }

            detalleTexto.append("\n💰 Total: S/ ").append(total);

            vista.getDetalleEntrega().getTxtIDPedido().setText(String.valueOf(idPedido));
            vista.getDetalleEntrega().getTxtNombreCliente().setText(cliente != null ? cliente.getNombre() : "Desconocido");
            vista.getDetalleEntrega().getTxaDetallesEntrega().setText(detalleTexto.toString());

            vista.cambiarPanel("detalleEntrega");

        } catch (Exception e) {
            logger.log(Level.WARNING, "Error en mostrarDetalleEntrega", e);
            mostrarError("Error al buscar entrega: " + e.getMessage());
        }
    }

    private void confirmarEntrega() {
        int confirm = JOptionPane.showConfirmDialog(vista, "¿Deseas confirmar la entrega?", "Confirmar", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            int idPedido = Integer.parseInt(vista.getDetalleEntrega().getTxtIDPedido().getText());
            PedidoDTO pedido = pedidoDAO.buscarPorId(idPedido);

            if (pedido == null) {
                mostrarError("Pedido no encontrado.");
                return;
            }

            // Validar si el pedido ya fue entregado
            if ("entregado".equalsIgnoreCase(pedido.getEstado())) {
                mostrarError("Este pedido ya fue entregado anteriormente.");
                return;
            }

            // Validar que el estado sea exactamente "listo" para poder entregarse
            if (!"listo".equalsIgnoreCase(pedido.getEstado())) {
                mostrarError("La cocina aún no ha terminado de preparar este pedido. Espere a que esté en estado 'listo'.");
                logger.warning("Intento de entrega bloqueado. Pedido no está 'listo'. Pedido ID: " + idPedido + ", estado actual: " + pedido.getEstado());
                return;
            }

            if ("comer_aqui".equalsIgnoreCase(pedido.getTipoConsumo())) {
                // Comer aquí: solo se actualiza el estado
                pedidoDAO.actualizarEstado(idPedido, "entregado");
                logger.info("Entrega confirmada (comer_aqui). Pedido ID: " + idPedido);
            } else {
                // Para llevar: insertar registro de entrega y actualizar estado
                RegistroEntregaDTO entrega = new RegistroEntregaDTO();
                entrega.setIdPedido(idPedido);
                entrega.setMetodoEntrega("personal");
                entrega.setFirmaCliente("FIRMADO");
                entrega.setIdUsuario(1); // ID del usuario que entrega (ajustar si tienes sistema de login)
                entrega.setFechaEntrega(java.sql.Timestamp.valueOf(LocalDateTime.now()));

                entregaDAO.insertar(entrega);
                pedidoDAO.actualizarEstado(idPedido, "entregado");

                logger.info("Entrega confirmada (para_llevar). Pedido ID: " + idPedido + " registrado con entrega.");
            }

            mostrarMensaje("Entrega confirmada exitosamente.");
            volverAPrincipal();

        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al confirmar entrega", e);
            mostrarError("Error al registrar entrega: " + e.getMessage());
        }
    }

    private void volverAPrincipal() {
        cargarEntregasEnTabla();
        vista.cambiarPanel("entregaPanel");
    }

    private void mostrarMensaje(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Información", JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
