/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author PC
 */

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.FileOutputStream;

public class ExportadorExcel {

    /**
     * Exporta una JTable a un archivo Excel (.xlsx)
     * @param parent Componente padre (usualmente JFrame o JPanel)
     * @param tabla JTable a exportar
     * @param nombreArchivo Nombre sugerido para el archivo (sin ruta)
     */
    public static void exportar(JComponent parent, JTable tabla, String nombreArchivo) {
        try {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Guardar como...");
            fileChooser.setSelectedFile(new File(nombreArchivo.endsWith(".xlsx") ? nombreArchivo : nombreArchivo + ".xlsx"));

            int userSelection = fileChooser.showSaveDialog(parent);
            if (userSelection != JFileChooser.APPROVE_OPTION) return;

            File archivo = fileChooser.getSelectedFile();
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("Datos");

            TableModel modelo = tabla.getModel();

            // Encabezado
            Row encabezado = sheet.createRow(0);
            for (int col = 0; col < modelo.getColumnCount(); col++) {
                Cell celda = encabezado.createCell(col);
                celda.setCellValue(modelo.getColumnName(col));
            }

            // Datos
            for (int row = 0; row < modelo.getRowCount(); row++) {
                Row fila = sheet.createRow(row + 1);
                for (int col = 0; col < modelo.getColumnCount(); col++) {
                    Object valor = modelo.getValueAt(row, col);
                    Cell celda = fila.createCell(col);

                    if (valor instanceof Boolean) {
                        celda.setCellValue((Boolean) valor ? "Sí" : "No");
                    } else if (valor instanceof Number) {
                        celda.setCellValue(Double.parseDouble(valor.toString()));
                    } else {
                        celda.setCellValue(valor != null ? valor.toString() : "");
                    }
                }
            }

            // Ajustar columnas
            for (int i = 0; i < modelo.getColumnCount(); i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                workbook.write(fos);
            }

            workbook.close();
            JOptionPane.showMessageDialog(parent, "Exportado correctamente a:\n" + archivo.getAbsolutePath());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(parent, "Error al exportar: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
