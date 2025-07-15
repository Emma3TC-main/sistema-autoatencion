/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Vista.LoginFrame;
import com.grupo1.dao.UsuarioDAO;
import com.grupo1.dao.impl.UsuarioDAOImpl;
import com.grupo1.dto.UsuarioDTO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import com.grupo1.vista.Principal;
import vista.SistemaCocina;
import vista.entregaVista;
import vista.mesasVista;
import vista.panelAdmin;
import controlador.ControladorGestionMesas;
import controlador.ControladorEntrega;
import controlador.ControladorSistemaCocina;
//para el jfreme de cliente:
/**
 *
 * @author PC
 */
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;

public class ControladorLogin {

    private static final Logger logger = LoggerFactory.getLogger(ControladorLogin.class);

    private final LoginFrame view;
    private final UsuarioDAO usuarioDAO;

    public ControladorLogin(LoginFrame view) {
        this.view = view;
        this.usuarioDAO = new UsuarioDAOImpl();
        initController();
        logger.info("ControladorLogin inicializado");
    }

    private void initController() {
        view.getBtnLogin().addActionListener(e -> procesarLogin());
        logger.debug("Evento de botón Login registrado.");
    }

    private void procesarLogin() {
        String correo = view.getCorreo().trim();
        String password = view.getPassword().trim();

        logger.debug("Intentando login con correo: {}", correo);

        if (correo.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor complete todos los campos obligatorios.", "Campos incompletos");
            logger.warn("Login fallido: campos vacíos.");
            return;
        }

        try {
            UsuarioDTO usuario = usuarioDAO.login(correo, password);

            if (usuario == null) {
                mostrarError("Correo o contraseña incorrectos. Intenta nuevamente.", "Acceso denegado");
                logger.info("Login fallido para correo: {}", correo);
                return;
            }

            String rol = usuario.getRol();
            if (rol == null || rol.trim().isEmpty()) {
                mostrarError("El usuario no tiene un rol asignado. Contacte al administrador.", "Rol no asignado");
                logger.error("Usuario sin rol: {}", usuario);
                return;
            }

            logger.info("Login exitoso: {} con rol {}", correo, rol);
            abrirModuloPorRol(rol.toLowerCase());
            view.dispose();

        } catch (Exception ex) {
            logger.error("Error al procesar login", ex);
            mostrarError("Se produjo un error al procesar el inicio de sesión:\n" + ex.getMessage(), "Error del sistema");
        }
    }

    private void abrirModuloPorRol(String rol) {
        switch (rol) {
            case "sistema":
                mostrarInfo("Acceso concedido al módulo: sistema-cliente", "Bienvenido");
                logger.debug("Abriendo módulo cliente");
                abrirModuloCliente();
                break;

            case "mozo":
                logger.debug("Rol mozo detectado, solicitando selección de módulo.");
                String[] opciones = {"Entrega", "Mesas"};
                int seleccion = JOptionPane.showOptionDialog(
                        view,
                        "¿Qué módulo deseas abrir?",
                        "Seleccione módulo",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        opciones,
                        opciones[0]
                );

                if (seleccion == 0) {
                    logger.info("Módulo seleccionado: Entrega");
                    abrirModuloEntrega();
                } else if (seleccion == 1) {
                    logger.info("Módulo seleccionado: Mesas");
                    abrirModuloMesas();
                } else {
                    logger.info("El usuario canceló la selección de módulo.");
                    mostrarInfo("No se seleccionó ningún módulo. Puedes iniciar sesión nuevamente si fue un error.", "Sin selección");
                }
                break;

            case "cocinero":
                mostrarInfo("Acceso concedido al módulo: sistema-cocina", "Bienvenido");
                logger.debug("Abriendo módulo cocina");
                abrirModuloCocina();
                break;

            case "admin":
                mostrarInfo("Acceso concedido al módulo: sistema-admin", "Bienvenido");
                logger.debug("Abriendo módulo administración");
                abrirModuloAdministracion();
                break;

            default:
                mostrarError("Rol no reconocido: " + rol + ". Contacte a soporte técnico.", "Rol inválido");
                logger.error("Rol no reconocido: {}", rol);
                break;
        }
    }

    private void abrirModuloCliente() {
        SwingUtilities.invokeLater(() -> {
            try {
                new Principal().setVisible(true);
                logger.info("Módulo cliente iniciado.");
            } catch (Exception e) {
                logger.error("Error al abrir módulo cliente", e);
                mostrarError("Error al abrir módulo cliente: " + e.getMessage(), "Error");
            }
        });
    }

    private void abrirModuloAdministracion() {
        SwingUtilities.invokeLater(() -> {
            try {
                new panelAdmin().setVisible(true);
                logger.info("Módulo administración iniciado.");
            } catch (Exception e) {
                logger.error("Error al abrir módulo administración", e);
                mostrarError("Error al abrir módulo administración: " + e.getMessage(), "Error");
            }
        });
    }

    private void abrirModuloCocina() {
        SwingUtilities.invokeLater(() -> {
            try {
                SistemaCocina vista = new SistemaCocina();
                new ControladorSistemaCocina(vista);
                vista.setVisible(true);
                logger.info("Módulo cocina iniciado.");
            } catch (Exception e) {
                logger.error("Error al abrir módulo cocina", e);
                mostrarError("Error al abrir módulo cocina: " + e.getMessage(), "Error");
            }
        });
    }

    private void abrirModuloEntrega() {
        SwingUtilities.invokeLater(() -> {
            try {
                entregaVista vista = new entregaVista();
                new ControladorEntrega(vista);
                vista.setVisible(true);
                logger.info("Módulo entrega iniciado.");
            } catch (Exception e) {
                logger.error("Error al abrir módulo entrega", e);
                mostrarError("Error al abrir módulo entrega: " + e.getMessage(), "Error");
            }
        });
    }

    private void abrirModuloMesas() {
        SwingUtilities.invokeLater(() -> {
            try {
                mesasVista vista = new mesasVista();
                new ControladorGestionMesas(vista);
                vista.setVisible(true);
                logger.info("Módulo mesas iniciado.");
            } catch (Exception e) {
                logger.error("Error al abrir módulo mesas", e);
                mostrarError("Error al abrir módulo mesas: " + e.getMessage(), "Error");
            }
        });
    }

    private void mostrarInfo(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(view, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(view, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }
}
