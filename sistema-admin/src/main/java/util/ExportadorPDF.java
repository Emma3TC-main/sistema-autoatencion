/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

/**
 *
 * @author PC
 */
import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

public class ExportadorPDF {

    private static final Logger logger = LoggerFactory.getLogger(ExportadorPDF.class);

    public static void imprimir(JTable tabla, String nombreArchivo) {
        String carpeta = "reportes";
        File carpetaDestino = new File(carpeta);
        if (!carpetaDestino.exists()) {
            carpetaDestino.mkdirs();
            logger.info("Carpeta 'reportes' creada.");
        }

        String ruta = carpeta + "/" + nombreArchivo + ".pdf";
        logger.info("Exportando PDF a: {}", ruta);

        try (PDDocument doc = new PDDocument()) {
            float margin = 40;
            float yStart = 540;
            float y = yStart;
            float leading = 20;
            float bottomMargin = 60;

            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            AtomicReference<PDPageContentStream> outRef = new AtomicReference<>(
                    new PDPageContentStream(doc, page)
            );

            outRef.get().setLeading(leading);
            outRef.get().beginText();
            outRef.get().setFont(PDType1Font.HELVETICA_BOLD, 18);
            outRef.get().newLineAtOffset(margin, y);

            outRef.get().showText("SISTEMA DE GESTIÓN - REPORTE");
            y -= leading;
            outRef.get().newLine();
            outRef.get().setFont(PDType1Font.HELVETICA, 12);
            outRef.get().showText("Reporte: " + nombreArchivo.toUpperCase());
            y -= leading;
            outRef.get().newLine();
            outRef.get().showText("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
            y -= leading;

            outRef.get().newLine();
            outRef.get().showText("-------------------------------------------------------------");
            y -= leading;

            TableModel model = tabla.getModel();
            int columnas = model.getColumnCount();
            int anchoPorColumna = 100;

            // Encabezados
            outRef.get().newLine();
            outRef.get().setFont(PDType1Font.HELVETICA_BOLD, 12);
            StringBuilder header = new StringBuilder();
            for (int i = 0; i < columnas; i++) {
                String col = model.getColumnName(i);
                header.append(String.format("%-" + anchoPorColumna + "s", col));
            }
            outRef.get().showText(header.toString());
            y -= leading;

            // Contenido
            outRef.get().setFont(PDType1Font.HELVETICA, 11);
            for (int fila = 0; fila < model.getRowCount(); fila++) {
                if (y < bottomMargin) {
                    outRef.get().endText();
                    outRef.get().close();

                    PDPage nuevaPagina = new PDPage(PDRectangle.A4);
                    doc.addPage(nuevaPagina);

                    PDPageContentStream nuevoStream = new PDPageContentStream(doc, nuevaPagina);
                    nuevoStream.setLeading(leading);
                    nuevoStream.beginText();
                    nuevoStream.setFont(PDType1Font.HELVETICA, 11);
                    nuevoStream.newLineAtOffset(margin, yStart);
                    outRef.set(nuevoStream);
                    y = yStart;
                }

                outRef.get().newLine();
                StringBuilder filaTexto = new StringBuilder();
                for (int col = 0; col < columnas; col++) {
                    Object valor = model.getValueAt(fila, col);
                    filaTexto.append(String.format("%-" + anchoPorColumna + "s", valor != null ? valor.toString() : ""));
                }
                outRef.get().showText(filaTexto.toString());
                y -= leading;
            }

            // Pie de página
            outRef.get().newLine();
            outRef.get().setFont(PDType1Font.HELVETICA_OBLIQUE, 11);
            outRef.get().showText("-------------------------------------------------------------");
            y -= leading;
            outRef.get().newLine();
            outRef.get().showText("Documento generado automáticamente - Gracias por usar el sistema");
            outRef.get().endText();
            outRef.get().close();

            doc.save(ruta);
            logger.info("PDF exportado correctamente: {}", ruta);
            JOptionPane.showMessageDialog(null, "PDF exportado exitosamente:\n" + ruta, "Exportación exitosa", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            logger.error("Error al exportar el PDF", e);
            JOptionPane.showMessageDialog(null, "Error al exportar el PDF:\n" + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
