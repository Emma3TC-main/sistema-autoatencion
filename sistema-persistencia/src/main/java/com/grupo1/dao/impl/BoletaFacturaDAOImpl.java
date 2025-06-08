/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.BoletaFacturaDAO;
import com.grupo1.dto.BoletaFacturaDTO;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public class BoletaFacturaDAOImpl implements BoletaFacturaDAO {

    @Override
    public void insertar(BoletaFacturaDTO boleta) throws SQLException {
        String sql = "{call proc_insertar_boletas_facturas(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, boleta.getIdPedido());
            cstmt.setString(2, boleta.getTipoDocumento());
            cstmt.setBigDecimal(3, boleta.getTotal());
            cstmt.setBigDecimal(4, boleta.getIgv());
            cstmt.setInt(5, boleta.getIdUsuario());
            cstmt.setString(6, boleta.getMetodoPago());
            cstmt.executeUpdate();
        }
    }

    @Override
    public BoletaFacturaDTO buscarPorId(int id) throws SQLException {
        String sql = "{call proc_obtener_boletas_facturas_por_id(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    BoletaFacturaDTO boleta = new BoletaFacturaDTO();
                    boleta.setIdDocumento(rs.getInt("id_documento"));
                    boleta.setIdPedido(rs.getInt("id_pedido"));
                    boleta.setTipoDocumento(rs.getString("tipo_documento"));
                    boleta.setTotal(rs.getBigDecimal("total"));
                    boleta.setIgv(rs.getBigDecimal("igv"));
                    boleta.setIdUsuario(rs.getInt("id_usuario"));
                    boleta.setMetodoPago(rs.getString("metodo_pago"));
                    return boleta;
                }
            }
        }
        return null;
    }

    @Override
    public List<BoletaFacturaDTO> listar() throws SQLException {
        List<BoletaFacturaDTO> lista = new ArrayList<>();
        String sql = "{call proc_obtener_todos_boletas_facturas}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql); ResultSet rs = cstmt.executeQuery()) {
            while (rs.next()) {
                BoletaFacturaDTO boleta = new BoletaFacturaDTO();
                boleta.setIdDocumento(rs.getInt("id_documento"));
                boleta.setIdPedido(rs.getInt("id_pedido"));
                boleta.setTipoDocumento(rs.getString("tipo_documento"));
                boleta.setTotal(rs.getBigDecimal("total"));
                boleta.setIgv(rs.getBigDecimal("igv"));
                boleta.setIdUsuario(rs.getInt("id_usuario"));
                boleta.setMetodoPago(rs.getString("metodo_pago"));
                lista.add(boleta);
            }
        }
        return lista;
    }

    @Override
    public void actualizar(BoletaFacturaDTO boleta) throws SQLException {
        String sql = "{call proc_actualizar_boletas_facturas(?, ?, ?, ?, ?, ?, ?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, boleta.getIdDocumento());
            cstmt.setInt(2, boleta.getIdPedido());
            cstmt.setString(3, boleta.getTipoDocumento());
            cstmt.setBigDecimal(4, boleta.getTotal());
            cstmt.setBigDecimal(5, boleta.getIgv());
            cstmt.setInt(6, boleta.getIdUsuario());
            cstmt.setString(7, boleta.getMetodoPago());
            cstmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "{call proc_eliminar_boletas_facturas(?)}";
        try (CallableStatement cstmt = ConexionSQL.getInstancia().getConexion().prepareCall(sql)) {
            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }
}
