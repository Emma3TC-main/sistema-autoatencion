/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.controller;

import com.grupo1.dao.ClienteDAO;
import com.grupo1.dao.PedidoDAO;
import com.grupo1.dao.impl.ClienteDAOImpl;
import com.grupo1.dao.impl.PedidoDAOImpl;
import com.grupo1.dto.BoletaFacturaDTO;
import com.grupo1.dto.ClienteDTO;
import com.grupo1.dto.ComandaDTO;
import com.grupo1.dto.GuiaRemisionDTO;
import com.grupo1.dto.MesaDTO;
import com.grupo1.dto.PedidoDTO;
import com.grupo1.dto.RegistroEntregaDTO;
import com.grupo1.modelo.ProductoSeleccionado;
import com.grupo1.vista.Confirmacion;
import com.grupo1.vista.Principal;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author PC
 */
public class ControladorConfirmacion {

    private final Confirmacion vista;
    private final Principal principal;

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();

    private static final Logger logger = LoggerFactory.getLogger(ControladorConfirmacion.class);

    public ControladorConfirmacion(Confirmacion vista, Principal principal) {
        this.vista = vista;
        this.principal = principal;

        this.vista.getBtnYes().addActionListener(e -> verificarSimulacion());
        this.vista.getBtnNo().addActionListener(e -> principal.mostrarPanel("Registro"));
    }

    private void verificarSimulacion() {
        try {
            ClienteDTO cliente = principal.getClienteTemporal();
            if (cliente == null) {
                JOptionPane.showMessageDialog(vista, "❌ No se ha registrado un cliente.");
                logger.warn("Cliente temporal no disponible.");
                return;
            }

            // Asignar ID temporal al cliente e insertar en BD si no existe
            if (cliente.getIdCliente() == 0) {
                int nuevoIdCliente = clienteDAO.Listar().stream()
                        .mapToInt(ClienteDTO::getIdCliente).max().orElse(0) + 1;
                cliente.setIdCliente(nuevoIdCliente);
                clienteDAO.insertar(cliente);
                logger.info("Cliente insertado: {}", cliente);
            }

            logger.info("ClienteDTO verificado: {}", cliente);

            // Simular Pedido
            PedidoDTO pedido = new PedidoDTO();
            int nuevoIdPedido = pedidoDAO.listar().stream()
                    .mapToInt(PedidoDTO::getIdPedido).max().orElse(0) + 1;

            pedido.setIdPedido(nuevoIdPedido);
            pedido.setIdCliente(cliente.getIdCliente());

            String modo = principal.getModoConsumo();
            if (modo == null || (!modo.equalsIgnoreCase("comer_aqui") && !modo.equalsIgnoreCase("para_llevar"))) {
                JOptionPane.showMessageDialog(vista, "❌ Modo de consumo inválido. Debe ser 'comer_aqui' o 'para_llevar'.");
                return;
            }

            pedido.setTipoConsumo(modo.toLowerCase());
            pedido.setEstado("pendiente");

            if (pedido.getTipoConsumo().equals("comer_aqui")) {
                List<MesaDTO> mesas = principal.getMesasSeleccionadas();
                if (mesas == null || mesas.isEmpty()) {
                    JOptionPane.showMessageDialog(vista, "⚠️ No hay mesa seleccionada para 'comer aquí'.");
                    return;
                }

                MesaDTO mesa = mesas.get(0);
                if (mesa.getIdMesa() == 0) {
                    JOptionPane.showMessageDialog(vista, "❌ La mesa seleccionada no tiene ID válido en la BD.");
                    return;
                }

                pedido.setIdMesa(mesa.getIdMesa()); // ✅ ahora sí el id_mesa es válido
            } else {
                pedido.setIdMesa(null); // ❗ usa null, no 0
            }

            logger.info("PedidoDTO simulado: {}", pedido);

            List<ProductoSeleccionado> productos = principal.getProductosSeleccionados();
            if (productos == null || productos.isEmpty()) {
                JOptionPane.showMessageDialog(vista, "❌ No se han seleccionado productos.");
                return;
            }

            BigDecimal total = productos.stream()
                    .filter(p -> p.getProducto() != null && p.getProducto().getPrecioUnitario() != null)
                    .map(p -> p.getProducto().getPrecioUnitario().multiply(BigDecimal.valueOf(p.getCantidad())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal igv = total.multiply(BigDecimal.valueOf(0.18)).setScale(2, RoundingMode.HALF_UP);

            // Simular Boleta
            BoletaFacturaDTO boleta = new BoletaFacturaDTO();
            boleta.setIdPedido(nuevoIdPedido);
            boleta.setTipoDocumento("boleta");
            boleta.setTotal(total);
            boleta.setIgv(igv);
            boleta.setMetodoPago("PayPal");
            boleta.setIdUsuario(1);

            // Simular Comanda
            ComandaDTO comanda = new ComandaDTO();
            comanda.setIdPedido(nuevoIdPedido);
            comanda.setEstado("pendiente");

            // Simular guía y entrega
            GuiaRemisionDTO guia = null;
            RegistroEntregaDTO entrega = null;
            if (pedido.getTipoConsumo().equals("para_llevar")) {
                guia = new GuiaRemisionDTO();
                guia.setIdPedido(nuevoIdPedido);
                guia.setIdCliente(cliente.getIdCliente());
                guia.setRazonSocial(cliente.getRazonSocial() != null ? cliente.getRazonSocial() : cliente.getNombre());
                guia.setDireccionEntrega("Dirección no registrada");
                guia.setFirmaEntrega("Por confirmar");

                entrega = new RegistroEntregaDTO();
                entrega.setIdPedido(nuevoIdPedido);
                entrega.setIdUsuario(1);
                entrega.setMetodoEntrega("personal");
                entrega.setFirmaCliente(cliente.getNombre());
            }

            // Guardar simulaciones en Principal
            principal.setClienteTemporal(cliente);
            principal.setPedidoSimulado(pedido);
            principal.setBoletaSimulada(boleta);
            principal.setComandaSimulada(comanda);
            principal.setGuiaSimulada(guia);
            principal.setEntregaSimulada(entrega);

            logger.info("✅ Simulación lista. Total: {}", total);
            SwingUtilities.invokeLater(() -> new PagoFrame(principal, total).setVisible(true));

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(vista, "❌ Error durante simulación: " + ex.getMessage());
            logger.error("Error durante simulación", ex);
        }
    }

}
