/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.BoletaFacturaDAO;
import com.grupo1.dto.BoletaFacturaDTO;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author emma3tc
 */
public class BoletaFacturaDAOImpl implements BoletaFacturaDAO {

    @Override
    public void insertar(BoletaFacturaDTO boleta) throws SQLException {
        if (boleta == null) {
            throw new IllegalArgumentException("❌ La boleta o factura no puede ser null.");
        }
        if (boleta.getIdPedido() <= 0) {
            throw new IllegalArgumentException("❌ ID de pedido inválido.");
        }
        if (boleta.getTipoDocumento() == null || boleta.getTipoDocumento().isBlank()) {
            throw new IllegalArgumentException("❌ Tipo de documento obligatorio.");
        }
        if (boleta.getTotal() == null || boleta.getIgv() == null) {
            throw new IllegalArgumentException("❌ Total e IGV son obligatorios.");
        }
        if (boleta.getMetodoPago() == null || boleta.getMetodoPago().isBlank()) {
            throw new IllegalArgumentException("❌ Método de pago obligatorio.");
        }

        String sql = "{call proc_insertar_boletas_facturas(?,?,?,?,?,?,?)}";  // Último parámetro OUTPUT

        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setInt(1, boleta.getIdPedido());
            cstmt.setString(2, boleta.getTipoDocumento());
            cstmt.setBigDecimal(3, boleta.getTotal());
            cstmt.setBigDecimal(4, boleta.getIgv());
            cstmt.setInt(5, boleta.getIdUsuario());
            cstmt.setString(6, boleta.getMetodoPago());

            cstmt.registerOutParameter(7, Types.INTEGER);

            int filas = cstmt.executeUpdate();
            if (filas == 0) {
                throw new SQLException("❌ No se insertó la boleta o factura.");
            }

            int idGenerado = cstmt.getInt(7);
            boleta.setIdDocumento(idGenerado);
        }
    }

    @Override
    public BoletaFacturaDTO buscarPorId(int id) throws SQLException {
        String sql = "{call proc_obtener_boletas_facturas_por_id(?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {

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
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql); ResultSet rs = cstmt.executeQuery()) {

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
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {

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
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }

    @Override
    public int obtenerUltimoIdInsertado() throws SQLException {
        String sql = "SELECT MAX(id_documento) AS ultimo_id FROM Boletas_Facturas";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("ultimo_id");
            }
        }
        return -1;
    }

}
