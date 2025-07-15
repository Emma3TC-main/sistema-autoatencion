/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.controller;

/**
 *
 * @author PC
 */
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BoletaPDFExporter {

    private static final Logger logger = LoggerFactory.getLogger(BoletaPDFExporter.class);

    public static class ProductoLinea {

        String nombre;
        int cantidad;
        double precio;

        public ProductoLinea(String nombre, int cantidad, double precio) {
            this.nombre = nombre;
            this.cantidad = cantidad;
            this.precio = precio;
        }

        public double getSubtotal() {
            return cantidad * precio;
        }
    }

    public static void exportarConSelector(String cliente, String dni, String metodoPago,
            List<ProductoLinea> productos, double igv, double total) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Guardar boleta PDF");
        fileChooser.setSelectedFile(new File("boleta_" + new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + ".pdf"));

        int userSelection = fileChooser.showSaveDialog(null);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            exportar(archivo.getAbsolutePath(), cliente, dni, metodoPago, productos, igv, total);
        } else {
            logger.info("Exportación cancelada por el usuario.");
        }
    }

    public static void exportar(String ruta, String cliente, String dni, String metodoPago,
            List<ProductoLinea> productos, double igv, double total) {
        try (PDDocument doc = new PDDocument()) {
            float margin = 50;
            float yStart = 750;
            float y = yStart;
            float leading = 16;
            float bottomMargin = 50;

            PDPage page = new PDPage(PDRectangle.A4);
            doc.addPage(page);

            AtomicReference<PDPageContentStream> outRef = new AtomicReference<>(
                    new PDPageContentStream(doc, page)
            );
            outRef.get().setLeading(leading);

            // Cabecera
            outRef.get().beginText();
            outRef.get().setFont(PDType1Font.HELVETICA_BOLD, 18);
            outRef.get().newLineAtOffset(margin, y);
            outRef.get().showText("EL GRAN POLLÓN");
            y -= leading;

            outRef.get().setFont(PDType1Font.HELVETICA, 12);
            outRef.get().newLine();
            outRef.get().showText("RUC 1234567890");
            y -= leading;
            outRef.get().newLine();
            outRef.get().showText("Av. Central #123, Lima, Perú");
            y -= leading;
            outRef.get().newLine();
            outRef.get().showText("------------------------------------------------------------");
            y -= leading;

            outRef.get().newLine();
            outRef.get().showText("Cliente: " + cliente);
            y -= leading;
            outRef.get().newLine();
            outRef.get().showText("DNI: " + dni);
            y -= leading;
            outRef.get().newLine();
            outRef.get().showText("Fecha: " + new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date()));
            y -= leading;
            outRef.get().newLine();
            outRef.get().showText("Método de pago: " + metodoPago);
            y -= leading;
            outRef.get().newLine();
            outRef.get().showText("------------------------------------------------------------");
            y -= leading;

            // Encabezado de tabla
            outRef.get().newLine();
            outRef.get().setFont(PDType1Font.HELVETICA_BOLD, 12);
            outRef.get().showText(String.format("%-18s %-8s %-10s %-10s", "Producto", "Cant.", "Precio", "Subtotal"));
            y -= leading;
            outRef.get().newLine();
            outRef.get().setFont(PDType1Font.HELVETICA, 12);
            outRef.get().showText("------------------------------------------------------------");
            y -= leading;

            for (ProductoLinea p : productos) {
                if (y < bottomMargin) {
                    outRef.get().endText();
                    outRef.get().close();

                    PDPage nueva = new PDPage(PDRectangle.A4);
                    doc.addPage(nueva);
                    PDPageContentStream nuevoOut = new PDPageContentStream(doc, nueva);
                    nuevoOut.setLeading(leading);
                    nuevoOut.beginText();
                    nuevoOut.setFont(PDType1Font.HELVETICA, 12);
                    nuevoOut.newLineAtOffset(margin, yStart);

                    outRef.set(nuevoOut);
                    y = yStart;
                }

                outRef.get().newLine();
                outRef.get().showText(String.format("%-18s %-8d %-10.2f %-10.2f",
                        p.nombre, p.cantidad, p.precio, p.getSubtotal()));
                y -= leading;
            }

            outRef.get().newLine();
            outRef.get().showText("------------------------------------------------------------");
            y -= leading;

            // Totales
            outRef.get().newLine();
            outRef.get().showText(String.format("IGV: S/ %.2f", igv));
            y -= leading;

            outRef.get().newLine();
            outRef.get().setFont(PDType1Font.HELVETICA_BOLD, 12);
            outRef.get().showText(String.format("TOTAL: S/ %.2f", total));
            y -= leading;

            outRef.get().newLine();
            outRef.get().setFont(PDType1Font.HELVETICA, 12);
            outRef.get().showText("------------------------------------------------------------");
            y -= leading;

            outRef.get().newLine();
            outRef.get().newLine();
            outRef.get().showText("¡Gracias por tu compra sostenible!");
            y -= leading;

            outRef.get().endText();
            outRef.get().close();

            doc.save(ruta);
            logger.info("PDF exportado correctamente en: {}", ruta);
        } catch (IOException e) {
            logger.error("Error al generar el PDF de boleta: {}", e.getMessage(), e);
            JOptionPane.showMessageDialog(null, "Error al exportar PDF:\n" + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
