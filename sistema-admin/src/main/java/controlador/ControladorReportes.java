/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

/**
 *
 * @author PC
 */
import com.grupo1.dao.ReporteDAO;
import com.grupo1.dao.impl.ReporteDAOImpl;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import util.ExportadorExcel;
import util.ExportadorPDF;
import vista.Reportes;

public class ControladorReportes {

    private final Reportes vista;
    private final ReporteDAO dao = new ReporteDAOImpl();

    public ControladorReportes(Reportes vista) {
        this.vista = vista;
        inicializar();
    }

    private void inicializar() {
        cargarTodasLasTablas();
        asociarEventosBusqueda();
        asociarEventosExportar();
        asociarEventosImprimir();
    }

    private void cargarTodasLasTablas() {
        try {
            cargarTabla(vista.getTablaReporteClientes(), dao.listarClientes(), new String[]{"ID", "Nombre", "DNI", "RUC", "Razón Social"});
            cargarTabla(vista.getTablaReportesBoletas(), dao.listarBoletas(), new String[]{"ID", "Tipo", "Fecha Emision", "Total", "IGV", "Método Pago", "Emisor"});
            cargarTabla(vista.getTablaRepoPedidos(), dao.listarPedidos(), new String[]{"ID", "Cliente", "Tipo", "Fecha", "Estado", "Nro Mesa"});
            cargarTabla(vista.getTablaReporteComandas(), dao.listarComandas(), new String[]{"ID", "Pedido", "Fecha", "Estado"});
            cargarTabla(vista.getTablaRepoRegEntregas(), dao.listarRegistrosEntrega(), new String[]{"ID", "Fecha", "Usuario", "Firma", "Supervisor", "Cliente"});
            cargarTabla(vista.getTablaReporteGuiaRem(), dao.listarGuiaRemision(), new String[]{"ID", "Fecha", "Cliente", "Razón Social", "Dirección", "Nombre", "RUC"});
        } catch (SQLException ex) {
            mostrarError("Error al cargar reportes: " + ex.getMessage());
        }
    }

    private void cargarTabla(JTable tabla, List<Object[]> datos, String[] columnas) {
        DefaultTableModel modelo = new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Object[] fila : datos) {
            modelo.addRow(fila);
        }

        tabla.setModel(modelo);
    }

    private void asociarEventosBusqueda() {
        buscarPorID(vista.getBtnBuscarReportesClientes(), vista.getTxtClientesID(), vista.getTablaReporteClientes(), 0);
        buscarPorID(vista.getBtnBuscarRepoBoletas(), vista.getTxtIDBoletaBusqueda(), vista.getTablaReportesBoletas(), 0);
        buscarPorID(vista.getBtnBuscarPedidos(), vista.getTxtIDPedidos(), vista.getTablaRepoPedidos(), 0);
        buscarPorID(vista.getBtnBuscarIDPedidosComandas(), vista.getTxtIDPedidoComandas(), vista.getTablaReporteComandas(), 1);
        buscarPorID(vista.getBtnBuscarRegEntregas(), vista.getTxtIDRegEntregas(), vista.getTablaRepoRegEntregas(), 1);
        buscarPorID(vista.getBtnBuscarGuiaRem(), vista.getTxtIDPedidosGuiaRem(), vista.getTablaReporteGuiaRem(), 1);
    }

    private void buscarPorID(JButton boton, JTextField campo, JTable tabla, int columnaBusqueda) {
        boton.addActionListener(e -> {
            String texto = campo.getText().trim();
            if (texto.isEmpty()) {
                mostrarError("Ingrese un ID.");
                return;
            }

            for (int i = 0; i < tabla.getRowCount(); i++) {
                if (tabla.getValueAt(i, columnaBusqueda).toString().equalsIgnoreCase(texto)) {
                    tabla.setRowSelectionInterval(i, i);
                    tabla.scrollRectToVisible(new Rectangle(tabla.getCellRect(i, 0, true)));
                    return;
                }
            }

            mostrarError("ID no encontrado.");
        });
    }

    private void asociarEventosExportar() {
        vista.getBtnExportarRepoClientes().addActionListener(e
                -> ExportadorExcel.exportar(vista, vista.getTablaReporteClientes(), "reporte_clientes"));
        vista.getBtnExportarRepoBoletas().addActionListener(e
                -> ExportadorExcel.exportar(vista, vista.getTablaReportesBoletas(), "reporte_boletas"));
        vista.getBtnExportarRepoPedidos().addActionListener(e
                -> ExportadorExcel.exportar(vista, vista.getTablaRepoPedidos(), "reporte_pedidos"));
        vista.getBtnExportarReportesComandas().addActionListener(e
                -> ExportadorExcel.exportar(vista, vista.getTablaReporteComandas(), "reporte_comandas"));
        vista.getBtnExportarRepoRegEntregas().addActionListener(e
                -> ExportadorExcel.exportar(vista, vista.getTablaRepoRegEntregas(), "reporte_entregas"));
        vista.getBtnExportarRepoGuiaRem().addActionListener(e
                -> ExportadorExcel.exportar(vista, vista.getTablaReporteGuiaRem(), "reporte_guias_remision"));
    }

    private void asociarEventosImprimir() {
        vista.getBtnRepoImprimir().addActionListener(e
                -> ExportadorPDF.imprimir(vista.getTablaReporteClientes(), "reporte_clientes"));
        vista.getBtnImprimirReporteBoletas().addActionListener(e
                -> ExportadorPDF.imprimir(vista.getTablaReportesBoletas(), "reporte_boletas"));
        vista.getBtnImprimirRepoPedidos().addActionListener(e
                -> ExportadorPDF.imprimir(vista.getTablaRepoPedidos(), "reporte_pedidos"));
        vista.getBtnImprimirRepoComandas().addActionListener(e
                -> ExportadorPDF.imprimir(vista.getTablaReporteComandas(), "reporte_comandas"));
        vista.getBtnImprimirRepoRegEntregas().addActionListener(e
                -> ExportadorPDF.imprimir(vista.getTablaRepoRegEntregas(), "reporte_entregas"));
        vista.getBtnImprimirGuiaRem().addActionListener(e
                -> ExportadorPDF.imprimir(vista.getTablaReporteGuiaRem(), "reporte_guias_remision"));
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(vista, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }
}
