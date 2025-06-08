/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dto;

import java.math.BigDecimal;
import java.sql.Timestamp;

/**
 *
 * @author emma3tc
 */
public class BoletaFacturaDTO {
    private int idDocumento;
    private int idPedido;
    private String tipoDocumento;
    private Timestamp fechaEMisiòn;
    private BigDecimal total;
    private BigDecimal igv;
    private int idUsuario;
    private String metodoPago;

    public int getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(int idDocumento) {
        this.idDocumento = idDocumento;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Timestamp getFechaEMisiòn() {
        return fechaEMisiòn;
    }

    public void setFechaEMisiòn(Timestamp fechaEMisiòn) {
        this.fechaEMisiòn = fechaEMisiòn;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public BigDecimal getIgv() {
        return igv;
    }

    public void setIgv(BigDecimal igv) {
        this.igv = igv;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }
    
    
    
}
