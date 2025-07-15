/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.controller;

/**
 *
 * @author PC
 */
import com.grupo1.dto.ClienteDTO;
import com.grupo1.vista.Principal;
import com.grupo1.vista.Registro;

import javax.swing.*;

public class ControladorRegistro {

    private final Registro vista;
    private final Principal principal;

    public ControladorRegistro(Registro vista, Principal principal) {
        this.vista = vista;
        this.principal = principal;
        configurarEventos();
    }

    private void configurarEventos() {
        vista.getBtnConfirmar().addActionListener(e -> registrarCliente());
    }

    private void registrarCliente() {
        String nombres = vista.getTxtNombreCliente().getText().trim();
        String apellidos = vista.getTxtApellidosCliente().getText().trim();
        String dni = vista.getTxtDNI().getText().trim();
        String ruc = vista.getTxtRUC().getText().trim();
        String razonSocial = vista.getTxtRazonSocial().getText().trim();
        String correo = vista.getTxtCorreo().getText().trim(); // <- campo importante

        // Validaciones básicas
        if (nombres.isEmpty() || apellidos.isEmpty() || dni.length() != 8) {
            JOptionPane.showMessageDialog(vista, "⚠️ Nombre, apellidos y DNI válido (8 dígitos) son obligatorios.");
            return;
        }

        // Crea el objeto ClienteDTO
        ClienteDTO cliente = new ClienteDTO();
        cliente.setNombre(nombres + " " + apellidos);
        cliente.setDNI(dni);
        cliente.setRUC(ruc.isEmpty() ? null : ruc);
        cliente.setRazonSocial(razonSocial.isEmpty() ? null : razonSocial);

        // Guarda en Principal (en memoria)
        principal.setClienteTemporal(cliente);
        principal.setCorreoTemporal(correo); // ← aquí guardas el correo temporalmente

        JOptionPane.showMessageDialog(vista, "✅ Cliente registrado correctamente. Listo para procesar el pago.");

        // Cambiar al siguiente panel, si deseas
        principal.mostrarPanel("Confirmacion");
    }

}
