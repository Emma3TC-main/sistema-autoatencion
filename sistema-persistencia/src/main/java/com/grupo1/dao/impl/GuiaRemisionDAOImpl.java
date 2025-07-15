/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.grupo1.dao.impl;

import com.grupo1.conexion.ConexionSQL;
import com.grupo1.dao.GuiaRemisionDAO;
import com.grupo1.dto.GuiaRemisionDTO;
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
public class GuiaRemisionDAOImpl implements GuiaRemisionDAO {

    @Override
    public void insertar(GuiaRemisionDTO guia) throws SQLException {
        if (guia == null) {
            throw new IllegalArgumentException("❌ La guía de remisión no puede ser null.");
        }
        if (guia.getIdPedido() <= 0 || guia.getIdCliente() <= 0) {
            throw new IllegalArgumentException("❌ ID de pedido o cliente inválido.");
        }
        if (guia.getRazonSocial() == null || guia.getRazonSocial().isBlank()) {
            throw new IllegalArgumentException("❌ La razón social es obligatoria.");
        }
        if (guia.getDireccionEntrega() == null || guia.getDireccionEntrega().isBlank()) {
            throw new IllegalArgumentException("❌ La dirección de entrega es obligatoria.");
        }

        String sql = "{call proc_insertar_guias_remision(?,?,?,?,?,?)}"; // 6to parámetro: OUTPUT

        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {

            cstmt.setInt(1, guia.getIdPedido());
            cstmt.setInt(2, guia.getIdCliente());
            cstmt.setString(3, guia.getRazonSocial());
            cstmt.setString(4, guia.getDireccionEntrega());
            cstmt.setString(5, guia.getFirmaEntrega());

            cstmt.registerOutParameter(6, Types.INTEGER);

            int filas = cstmt.executeUpdate();
            if (filas == 0) {
                throw new SQLException("❌ No se insertó la guía de remisión.");
            }

            int idGenerado = cstmt.getInt(6);
            guia.setIdGuia(idGenerado);
        }
    }

    @Override
    public GuiaRemisionDTO buscarPorId(int id) throws SQLException {
        String sql = "{call proc_obtener_guias_remision_por_id(?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, id);
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    GuiaRemisionDTO guia = new GuiaRemisionDTO();
                    guia.setIdGuia(rs.getInt("id_guia"));
                    guia.setIdPedido(rs.getInt("id_pedido"));
                    guia.setIdCliente(rs.getInt("id_cliente"));
                    guia.setRazonSocial(rs.getString("razon_social"));
                    guia.setDireccionEntrega(rs.getString("direccion_entrega"));
                    guia.setFirmaEntrega(rs.getString("firma_entrega"));
                    return guia;
                }
            }
        }
        return null;
    }

    @Override
    public List<GuiaRemisionDTO> listar() throws SQLException {
        List<GuiaRemisionDTO> lista = new ArrayList<>();
        String sql = "{call proc_obtener_todos_guias_remision}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql); ResultSet rs = cstmt.executeQuery()) {
            while (rs.next()) {
                GuiaRemisionDTO guia = new GuiaRemisionDTO();
                guia.setIdGuia(rs.getInt("id_guia"));
                guia.setIdPedido(rs.getInt("id_pedido"));
                guia.setIdCliente(rs.getInt("id_cliente"));
                guia.setRazonSocial(rs.getString("razon_social"));
                guia.setDireccionEntrega(rs.getString("direccion_entrega"));
                guia.setFirmaEntrega(rs.getString("firma_entrega"));
                lista.add(guia);
            }
        }
        return lista;
    }

    @Override
    public void actualizar(GuiaRemisionDTO guia) throws SQLException {
        String sql = "{call proc_actualizar_guias_remision(?, ?, ?, ?, ?, ?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, guia.getIdGuia());
            cstmt.setInt(2, guia.getIdPedido());
            cstmt.setInt(3, guia.getIdCliente());
            cstmt.setString(4, guia.getRazonSocial());
            cstmt.setString(5, guia.getDireccionEntrega());
            cstmt.setString(6, guia.getFirmaEntrega());
            cstmt.executeUpdate();
        }
    }

    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "{call proc_eliminar_guias_remision(?)}";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, id);
            cstmt.executeUpdate();
        }
    }

    @Override
    public int obtenerUltimoIdInsertado() throws SQLException {
        String sql = "SELECT MAX(id_guia) AS ultimo_id FROM Guias_Remision";
        try (Connection con = ConexionSQL.getInstancia().getConexion(); PreparedStatement stmt = con.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("ultimo_id");
            }
        }
        return -1;
    }

}
