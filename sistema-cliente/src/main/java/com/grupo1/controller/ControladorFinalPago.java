/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.controller;

import com.grupo1.dao.*;
import com.grupo1.dao.impl.*;
import com.grupo1.dto.*;
import com.grupo1.modelo.ProductoSeleccionado;
import com.grupo1.vista.Principal;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;

import javax.swing.JOptionPane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ControladorFinalPago {

    private final Principal principal;

    private final ClienteDAO clienteDAO = new ClienteDAOImpl();
    private final PedidoDAO pedidoDAO = new PedidoDAOImpl();
    private final DetallePedidoDAO detalleDAO = new DetallePedidoDAOImpl();
    private final BoletaFacturaDAO boletaDAO = new BoletaFacturaDAOImpl();
    private final ComandaDAO comandaDAO = new ComandaDAOImpl();
    private final GuiaRemisionDAO guiaDAO = new GuiaRemisionDAOImpl();
    private final RegistroEntregaDAO entregaDAO = new RegistroEntregaDAOImpl();

    private static final Logger logger = LoggerFactory.getLogger(ControladorFinalPago.class);

    public ControladorFinalPago(Principal principal) {
        this.principal = principal;
    }

    public void confirmarPagoYGuardarEnBD() {
        logger.info("▶️ Iniciando proceso de confirmación de pago y registro en BD...");

        try {
            // === Recuperar datos simulados desde Principal ===
            ClienteDTO cliente = principal.getClienteTemporal();
            PedidoDTO pedido = principal.getPedidoSimulado();
            List<ProductoSeleccionado> productos = principal.getProductosSeleccionados();
            BoletaFacturaDTO boleta = principal.getBoletaSimulada();
            ComandaDTO comanda = principal.getComandaSimulada();
            GuiaRemisionDTO guia = principal.getGuiaSimulada();
            RegistroEntregaDTO entrega = principal.getEntregaSimulada();

            logger.debug("📋 Cliente: {}", cliente);
            logger.debug("🧾 Boleta: {}", boleta);
            logger.debug("🍽️ Comanda: {}", comanda);
            logger.debug("📦 Guía: {}", guia);
            logger.debug("🚚 Entrega: {}", entrega);

            // === Validaciones ===
            if (cliente == null || pedido == null || boleta == null || productos == null || productos.isEmpty()) {
                logger.error("❌ Datos simulados incompletos. Verifique que se haya realizado la confirmación.");
                JOptionPane.showMessageDialog(null, "❌ Error: Datos incompletos para registrar el pedido.");
                return;
            }

            if (cliente.getIdCliente() == 0) {
                // Generar e insertar cliente
                int nuevoIdCliente = clienteDAO.Listar().stream()
                        .mapToInt(ClienteDTO::getIdCliente).max().orElse(0) + 1;
                cliente.setIdCliente(nuevoIdCliente);
                clienteDAO.insertar(cliente);
                logger.info("✅ Cliente insertado: {}", cliente);
            } else if (clienteDAO.buscarPorId(cliente.getIdCliente()) == null) {
                logger.error("❌ Cliente con ID {} no encontrado en la BD.", cliente.getIdCliente());
                JOptionPane.showMessageDialog(null, "❌ Error: Cliente inválido.");
                return;
            } else {
                logger.info("✅ Cliente ya registrado en BD: {}", cliente);
            }

            // === Insertar Pedido ===
            int nuevoIdPedido = pedidoDAO.listar().stream()
                    .mapToInt(PedidoDTO::getIdPedido).max().orElse(0) + 1;

            pedido.setIdPedido(nuevoIdPedido);
            pedido.setIdCliente(cliente.getIdCliente());
            pedidoDAO.insertar(pedido);
            logger.info("✅ Pedido insertado: {}", pedido);

            // === Insertar Detalles ===
            for (ProductoSeleccionado ps : productos) {
                if (ps.getProducto() == null || ps.getCantidad() <= 0) {
                    logger.warn("⚠️ Producto inválido o cantidad no válida: {}", ps);
                    continue;
                }

                DetallePedidoDTO detalle = new DetallePedidoDTO();
                detalle.setIdPedido(pedido.getIdPedido());
                detalle.setIdProducto(ps.getProducto().getIdProducto());
                detalle.setCantidad(ps.getCantidad());
                detalle.setPrecioUnitario(ps.getProducto().getPrecioUnitario());

                detalleDAO.insertar(detalle);
                logger.debug("✅ Detalle insertado: {}", detalle);
            }

            // === Insertar Boleta/Factura ===
            boleta.setIdPedido(pedido.getIdPedido());
            boletaDAO.insertar(boleta);
            logger.info("✅ Boleta/Factura registrada: {}", boleta);

            // === Insertar Comanda ===
            comanda.setIdPedido(pedido.getIdPedido());
            comandaDAO.insertar(comanda);
            logger.info("✅ Comanda insertada: {}", comanda);

            // === Guía y Entrega para ambos casos ===
            if ("para_llevar".equalsIgnoreCase(pedido.getTipoConsumo())) {
                if (guia != null) {
                    guia.setIdPedido(pedido.getIdPedido());
                    guia.setIdCliente(cliente.getIdCliente());
                    guiaDAO.insertar(guia);
                    logger.info("✅ Guía de remisión insertada: {}", guia);
                }

                if (entrega != null) {
                    entrega.setIdPedido(pedido.getIdPedido());
                    entregaDAO.insertar(entrega);
                    logger.info("✅ Registro de entrega insertado: {}", entrega);
                }

            } else if ("comer_aqui".equalsIgnoreCase(pedido.getTipoConsumo())) {
                // === Marcar mesas como ocupadas ===
                List<MesaDTO> mesasSeleccionadas = principal.getMesasSeleccionadas();
                if (mesasSeleccionadas != null && !mesasSeleccionadas.isEmpty()) {
                    MesaDAO mesaDAO = new MesaDAOImpl();
                    for (MesaDTO mesa : mesasSeleccionadas) {
                        mesa.setEstado("ocupada");
                        mesaDAO.actualizar(mesa);
                        logger.info("✅ Mesa marcada como ocupada: {}", mesa);
                    }
                } else {
                    logger.warn("⚠️ No se seleccionaron mesas, pero el consumo es 'comer_aqui'");
                }

                // === También generar Guía y Entrega ===
                if (guia != null) {
                    guia.setIdPedido(pedido.getIdPedido());
                    guia.setIdCliente(cliente.getIdCliente());
                    guiaDAO.insertar(guia);
                    logger.info("✅ Guía de remisión insertada: {}", guia);
                }

                if (entrega != null) {
                    entrega.setIdPedido(pedido.getIdPedido());
                    entregaDAO.insertar(entrega);
                    logger.info("✅ Registro de entrega insertado: {}", entrega);
                }
            }

            logger.info("✅ Proceso de registro finalizado correctamente.");
            JOptionPane.showMessageDialog(null, "✅ Pago confirmado y datos registrados exitosamente.");

        } catch (SQLException ex) {
            logger.error("❌ Error SQL al guardar en BD: {}", ex.getMessage(), ex);
            JOptionPane.showMessageDialog(null, "❌ Error al guardar en BD: " + ex.getMessage());
        } catch (Exception e) {
            logger.error("❌ Error inesperado durante el proceso final de pago: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "❌ Error inesperado: " + e.getMessage());
        }
    }
}
