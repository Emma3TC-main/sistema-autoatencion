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
import vista.cocinaVista;
import vista.mesasVista;
import vista.panelAdmin;

//para el jfreme de cliente:
/**
 *
 * @author PC
 */
public class ControladorLogin {

    private LoginFrame view;
    private UsuarioDAO usuarioDAO;

    public ControladorLogin(LoginFrame view) {
        this.view = view;
        this.usuarioDAO = new UsuarioDAOImpl();

        initController();
    }

    private void initController() {
        // Registramos el evento del botón
        view.getBtnLogin().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                procesarLogin();
            }
        });
    }

    private void procesarLogin() {
        String correo = view.getCorreo();
        String password = view.getPassword();

        if (correo.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor complete todos los campos", "Error");
            return;
        }

        try {
            UsuarioDTO usuario = usuarioDAO.login(correo, password);

            if (usuario != null) {
                String rol = usuario.getRol();

                switch (rol) {
                    case "sistema":
                        mostrarInfo("Módulo disponible: sistema-cliente", "Login Correcto");
                        abrirModuloCliente();
                        break;
                    case "mozo":
                        mostrarInfo("Módulo disponible: sistema-entrega / sistema-mesas", "Login Correcto");
                        abrirModuloEntrega();
                        break;
                    case "cocinero":
                        mostrarInfo("Módulo disponible: sistema-cocina", "Login Correcto");
                        abrirModuloCocina();
                        break;
                    case "admin":
                        mostrarInfo("Módulo disponible: sistema-admin", "Login Correcto");
                        abrirModuloAdministracion();
                        break;
                    default:
                        mostrarInfo("Rol no reconocido: " + rol, "Login Correcto");
                        break;
                }

                view.dispose(); // Cerramos la ventana de login

            } else {
                mostrarError("Credenciales incorrectas", "Login Fallido");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarError("Error al procesar login: " + e.getMessage(), "Error");
        }
    }

    private void abrirModuloCliente() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            Principal principalFrame = new Principal();
            principalFrame.setVisible(true);

        });
    }

    private void abrirModuloAdministracion() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            panelAdmin adminFrame = new panelAdmin();
            adminFrame.setVisible(true);
        });
    }

    private void abrirModuloCocina() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            cocinaVista cocinaFrame = new cocinaVista();
            cocinaFrame.setVisible(true);
        });
    }

    private void abrirModuloEntrega() {
        javax.swing.SwingUtilities.invokeLater(() -> {
            mesasVista mesaFrame = new mesasVista();
            mesaFrame.setVisible(true);
        });
    }

    private void mostrarInfo(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(view, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
    }

    private void mostrarError(String mensaje, String titulo) {
        JOptionPane.showMessageDialog(view, mensaje, titulo, JOptionPane.ERROR_MESSAGE);
    }
}
