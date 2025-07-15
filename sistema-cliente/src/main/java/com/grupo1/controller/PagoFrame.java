/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.controller;

import com.grupo1.vista.Principal;
import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.math.RoundingMode;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author PC
 */
public class PagoFrame extends JFrame {

    private final Principal principal;
    private final JFXPanel navegadorPanel;
    private final BigDecimal monto;

    private static final Logger logger = LoggerFactory.getLogger(PagoFrame.class);
    private volatile boolean pagoEnProceso = false;

    public PagoFrame(Principal principal, BigDecimal monto) {
        this.principal = principal;
        this.monto = monto.setScale(2, RoundingMode.HALF_UP);
        setTitle("💳 Pago con PayPal");
        setSize(900, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        navegadorPanel = new JFXPanel();
        initComponents();
    }

    private void initComponents() {
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(navegadorPanel, BorderLayout.CENTER);
        iniciarPago();
    }

    private void iniciarPago() {
        String email = principal.getCorreoTemporal();

        if (email == null || monto == null) {
            JOptionPane.showMessageDialog(this, "❌ Faltan datos para realizar el pago.");
            logger.error("Correo o monto no disponible para procesar el pago.");
            dispose();
            return;
        }

        logger.info("🔐 Iniciando pago con PayPal para '{}' - Monto: S/ {}", email, monto);

        new Thread(() -> {
            try {
                PayPalClient client = new PayPalClient();
                String[] resultado = client.crearOrdenPago(monto.toString());

                if (resultado == null || resultado.length != 2) {
                    throw new Exception("❌ Error al generar orden: respuesta inválida de PayPal.");
                }

                String approvalUrl = resultado[0];
                String orderId = resultado[1];

                logger.debug("🔗 URL de aprobación: {}", approvalUrl);
                logger.debug("🆔 Order ID: {}", orderId);

                Platform.runLater(() -> {
                    WebView webView = new WebView();
                    WebEngine engine = webView.getEngine();

                    engine.locationProperty().addListener((obs, oldLoc, newLoc) -> {
                        if (pagoEnProceso) {
                            return;
                        }

                        if (newLoc.contains("success")) {
                            pagoEnProceso = true;
                            logger.info("✅ Redirección detectada: pago aprobado. Capturando orden...");

                            new Thread(() -> {
                                try {
                                    String boleta = client.capturarOrden(orderId);

                                    if (boleta == null || boleta.isBlank()) {
                                        throw new Exception("⚠️ Boleta vacía o nula tras la captura de orden.");
                                    }

                                    logger.info("✅ Pago confirmado por PayPal. Registrando en base de datos...");
                                    logger.debug("📄 Boleta recibida:\n{}", boleta);

                                    ControladorFinalPago controlador = new ControladorFinalPago(principal);
                                    controlador.confirmarPagoYGuardarEnBD();  // ← punto crítico de atomicidad

                                    SwingUtilities.invokeLater(() -> {
                                        JOptionPane.showMessageDialog(PagoFrame.this,
                                                "✅ Pago exitoso:\n\n" + boleta,
                                                "Confirmación de Pago", JOptionPane.INFORMATION_MESSAGE);

                                        JPanel panel = principal.getPanel("ImprimirBoleta");
                                        if (panel instanceof PanelImprimirBoleta pib) {
                                            pib.mostrarBoleta(principal);
                                            principal.mostrarPanel("ImprimirBoleta");
                                        } else {
                                            logger.warn("❌ Panel 'ImprimirBoleta' no encontrado.");
                                            JOptionPane.showMessageDialog(PagoFrame.this,
                                                    "⚠️ No se pudo mostrar la boleta.",
                                                    "Advertencia", JOptionPane.WARNING_MESSAGE);
                                        }

                                        dispose();
                                    });

                                } catch (Exception ex) {
                                    logger.error("❌ Error al capturar y registrar la orden PayPal: {}", ex.getMessage(), ex);
                                    SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(PagoFrame.this,
                                            "❌ Error al confirmar y guardar el pago.", "Error", JOptionPane.ERROR_MESSAGE));
                                    pagoEnProceso = false;
                                }
                            }).start();

                        } else if (newLoc.contains("cancel")) {
                            pagoEnProceso = true;
                            logger.warn("⚠️ El usuario canceló el proceso de pago.");
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(PagoFrame.this,
                                        "⚠️ El pago fue cancelado por el usuario.",
                                        "Pago Cancelado", JOptionPane.WARNING_MESSAGE);
                                dispose();
                            });
                        }
                    });

                    engine.load(approvalUrl);
                    navegadorPanel.setScene(new Scene(webView));
                });

            } catch (Exception ex) {
                logger.error("❌ Error al iniciar el proceso de pago: {}", ex.getMessage(), ex);
                SwingUtilities.invokeLater(() -> JOptionPane.showMessageDialog(PagoFrame.this,
                        "❌ No se pudo iniciar el pago.", "Error", JOptionPane.ERROR_MESSAGE));
                dispose();
            }
        }).start();
    }
}
