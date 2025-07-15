/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.grupo1.vista;

import com.grupo1.controller.ControladorConfirmacion;
import com.grupo1.controller.ControladorRegistro;
import com.grupo1.controller.ControladorResumen;
import com.grupo1.controller.PanelImprimirBoleta;
import com.grupo1.dto.BoletaFacturaDTO;
import com.grupo1.dto.ClienteDTO;
import com.grupo1.dto.ComandaDTO;
import com.grupo1.dto.GuiaRemisionDTO;
import com.grupo1.dto.MesaDTO;
import com.grupo1.dto.PedidoDTO;
import com.grupo1.dto.ProductoDTO;
import com.grupo1.dto.RegistroEntregaDTO;
import com.grupo1.modelo.ProductoSeleccionado;
import java.awt.CardLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author user
 */
public class Principal extends javax.swing.JFrame {

    /**
     * Creates new form Principal
     */
    private CardLayout cardLayout;
    private JPanel panelContenedor;
    private PanelImprimirBoleta panelImprimirBoleta;

    private SeleccionarProducto panelBotones;
    private ParaLlevar_o_Aca panelParaLlevar;
    private Mesas panelMesas;
    private Resumen panelResumen; // Agrega esta línea

    private ControladorResumen controladorResumen; // ← nuevo campo
    private Registro panelRegistro;
    private ControladorRegistro controladorRegistro;

    private String modoConsumo; 

    private Confirmacion panelConfirmacion;
    private ControladorConfirmacion controladorConfirmacion;

    private PedidoDTO pedidoSimulado;
    private BoletaFacturaDTO boletaSimulada;
    private ComandaDTO comandaSimulada;
    private GuiaRemisionDTO guiaSimulada;
    private RegistroEntregaDTO entregaSimulada;

    public Principal() {
        initComponents();
        cardLayout = new CardLayout();
        panelContenedor = new JPanel(cardLayout);

        panelBotones = new SeleccionarProducto(this);
        panelParaLlevar = new ParaLlevar_o_Aca(this);
        panelMesas = new Mesas(this);
        panelResumen = new Resumen(this);
        controladorResumen = new ControladorResumen(panelResumen, this);
// ← nuevo panel
        panelContenedor.add(panelBotones, "Botones");
        panelContenedor.add(panelParaLlevar, "ParaLlevar_o_Aca");
        panelContenedor.add(panelMesas, "Mesas");

        panelContenedor.add(panelResumen, "Resumen"); // ← agregarlo al contenedor
        this.setContentPane(panelContenedor);
        cardLayout.show(panelContenedor, "Botones");
        this.pack();                  // Ajusta al tamaño de los paneles
        this.setLocationRelativeTo(null); // Centra en la pantalla
        this.setResizable(false);    // Opcional: evita cambiar tamaño

        panelRegistro = new Registro(this);
        controladorRegistro = new ControladorRegistro(panelRegistro, this);
        panelContenedor.add(panelRegistro, "Registro");

        panelConfirmacion = new Confirmacion();
        controladorConfirmacion = new ControladorConfirmacion(panelConfirmacion, this);
        panelContenedor.add(panelConfirmacion, "Confirmacion");

        panelImprimirBoleta = new PanelImprimirBoleta(this);
        panelImprimirBoleta.setName("ImprimirBoleta"); // <-- ESTA LÍNEA ES CLAVE
        panelContenedor.add(panelImprimirBoleta, "ImprimirBoleta");

    }

    public void mostrarBoleta() {
        if (panelImprimirBoleta != null) {
            panelImprimirBoleta.mostrarBoleta(this);
        }
    }

    public PanelImprimirBoleta getPanelImprimirBoleta() {
        return panelImprimirBoleta;
    }

    public void setModoConsumo(String modo) {
        this.modoConsumo = modo;
    }

    public String getModoConsumo() {
        return modoConsumo;
    }

    private List<ProductoSeleccionado> productosSeleccionados = new ArrayList<>();

    private ClienteDTO clienteTemporal;

    public void setClienteTemporal(ClienteDTO cliente) {
        this.clienteTemporal = cliente;
    }

    public ClienteDTO getClienteTemporal() {
        return clienteTemporal;
    }

    private String correoTemporal;

    public void setCorreoTemporal(String correo) {
        this.correoTemporal = correo;
    }

    public String getCorreoTemporal() {
        return correoTemporal;
    }

    public void setProductosSeleccionados(List<ProductoSeleccionado> seleccionados) {
        this.productosSeleccionados = seleccionados;
    }

    public List<ProductoSeleccionado> getProductosSeleccionados() {
        return productosSeleccionados;
    }

    public void mostrarPanel(String nombrePanel) {
        cardLayout.show(panelContenedor, nombrePanel);
    }

    public ControladorResumen getControladorResumen() {
        return controladorResumen;
    }

    private List<MesaDTO> mesasSeleccionadas = new ArrayList<>();

    public void setMesasSeleccionadas(List<MesaDTO> mesas) {
        this.mesasSeleccionadas = mesas;
    }

    public List<MesaDTO> getMesasSeleccionadas() {
        return mesasSeleccionadas;
    }

    public Confirmacion getPanelConfirmacion() {
        return panelConfirmacion;
    }

    //Para la insercion DB
    public JPanel getPanel(String nombrePanel) {
        if (panelContenedor == null) {
            return null;
        }
        for (Component comp : panelContenedor.getComponents()) {
            if (comp.getName() != null && comp.getName().equals(nombrePanel)) {
                return (JPanel) comp;
            }
        }
        return null;
    }

    public PedidoDTO getPedidoSimulado() {
        return pedidoSimulado;
    }

    public void setPedidoSimulado(PedidoDTO pedido) {
        this.pedidoSimulado = pedido;
    }

    public BoletaFacturaDTO getBoletaSimulada() {
        return boletaSimulada;
    }

    public void setBoletaSimulada(BoletaFacturaDTO boletaSimulada) {
        this.boletaSimulada = boletaSimulada;
    }

    public ComandaDTO getComandaSimulada() {
        return comandaSimulada;
    }

    public void setComandaSimulada(ComandaDTO comandaSimulada) {
        this.comandaSimulada = comandaSimulada;
    }

    public GuiaRemisionDTO getGuiaSimulada() {
        return guiaSimulada;
    }

    public void setGuiaSimulada(GuiaRemisionDTO guiaSimulada) {
        this.guiaSimulada = guiaSimulada;
    }

    public RegistroEntregaDTO getEntregaSimulada() {
        return entregaSimulada;
    }

    public void setEntregaSimulada(RegistroEntregaDTO entregaSimulada) {
        this.entregaSimulada = entregaSimulada;
    }

    public void reiniciar() {
        this.clienteTemporal = null;
        this.pedidoSimulado = null;
        this.boletaSimulada = null;
        this.comandaSimulada = null;
        this.entregaSimulada = null;
        this.guiaSimulada = null;
        this.productosSeleccionados.clear();
        this.mesasSeleccionadas.clear();
        this.correoTemporal = null;
    }

// Repite lo mismo para boletaSimulada, comandaSimulada, etc.
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            java.util.logging.Logger.getLogger(Principal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> {
            new Principal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
