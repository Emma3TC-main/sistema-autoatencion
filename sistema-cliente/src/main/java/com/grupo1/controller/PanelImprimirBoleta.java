/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.controller;

/**
 *
 * @author PC
 */
import com.grupo1.dto.BoletaFacturaDTO;
import com.grupo1.dto.ClienteDTO;
import com.grupo1.dto.PedidoDTO;
import com.grupo1.modelo.ProductoSeleccionado;
import com.grupo1.vista.Principal;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;

public class PanelImprimirBoleta extends JPanel {

    private final JTextArea areaBoleta;
    private final JButton btnImprimir;
    private final JButton btnNuevaCompra;

    private final Principal principal;

    public PanelImprimirBoleta(Principal principal) {
        this.principal = principal;
        setLayout(new BorderLayout());

        areaBoleta = new JTextArea(25, 60);
        areaBoleta.setEditable(false);
        areaBoleta.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scroll = new JScrollPane(areaBoleta);

        btnImprimir = new JButton("🖨️ Imprimir o Exportar PDF");
        btnNuevaCompra = new JButton("🔄 Realizar otra compra");

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotones.add(btnImprimir);
        panelBotones.add(btnNuevaCompra);

        add(new JLabel("BOLETA DE PAGO", SwingConstants.CENTER), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(panelBotones, BorderLayout.SOUTH);

        // Acción imprimir o exportar
        btnImprimir.addActionListener(e -> imprimirBoleta());

        // Acción nueva compra
        btnNuevaCompra.addActionListener(e -> {
            principal.reiniciar();  // método que debes tener para limpiar estado
            principal.mostrarPanel("Botones"); // o el panel que consideres
        });
    }

    public void mostrarBoleta(Principal principal) {
        ClienteDTO cliente = principal.getClienteTemporal();
        PedidoDTO pedido = principal.getPedidoSimulado();
        BoletaFacturaDTO boleta = principal.getBoletaSimulada();
        List<ProductoSeleccionado> productos = principal.getProductosSeleccionados();

        StringBuilder sb = new StringBuilder();
        sb.append("            Polleria: El Gran Pollon \n");
        sb.append("        Av. Central 123, Lima\n");
        sb.append("         RUC: 1234567890\n");
        sb.append("========================================\n");
        sb.append("Cliente     : ").append(cliente.getNombre()).append("\n");
        sb.append("Fecha       : ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"))).append("\n");
        sb.append("Consumo     : ").append(pedido.getTipoConsumo().equals("comer_aqui") ? "En el local" : "Para llevar").append("\n");

        if (pedido.getIdMesa() != null && pedido.getIdMesa() > 0) {
            sb.append("Mesa        : N° ").append(pedido.getIdMesa()).append("\n");
        }

        sb.append("Estado      : ").append(pedido.getEstado()).append("\n");
        sb.append("----------------------------------------\n");
        sb.append(String.format("%-25s %5s %10s\n", "Producto", "Cant", "Subtotal"));
        sb.append("----------------------------------------\n");

        for (ProductoSeleccionado p : productos) {
            String nombre = p.getProducto().getNombre();
            int cantidad = p.getCantidad();
            BigDecimal subtotal = p.getProducto().getPrecioUnitario().multiply(BigDecimal.valueOf(cantidad));
            sb.append(String.format("%-25s %5d %10.2f\n", nombre, cantidad, subtotal));
        }

        sb.append("----------------------------------------\n");
        sb.append(String.format("%-30s S/. %7.2f\n", "IGV (18%)", boleta.getIgv()));
        sb.append(String.format("%-30s S/. %7.2f\n", "TOTAL A PAGAR", boleta.getTotal()));
        sb.append("========================================\n");
        sb.append("Método de pago: ").append(boleta.getMetodoPago()).append("\n");
        sb.append("Gracias por su compra responsable 🌱\n");

        areaBoleta.setText(sb.toString());
    }

    private void imprimirBoleta() {
        int opcion = JOptionPane.showConfirmDialog(this,
                "¿Deseas exportar la boleta como PDF?",
                "Exportar PDF", JOptionPane.YES_NO_CANCEL_OPTION);

        if (opcion == JOptionPane.YES_OPTION) {
            JFileChooser fc = new JFileChooser();
            fc.setSelectedFile(new File("boleta.pdf"));
            int resultado = fc.showSaveDialog(this);
            if (resultado == JFileChooser.APPROVE_OPTION) {
                File archivo = fc.getSelectedFile();
                // Construye los datos
                ClienteDTO cliente = principal.getClienteTemporal();
                BoletaFacturaDTO boleta = principal.getBoletaSimulada();
                List<ProductoSeleccionado> productos = principal.getProductosSeleccionados();
                List<BoletaPDFExporter.ProductoLinea> lineas = new ArrayList<>();
                for (ProductoSeleccionado p : productos) {
                    lineas.add(new BoletaPDFExporter.ProductoLinea(
                            p.getProducto().getNombre(),
                            p.getCantidad(),
                            p.getProducto().getPrecioUnitario().doubleValue()
                    ));
                }
                BoletaPDFExporter.exportar(
                        archivo.getAbsolutePath(),
                        cliente.getNombre(),
                        cliente.getDNI(),
                        boleta.getMetodoPago(),
                        lineas,
                        boleta.getIgv().doubleValue(),
                        boleta.getTotal().doubleValue()
                );
                JOptionPane.showMessageDialog(this, "✅ Boleta exportada correctamente como PDF.");
            }
        } else if (opcion == JOptionPane.NO_OPTION) {
            try {
                boolean done = areaBoleta.print();
                if (done) {
                    JOptionPane.showMessageDialog(this, "✅ Boleta impresa correctamente.");
                } else {
                    JOptionPane.showMessageDialog(this, "⚠️ La impresión fue cancelada.");
                }
            } catch (PrinterException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "❌ Error al imprimir: " + e.getMessage());
            }
        }
    }
}
