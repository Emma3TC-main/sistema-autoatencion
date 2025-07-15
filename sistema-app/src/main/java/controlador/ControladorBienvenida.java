/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author PC
 */
import java.awt.Window;
import javax.swing.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vista.bienvenida;
import vista.principal;
import Vista.LoginFrame;
import Controlador.ControladorLogin;

public class ControladorBienvenida {

    private final bienvenida panelBienvenida;
    private final principal ventanaPrincipal;
    private static final Logger logger = LoggerFactory.getLogger(ControladorBienvenida.class);

    public ControladorBienvenida(bienvenida panelBienvenida, principal ventanaPrincipal) {
        this.panelBienvenida = panelBienvenida;
        this.ventanaPrincipal = ventanaPrincipal;

        initEventos();
    }

    private void initEventos() {
        panelBienvenida.getBtnContinuar().addActionListener(e -> {
            logger.info("Botón 'Iniciar' presionado desde bienvenida");

            // Cerrar ventana principal
            Window window = SwingUtilities.getWindowAncestor(panelBienvenida);
            if (window != null) {
                window.dispose();
                logger.debug("Ventana principal cerrada");
            }

            // Abrir LoginFrame
            SwingUtilities.invokeLater(() -> {
                try {
                    LoginFrame login = new LoginFrame();
                    new ControladorLogin(login);
                    login.setVisible(true);
                    logger.info("LoginFrame iniciado");
                } catch (Exception ex) {
                    logger.error("Error al iniciar LoginFrame", ex);
                    JOptionPane.showMessageDialog(null, "Error al abrir el login: " + ex.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
        });
    }
}
